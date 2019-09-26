package com.example.FiMaid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.detailfoto.*

class Detailfoto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailfoto)

        val imageProf = intent.getStringExtra("foto_profile")
        Glide.with(this).load(imageProf)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(foto)

    }
}