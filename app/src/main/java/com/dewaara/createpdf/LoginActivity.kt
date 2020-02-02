package com.dewaara.createpdf

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.dewaara.createpdf.LoginActivity
import com.dewaara.createpdf.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
  //  private var logoImageView: ImageView? = null
    private var loginButton: Button? = null
    private var registerButton: Button? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var firebaseAuth: FirebaseAuth? = null
   /// private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
      //  logoImageView = findViewById(R.id.logo_iamgeview)
        email = findViewById(R.id.emailogin_edittext)
        password = findViewById(R.id.password_edittext)
        registerButton = findViewById(R.id.register_button2)
        loginButton = findViewById(R.id.login_button)
        firebaseAuth = FirebaseAuth.getInstance()
        email?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputs()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputs()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        /////////////////////////////////////////////
        loginButton?.setOnClickListener(View.OnClickListener { checkEmailAndPassword() })


       // IF USER LOGIN PAGE ON THE REGISTER BUTTON-2 ON CLICK THEN GO BACK TO THE SignUpActivity AND FULLFIL UP THE INFORMATION
       registerButton?.setOnClickListener {
           val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
           startActivity(intent)
           finish()
       } // END OF THE PROCESS
    }

    private fun checkInputs() {
        if (!TextUtils.isEmpty(email!!.text)) {
            if (TextUtils.isEmpty(password!!.text)) {
                // loginButton.setEnabled( true );
// loginButton.setTextColor( Color.rgb(255,255,255) );
            } else {
                //   loginButton.setEnabled( false );
//   loginButton.setTextColor( Color.argb(50,255,255,255) );
            }
        } else {
            //  loginButton.setEnabled( false );
//   loginButton.setTextColor( Color.argb(50,255,255,255) );
        }
    }

    private fun checkEmailAndPassword() {
    ///    if (email!!.text.toString().matches(emailPattern)) {
            if (password!!.length() >= 6) {
//  loginButton.setEnabled( false );
//   loginButton.setTextColor( Color.argb(50,255,255,255) );
                firebaseAuth!!.signInWithEmailAndPassword(email!!.text.toString(), password!!.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(mainIntent)
                                finish()
                            } else {
// loginButton.setEnabled( true );
//   loginButton.setTextColor( Color.rgb(255,255,255) );
                              //  val error = task.exception!!.message
                             //   Toast.makeText(this@LoginActivity, "Error!!", Toast.LENGTH_SHORT).show()
                            }
                        }
            } else {
                Toast.makeText(this@LoginActivity, "Please Enter Email & Password", Toast.LENGTH_SHORT).show()
            }
       /// } else
        {
            Toast.makeText(this@LoginActivity, "Incorrect email or password", Toast.LENGTH_SHORT).show()
        }
    }

}
