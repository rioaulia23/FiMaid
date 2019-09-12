package com.example.FiMaid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.selectrole.*

class SelectRole : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectrole)

        maid.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        boss.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }
}