package com.example.FiMaid.Fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.bottom_nav.*

class AllFragment : AppCompatActivity() {
    lateinit var pref: PrefHelper
    val manager = supportFragmentManager

    val fragmentHome = FragmentHome()
    val fragmentHistory = FragmentHistory()
    val fragmentAccount = FragmentAccount()
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.content, fragmentHome)
                transaction.addToBackStack(null)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.calender -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.content, fragmentHistory)
                transaction.addToBackStack(null)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }

            R.id.account -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.content, fragmentAccount)
                transaction.addToBackStack(null)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        true
    }
    lateinit var fAuth: FirebaseAuth
    lateinit var preferences: PrefHelper
    lateinit var storageReference: StorageReference
    lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_nav)
        preferences = PrefHelper(this)
        fAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    tv_nama.text = p0.child("name").value.toString()
                    Glide.with(applicationContext).load(p0.child("img").value.toString())
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(ava2)
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val transaction = manager.beginTransaction()
        transaction.replace(R.id.content, fragmentHome)
        transaction.addToBackStack(null)
        transaction.commit()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

    override fun onBackPressed() {
        finish()
    }
}
