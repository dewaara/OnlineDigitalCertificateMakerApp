package com.dewaara.createpdf

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BinaryBitmap
import com.google.zxing.WriterException


import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*



class SignUpActivity : AppCompatActivity() {
    var txt_fullName: EditText? = null
    var txt_username: EditText? = null
    var txt_email: EditText? = null
    var txt_password: EditText? = null
    var btn_register: Button? = null
    var radioGenderMale: RadioButton? = null
    var radioGenderFemale: RadioButton? = null
    var databaseReference: DatabaseReference? = null
    var gender = ""
    var firebaseAuth: FirebaseAuth? = null
   // var qrImage: ImageView? = null
    var printCertificate: TextView? = null
   // var profile_image: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        txt_fullName = findViewById<View>(R.id.txt_full_name) as EditText
        txt_username = findViewById<View>(R.id.txt_username) as EditText
        txt_email = findViewById<View>(R.id.txt_email) as EditText
        txt_password = findViewById<View>(R.id.txt_password) as EditText
        btn_register = findViewById<View>(R.id.btn_register) as Button
        radioGenderMale = findViewById<View>(R.id.radio_male) as RadioButton
        radioGenderFemale = findViewById<View>(R.id.radio_female) as RadioButton
      //  qrImage = findViewById<View>(R.id.qrcodeImage) as ImageView
        printCertificate = findViewById<View>(R.id.textView_RegOk) as TextView
      //  profile_image = findViewById<View>(R.id.DP) as Button





        // START OF USER RGISTATION PROCESS REALTIME FIRE..DB IN CREATE A CHILD REFERENCE ID OF IN SIDE THE USER INFO. LIKE-FULL NEMAE
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        firebaseAuth = FirebaseAuth.getInstance()


        btn_register!!.setOnClickListener {
            val fullName = txt_fullName!!.text.toString()
            val username = txt_username!!.text.toString()
            val email = txt_email!!.text.toString()
            val password = txt_password!!.text.toString()
            if (radioGenderMale!!.isChecked) {
                gender = "Male"
            }
            if (radioGenderFemale!!.isChecked) {
                gender = "Female"

            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@SignUpActivity, "Please Enter The Email", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(fullName)) {
                Toast.makeText(this@SignUpActivity, "Enter Your Full Name", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@SignUpActivity, "Please Enter Password", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this@SignUpActivity, "Enter The Username", Toast.LENGTH_SHORT).show()
            }

            firebaseAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this@SignUpActivity) { task ->
                if (task.isSuccessful) {

                    // UPLOAD PROFILE IMAGE(DP) TO STORE IN FIREBASE DATABASE
                    uploadImageToFirebaseStorage()



                    Toast.makeText(this@SignUpActivity, "Registation Sucessfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))

                } else {

                    Toast.makeText(this@SignUpActivity, "Please Filup the form", Toast.LENGTH_LONG).show()
                }
                // ...

            }

        }


        // STRAT OF  QR CODE IN USER TEXT LIKE:- ENTER FULL NAME
        radioGenderMale!!.setOnClickListener {
              val data = txt_fullName!!.text.toString()
            val qrgEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, 700)
            try {
                val qrBits = qrgEncoder.encodeAsBitmap()
                qrcodeImage!!.setImageBitmap(qrBits)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
        radioGenderFemale!!.setOnClickListener {
            val data = txt_fullName!!.text.toString()
            val qrgEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, 700)
            try {
                val qrBits = qrgEncoder.encodeAsBitmap()
                qrcodeImage!!.setImageBitmap(qrBits)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

        // END OF QR CODE PROCESS

        // SENT THE USER QRCODE TO THE FIREBASE DATABASE START PROCESS
        checkbox_tc!!.setOnClickListener {
            // Get the data from an ImageView as bytes


            qrcodeImage!!.isDrawingCacheEnabled = true
            qrcodeImage!!.buildDrawingCache()
            val bitmap = qrcodeImage!!.drawingCache
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            for (i in 0..1 step 1) {
              //  println(i)


                val path = "QRCode/" + i + ".png"

                val storageRef = FirebaseStorage.getInstance().getReference(path)
                val uploadTask = storageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    val downloadUrl = taskSnapshot.uploadSessionUri!!.toString()
                    Log.d("onSuccess", "" + downloadUrl)

                }

            }
            //}  //END OF THE QRCODE UPLOADING PROCESS

        }
        // UPLOAD USER FROFILE IMAGE/PHOTO IN DP TO DIRECT ACESS AT THE LOCAL COMPUTER GLARRY IMAGE E.G:- IT'S BUTTON


              DP.setOnClickListener {
                  Log.d("SignUpActivity", "Try to show selecter")

                  val intent = Intent(Intent.ACTION_PICK)
                  intent.type = "image/*"
                  startActivityForResult(intent, 0)
              }


    // IF USER ALLREDY REGISTER INFORMATION AND THEN ONLY PRINT CERTIFICATE
        // ON THE FUTURE CASES THEN CLICK ON(PRINT CERTIFICATE TEXT) AND GO TO THE NEXT LEVEL Login Activity OPEN BLA...BLA.
            printCertificate?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }


    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("SignUpActivity", "Suceessfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("SignUpActivity", "File location: $it")

                        saveUserToFirebaseDatabase(it.toString(), qrcodesImageUrl = toString())
                    }
                }
                .addOnFailureListener {
                    // do some logging here
                }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String,qrcodesImageUrl: String) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        val user = Users(uid, txt_fullName?.text.toString(),
                txt_username?.text.toString(),
                txt_email?.text.toString(),
                txt_password?.text.toString(),
                profileImageUrl,
                gender, qrcodesImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("SignUpActivity", "Finally i saved the user to Firebase database:")
                }
    }

    var selectedPhotoUri: Uri? =null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // process and check what the selected image was.....
            Log.d("SignUpActivity", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            DP_imageview.setImageBitmap(bitmap)

            DP.alpha = 0f

        }
    }

    // END OF THE PROFILE IMAGE (DP) PROCESS

    ///////////////////////////



    /////////////////////////

    }
class Users(val uid: String, val username: String, val profileImageUrl: String, val fullName: String,  val gender: String, val Email: String,val password: String, val qrcodesImageUrl: String)



