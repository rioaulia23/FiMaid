package com.example.FiMaid.Fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder.VideoSource.CAMERA
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Log.e
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.editprofile.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class EditProfile : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    private lateinit var fAuth: FirebaseAuth
    lateinit var preferences: PrefHelper
    lateinit var storageReference: StorageReference
    lateinit var firebaseStorage: FirebaseStorage
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    lateinit var filePathImage2: Uri
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

    fun GetFileExtension(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                filePathImage2 = data?.data!!
                try {
                    val bitmap: Bitmap = MediaStore
                        .Images.Media.getBitmap(
                        this.contentResolver, filePathImage2
                    )
                    Glide.with(this).load(bitmap)
                        .override(250, 250)
                        .centerCrop().into(ava3)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
            CAMERA -> {
                val thumbnail = data!!.extras!!.get("data") as Bitmap

                Glide.with(this)
                    .load(thumbnail)
                    .into(imgview)
                filePathImage2 = Uri.fromFile(File(saveImage(thumbnail)))
//                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                imgview.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofile)
        preferences = PrefHelper(this)
        fAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference




        ava3.setOnClickListener {
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

        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_nama1.setText(p0.child("name").value.toString())
                    etp_alamat1.setText(p0.child("alamat").value.toString())
                    etp_nomor1.setText(p0.child("phone").value.toString())
                    etp_usia2.setText(p0.child("age").value.toString())
                    Glide.with(applicationContext).load(p0.child("img").value.toString())
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(ava3)
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })

        tes2.setOnClickListener {
            bar2.visibility = View.VISIBLE
            tes2.visibility = View.GONE
            val uidUser = fAuth.currentUser?.uid
            val counter = preferences.getUID()
            dbRef = FirebaseDatabase.getInstance().reference
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Error", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val editno = etp_nomor1.text.toString()
                    val editname = etp_nama1.text.toString()
                    val editalamat = etp_alamat1.text.toString()
                    val editusia = etp_usia2.text.toString()

                    try {
                        val storageRef: StorageReference = storageReference
                            .child(
                                "img/$uidUser/${preferences.getUID()}.${GetFileExtension(
                                    filePathImage
                                )}"
                            )

                        storageRef.putFile(filePathImage).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener {
                                dbRef.child("user/$uidUser/img").setValue(it.toString())
                            }
                        }.addOnFailureListener {
                            Log.e("TAG_ERROR", it.message)
                        }.addOnProgressListener { taskSnapshot ->
                            value = (100.0 * taskSnapshot
                                .bytesTransferred / taskSnapshot.totalByteCount)

                        }
                    } catch (e: UninitializedPropertyAccessException) {
                        Toast.makeText(this@EditProfile, "Sukses", Toast.LENGTH_SHORT).show()
                    }
                    dbRef.child("user/$uidUser/alamat").setValue(editalamat)
                    dbRef.child("user/$uidUser/name").setValue(editname)
                    dbRef.child("user/$uidUser/phone").setValue(editno)
                    dbRef.child("user/$uidUser/age").setValue(editusia)
                    Toast.makeText(this@EditProfile, "Sukses", Toast.LENGTH_SHORT).show()

                }
            })

            finish()
        }

    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(this.filesDir, "mydir")
        // have the object build the directory structure, if needed.
        Log.e("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            e("path", f.path)
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e("error", "saveImage", e1)
        }

        return ""
    }
}