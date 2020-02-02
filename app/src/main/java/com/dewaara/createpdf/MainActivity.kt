package com.dewaara.createpdf

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var webView: WebView? = null
   // private var fullName: String? = null
   // private var Gender: String? = null
   // private var Username: String? = null
 //   private var Email: String? = null
    private var ProfileImage: String? = null
    private var Email: String? = null
    private var Password: String? = null
    private var PhoneNumber: String? = null
    private var UID: String? = null
    private var UserName: String? = null

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val mAuthListener: AuthStateListener? = null
    private var myRef: DatabaseReference? = null
    private var userID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webview1)
        mAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        myRef = mFirebaseDatabase!!.reference
        val user = mAuth?.getCurrentUser()
        userID = user!!.uid

        myRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    ProfileImage = ds.child(userID!!).child("email").value.toString()
                   // Email = ds.child(userID!!).child("fullName").value.toString()
                  //  Password = ds.child(userID!!).child("gender").value.toString()
                   // PhoneNumber = ds.child(userID!!).child("profileImageUrl").value.toString()
                    UID = ds.child(userID!!).child("uid").value.toString()
                    UserName = ds.child(userID!!).child("username").value.toString()

                    val html = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<body> \n" +
                            "<center>\n" +
                            "<style>\n" +
                            ".signature, .title { \n" +
                            "float:left;\n" +
                            "  border-top: 1px solid #000;\n" +
                            "  width: 150px height:150px; \n" +
                            "  text-align: center;\n" +
                            "}\n" +
                            "</style>\n" +
                            "<div style=\"width:700px; height:500px; padding:20px; text-align:center; border: 10px solid #787878\">\n" +
                            "<div style=\"width:650px; height:450px; padding:20px; text-align:center; border: 5px solid #787878\">\n" +
                            "\t<span style=\"font-size:50px; font-weight:bold\">Certificate of Completion </span>\n" +
                            "<br><br>\n" +
                            "       <span style=\"font-size:25px\"><i>This is to certify that</i><img src=$ProfileImage width=\"100\" height=\"120\"style=\"margin-top:10px;float:left\"><img src=\"https://firebasestorage.googleapis.com/v0/b/seven-29b38.appspot.com/o/Quiz%202019%2FQR.jpeg?alt=media&token=34814ac5-603c-42d5-a11a-7046d5525a55\" width=\"120\" height=\"120\"style=\"margin-top:10px;float:right\"></span>\n" +
                            "       <br><br>\n" +
                            "       <span style=\"font-size:30px\"><b>$UserName</b></span><br/><br/>\n" +
                            "       <span style=\"font-size:25px\"><i>Has completed the course</i></span> <br/><br/>\n" +
                            "       <span style=\"font-size:30px\">GIZA QUIZ</span> \n" +
                            "<br/><br/>\n" +
                            "       <span style=\"font-size:20px\">with score of <b>Passed- 85%</b></span>\n" +
                            " <br/><br/>\n" +
                            "       <span style=\"font-size:20px\"><i>Certificate ID:-</i></span><br>\n" +
                            "       <span style=\"font-size:22px\"><i>$UID</i></span><br>\n" +
                            "<table style=\"margin-top:1px;float:left\">\n" +
                            "     <tr>\n" +
                            "            <td><span><b><img src=\"https://firebasestorage.googleapis.com/v0/b/seven-29b38.appspot.com/o/Quiz%202019%2FPicsArt_01-21-09.40.54%20(1).png?alt=media&token=8bf147c4-2596-4c3b-b7ae-d8edd8d2cfac\" width=\"80\" height=\"100\"></b></td>\n" +
                            "     \n" +
                            "      \n" +
                            "</table>\n" +
                            "<table style=\"margin-top:40px;float:right\">\n" +
                            "    <tr>\n" +
                            "            <td><span><img src=\"https://firebasestorage.googleapis.com/v0/b/seven-29b38.appspot.com/o/Quiz%202019%2FPicsArt_01-23-12.41.47%20(1).png?alt=media&token=9c0bf8ba-12d8-44a9-be2b-9bce4b0c335d\" width=\"180\" height=\"70\"></td>\n" +
                            " \n" +
                            " \n" +
                            "</table>\n" +
                            "</div>\n" +
                            "</div>\n" +
                            "</center>\n" +
                            "</html>"

                    webView?.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    // CREATE THE WEBWIEW TO PRINT IN PDF FORMET AND DOWNLOAD THE LOCAL MOBILE STORE
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun CreatePdf(view: View?) {
        val context: Context = this@MainActivity
        val printManager = this@MainActivity.getSystemService(Context.PRINT_SERVICE) as PrintManager
        var adapter: PrintDocumentAdapter? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            adapter = webView!!.createPrintDocumentAdapter()
        }
        val JobName = getString(R.string.app_name) + "Document"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val printJob = printManager.print(JobName, adapter, PrintAttributes.Builder().build())
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}



