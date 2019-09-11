package com.example.FiMaid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.FiMaid.Helper.PrefHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.signup.*
import java.io.IOException
import java.util.*

class Register : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    lateinit var helperPref: PrefHelper
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var fAuth: FirebaseAuth
    lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        fAuth = FirebaseAuth.getInstance()
        helperPref = PrefHelper(this)
        storageReference = FirebaseStorage.getInstance().reference

        btn_signup.setOnClickListener {
            var name = et_nama.text.toString()

            var phone = et_nomor.text.toString()
            var email = et_email.text.toString()
            var password = et_password.text.toString()
            var alamat = et_alamat.text.toString()
            if (name.isNotEmpty() || alamat.isNotEmpty() || phone.isNotEmpty() || email.isNotEmpty() || password.isNotEmpty()
            ) {
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            simpanToFirebase(name, alamat, email, password, phone)
                            Toast.makeText(this, "Register Berhasil!", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        } else {
                            Toast.makeText(this, "Value must be 6 or more digit!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "There's some empty input!", Toast.LENGTH_SHORT).show()
            }
        }
        upload.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), PERMISSION_RC
                        )
                    } else {
                        imageChooser()
                    }
                }
                else -> {
                    imageChooser()
                }
            }
        }
    }

    private fun imageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            REQUEST_CODE_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                filePathImage = data?.data!!
                try {
                    val bitmap: Bitmap = MediaStore
                        .Images.Media.getBitmap(
                        this.contentResolver, filePathImage
                    )
                    Glide.with(this).load(bitmap)
                        .override(250, 250)
                        .centerCrop().into(upload)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
        }
    }

    fun simpanToFirebase(name: String, alamat: String, email: String, password: String, phone: String) {
        val uidUser = fAuth.currentUser?.uid
        val uid = helperPref.getUID()
        val nameXXX = UUID.randomUUID().toString()
        val storageRef: StorageReference = storageReference
            .child("img/$uid/$nameXXX.${GetFileExtension(filePathImage)}")
        storageRef.putFile(filePathImage).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                dbRef = FirebaseDatabase.getInstance().getReference("user/$uidUser")
                dbRef.child("/id").setValue(uidUser)
                dbRef.child("/name").setValue(name)
                dbRef.child("/alamat").setValue(alamat)
                dbRef.child("/email").setValue(email)
                dbRef.child("/password").setValue(password)
                dbRef.child("/phone").setValue(phone)
                dbRef.child("/img").setValue(it.toString())
            }
            Toast.makeText(
                this,
                "Success Upload",
                Toast.LENGTH_SHORT
            ).show()

        }.addOnFailureListener {
            Log.e("TAG_ERROR", it.message)
        }.addOnProgressListener { taskSnapshot ->
            value = (100.0 * taskSnapshot
                .bytesTransferred / taskSnapshot.totalByteCount)

        }
        startActivity(Intent(this, Login::class.java))

    }

    fun GetFileExtension(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
