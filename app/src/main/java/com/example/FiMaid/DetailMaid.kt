package com.example.FiMaid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.detailprofile.*

class DetailMaid : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailprofile)

        val imageProf = intent.getStringExtra("foto_profile")
        etp_nama2.text = intent.getStringExtra("nama_user")
        etp_desc.text = intent.getStringExtra("desc")
        etp_email.text = intent.getStringExtra("email")
        etp_alamat.text = intent.getStringExtra("alamat")
        etp_nomor2.text = intent.getStringExtra("phone")
        etp_age.text = intent.getStringExtra("umur")
        Glide.with(this).load(imageProf)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(ava2)

        ava2.setOnClickListener {
            val intent = Intent(this, DetailMaid::class.java)
            startActivity(intent)
        }
    }

}