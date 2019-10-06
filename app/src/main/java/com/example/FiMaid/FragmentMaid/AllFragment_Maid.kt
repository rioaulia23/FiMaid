package com.example.FiMaid.FragmentMaid

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
import kotlinx.android.synthetic.main.bottom_nav_maid.*

class AllFragment_Maid : AppCompatActivity() {
    lateinit var pref: PrefHelper
    val manager = supportFragmentManager

    val jobFragment = JobFragment()

    val fragmentAccountMaid = FragmentAccountMaid()
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.list -> {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.content1, jobFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.account1 -> {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.content1, fragmentAccountMaid)
                    transaction.addToBackStack(null)
                    transaction.commit()
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
        setContentView(R.layout.bottom_nav_maid)
        preferences = PrefHelper(this)
        fAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    tv_nama_maid.text = p0.child("name").value.toString()
                    Glide.with(applicationContext).load(p0.child("img").value.toString())
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(avamaid1)
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })

        val navView: BottomNavigationView = findViewById(R.id.nav_view_maid)

        val transaction = manager.beginTransaction()
        transaction.replace(R.id.content1, jobFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

    override fun onBackPressed() {
        finish()
    }
}
