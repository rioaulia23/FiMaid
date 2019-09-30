package com.example.FiMaid.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.FiMaid.DetailMaid
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.Model.UserModel
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    var mCtx: Context
    var user: List<UserModel>
    lateinit var pref: PrefHelper
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userModel: UserModel = user.get(position)



        Glide.with(mCtx).load(userModel.img!!)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(holder.img)
        holder.tvnama.text = userModel.name.toString()

        holder.status.text = userModel.verified.toString()
        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, DetailMaid::class.java)
            intent.putExtra("nama_user", userModel.name)
            intent.putExtra("id", userModel.id)
            intent.putExtra("foto_profile", userModel.img)
            intent.putExtra("umur", userModel.age)
            intent.putExtra("phone", userModel.phone)
            intent.putExtra("desc", userModel.desc)
            intent.putExtra("alamat", userModel.alamat)
            intent.putExtra("email", userModel.email)
            intent.putExtra("uang", userModel.salary)

            mCtx.startActivity(intent)
        }
    }

    constructor (mCtx: Context, list: List<UserModel>) {
        this.mCtx = mCtx
        this.user = list
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserViewHolder {
        val view: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.itemjob, p0, false)
        val userViewHolder = UserViewHolder(view)
        return userViewHolder
    }

    override fun getItemCount(): Int {
        return user.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var tvnama: TextView
        var status: TextView
        var img: ImageView


        init {
            ll = itemView.findViewById(R.id.lljob)
            tvnama = itemView.findViewById(R.id.tvnamamaid)
            status = itemView.findViewById(R.id.status)
            img = itemView.findViewById(R.id.img)
        }
    }

}