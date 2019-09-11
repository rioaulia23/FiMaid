package com.example.FiMaid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.FiMaid.Helper.PrefHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity() {
    private val RC_SIGN_IN = 7
    private lateinit var mGoogleSignIn: GoogleSignInClient
    private lateinit var fAuth: FirebaseAuth
    private lateinit var helperPref: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        helperPref = PrefHelper(this)
        fAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignIn = GoogleSignIn.getClient(this, gso)
//        sign_button_google.setOnClickListener {
//            signIN()
//        }
        tv_signup.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        btn_login.setOnClickListener {
            val email = et_email1.text.toString()
            val pass = et_password1.text.toString()

            if (email.isNotEmpty() || pass.isNotEmpty() ||
                !email.equals("") || !pass.equals("")
            ) {
                fAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener {
                        startActivity(Intent(this, AllFragment::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this, "Login Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this, "Login Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun signIN() {
        val signInIntent = mGoogleSignIn.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("FAUTH_LOGIN", "firebaseAuth : ${account.id}")

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        fAuth.signInWithCredential(credential).addOnCompleteListener(this) {
            if (it.isSuccessful) {
//                Toast.makeText(this, "Login Successful Welcome ${fAuth.currentUser!!.displayName}",
//                    Toast.LENGTH_SHORT).show()
                val user = fAuth.currentUser
                updateUI(user)
            } else {
                Toast.makeText(
                    this, "Failed to Login",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(
                this, "Welcome",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, AllFragment::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("AUTH_LOGIN", "LOGIN FAILED", e)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = fAuth.currentUser
        if (user != null)
            updateUI(user)

    }
}