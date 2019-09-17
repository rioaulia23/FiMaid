package com.example.FiMaid.FragmentMaid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.Login
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.profilemaid.*

class FragmentAccountMaid : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profilemaid, container, false)
    }

    lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var preferences: PrefHelper
    lateinit var storageReference: StorageReference
    lateinit var firebaseStorage: FirebaseStorage

    lateinit var filePathImage: Uri
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = PrefHelper(context!!)
        fAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .child("alamat").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_alamat_maid.setText(p0.value.toString())
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .child("phone").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_nomor_maid.setText(p0.value.toString())
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .child("name").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_nama_maid.setText(p0.value.toString())
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .child("email").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_email_maid.setText(p0.value.toString())
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .child("age").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_usia1_maid.setText(p0.value.toString())
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .child("password").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_password_maid.setText(p0.value.toString())
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}")
            .child("img").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    Glide.with(context!!).load(p0.value.toString())
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(avamaid)
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })

        fAuth = FirebaseAuth.getInstance()
        logoutmaid.setOnClickListener {
            fAuth.signOut()
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
            activity!!.finish()
        }
        editmaid.setOnClickListener {

            val intent = Intent(context, EditProfileMaid::class.java)
            startActivity(intent)

        }
    }
}