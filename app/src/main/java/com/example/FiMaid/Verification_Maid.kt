package com.example.FiMaid

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.FiMaid.Helper.PrefHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.verification_act.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Verification_Maid : AppCompatActivity() {

    lateinit var dbRef: DatabaseReference
    private lateinit var fAuth: FirebaseAuth
    lateinit var preferences: PrefHelper
    lateinit var storageReference: StorageReference
    lateinit var firebaseStorage: FirebaseStorage
    val REQUEST_CODE_IMAGE = 10002
    val PERMISSION_RC = 10003
    var value = 0.0
    lateinit var filePathImage: Uri
    var Foto: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_act)
        preferences = PrefHelper(this)
        fAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference


        imgver.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.CAMERA
                    ), PERMISSION_RC
                )
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, MediaRecorder.VideoSource.CAMERA)
            }


        }
        btn_verification.setOnClickListener {

            if (Foto!!.toLong() != 1L) {
                Toast.makeText(this, "Foto Harus Di isi", Toast.LENGTH_SHORT).show()
            } else {
                bar2.visibility = View.VISIBLE
                val uidUser = fAuth.currentUser?.uid
                val counter = preferences.getUID()
                FirebaseDatabase.getInstance()
                    .getReference("user/${fAuth.currentUser?.uid}")
                    .child("verified").setValue("Sedang di proses")
                dbRef = FirebaseDatabase.getInstance().reference
                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("Error", p0.message)
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        try {
                            val storageRef: StorageReference = storageReference
                                .child(
                                    "verification/$uidUser/${preferences.getUID()}.${GetFileExtension(
                                        filePathImage
                                    )}"
                                )

                            storageRef.putFile(filePathImage).addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener {
                                    dbRef.child("user/$uidUser/verification")
                                        .setValue(it.toString())
                                }
                            }.addOnFailureListener {
                                Log.e("TAG_ERROR", it.message)
                            }.addOnProgressListener { taskSnapshot ->
                                value = (100.0 * taskSnapshot
                                    .bytesTransferred / taskSnapshot.totalByteCount)

                            }
                        } catch (e: UninitializedPropertyAccessException) {
                            Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                finish()
            }
        }
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
                        .centerCrop().into(imgver)
                } catch (x: IOException) {
                    x.printStackTrace()
                }
            }
            MediaRecorder.VideoSource.CAMERA -> {
                val thumbnail = data!!.extras!!.get("data") as Bitmap

                Glide.with(this)
                    .load(thumbnail)
                    .into(imgver)
                filePathImage = Uri.fromFile(File(saveImage(thumbnail)))
//                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                Foto = 1
            }
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
            Log.e("path", f.path)
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
            Log.e("error", "saveImage", e1)
        }

        return ""
    }

    fun GetFileExtension(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

}