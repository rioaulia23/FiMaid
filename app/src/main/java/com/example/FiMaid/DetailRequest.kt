package com.example.FiMaid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.detailprofile2.*

class DetailRequest : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailprofile2)
        fAuth = FirebaseAuth.getInstance()

        val imageProf = intent.getStringExtra("foto_profile7")
        etp_nama3.text = intent.getStringExtra("nama_user7")
        etp_desc3.text = intent.getStringExtra("desc7")
        etp_email3.text = intent.getStringExtra("email7")
        etp_alamat3.text = intent.getStringExtra("alamat7")
        etp_nomor3.text = intent.getStringExtra("phone7")
        etp_age3.text = intent.getStringExtra("umur7")
        gaji3.text = intent.getStringExtra("uang7")

        Glide.with(this).load(imageProf)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(ava7)

        FirebaseDatabase.getInstance()
            .getReference("user/${intent.getStringExtra("uidmaid")}/request/${fAuth.currentUser?.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    status_cok2.text = p0.child("status").value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        ava7.setOnClickListener {
            val intent = Intent(this, Detailfoto::class.java)
            intent.putExtra("img", imageProf)
            startActivity(intent)
        }


    }


}