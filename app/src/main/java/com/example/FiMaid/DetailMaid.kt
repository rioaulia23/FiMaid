package com.example.FiMaid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.detailprofile.*

class DetailMaid : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailprofile)
        fAuth = FirebaseAuth.getInstance()

        val imageProf = intent.getStringExtra("foto_profile")
        etp_nama2.text = intent.getStringExtra("nama_user")
        etp_desc.text = intent.getStringExtra("desc")
        etp_email.text = intent.getStringExtra("email")
        etp_alamat.text = intent.getStringExtra("alamat")
        etp_nomor2.text = intent.getStringExtra("phone")
        etp_age.text = intent.getStringExtra("umur")
        gaji1.text = intent.getStringExtra("uang")

        Glide.with(this).load(imageProf)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(ava2)

        ava2.setOnClickListener {
            val intent = Intent(this, Detailfoto::class.java)
            intent.putExtra("img", imageProf)
            startActivity(intent)
        }
        btn_hire.setOnClickListener {
            showDialog()
        }


    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Meminta")
        builder.setMessage("Apakah anda yakin akan memintanya untuk bekerja pada anda?")
        builder.setPositiveButton("YES") { dialog, which ->
            simpanToFirebase()
            Toast.makeText(applicationContext, "Permintaan di kirim.", Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("No") { dialog, which ->
            Toast.makeText(applicationContext, "Tidak.", Toast.LENGTH_SHORT)
                .show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun simpanToFirebase() {
        val iduser = intent.getStringExtra("id")
        val fauth = fAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("user/$iduser/request/${fAuth.uid}")
        dbRef.child("id_boss").setValue(fauth)
        dbRef.child("status").setValue("Requested")
    }
}