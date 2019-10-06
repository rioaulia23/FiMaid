package com.example.FiMaid.FragmentMaid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.Login
import com.example.FiMaid.R
import com.example.FiMaid.Verification_Maid
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
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    etp_alamat_maid.text = p0.child("alamat").value.toString()
                    gaji.text = p0.child("salary").value.toString()
                    etp_nomor_maid.text = p0.child("phone").value.toString()
                    etp_nama_maid.text = p0.child("name").value.toString()
                    etp_email_maid.text = p0.child("email").value.toString()
                    etp_usia1_maid.text = p0.child("age").value.toString()
                    etp_password_maid.text = p0.child("password").value.toString()
                    desc1.text = p0.child("desc").value.toString()
                    etp_verified.text = p0.child("verified").value.toString()
                    Glide.with(context!!).load(p0.child("img").value.toString())
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(avamaid)
                    if (p0.child("verified").value.toString() == "Verified" || p0.child("verified").value.toString() == "Sedang di proses") {
                        verified.visibility = GONE
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })


        fAuth = FirebaseAuth.getInstance()
        logoutmaid.setOnClickListener {
            showDialog()
        }
        editmaid.setOnClickListener {

            val intent = Intent(context, EditProfileMaid::class.java)
            startActivity(intent)


        }
        val CAMERA_REQUEST_CODE = 5

        verified.setOnClickListener {
            val intent = Intent(context, Verification_Maid::class.java)
            startActivity(intent)


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Activity.RESULT_OK && data != null) {
        } else {
            Toast.makeText(activity, "tes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Keluar?")
        builder.setMessage("Anda akan keluar dari akun ini")
        builder.setPositiveButton("YES") { dialog, which ->
            preferences.setStatus(false)
            fAuth.signOut()
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
            activity!!.finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            Toast.makeText(context, "Tidak", Toast.LENGTH_SHORT).show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}




