package com.example.FiMaid

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.detailboss.*

class DetailBoss : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference
    lateinit var fAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailboss)
        fAuth = FirebaseAuth.getInstance()

        val imageProf = intent.getStringExtra("foto_profile1")
        etp_nama3.text = intent.getStringExtra("nama_maid")
        etp_email1.text = intent.getStringExtra("email1")
        etp_alamat2.text = intent.getStringExtra("alamat1")
        etp_nomor3.text = intent.getStringExtra("phone1")
        etp_age1.text = intent.getStringExtra("umur1")

        Glide.with(this).load(imageProf)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(ava4)

        FirebaseDatabase.getInstance()
            .getReference("user/${fAuth.currentUser?.uid}/request/${intent.getStringExtra("id1")}")
            .child("status").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val status = p0.value.toString()
                        when (status) {
                            "Confirmed" -> {
                                status_cok.visibility = View.VISIBLE
                                content_acc_rej.visibility = View.GONE
                                status_cok.text = "confirmed"
                            }
                            "Rejected" -> {
                                status_cok.visibility = View.VISIBLE
                                content_acc_rej.visibility = View.GONE
                                status_cok.text = "rejected"
                            }
                        }
                    }

                }
            )

        ava4.setOnClickListener {
            val intent = Intent(this, Detailfoto::class.java)
            intent.putExtra("img", imageProf)
            startActivity(intent)
        }
        btn_hire1.setOnClickListener {
            showDialog()
        }
        reject.setOnClickListener {
            showDialog1()
        }

    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Job Anda!")
        builder.setMessage("Terima job ini?")
        builder.setPositiveButton("YES") { dialog, which ->
            FirebaseDatabase.getInstance()
                .getReference("user/${fAuth.currentUser?.uid}/request/${intent.getStringExtra("id1")}")
                .child("status").setValue("Confirmed")
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showDialog1() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Job Anda!")
        builder.setMessage("Tolak job ini?")
        builder.setPositiveButton("YES") { dialog, which ->
            FirebaseDatabase.getInstance()
                .getReference("user/${fAuth.currentUser?.uid}/request/${intent.getStringExtra("id1")}")
                .child("status").setValue("Rejected")

            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}