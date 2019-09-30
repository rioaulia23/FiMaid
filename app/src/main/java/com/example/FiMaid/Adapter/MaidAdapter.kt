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
import com.example.FiMaid.Model.UserModel
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MaidAdapter : RecyclerView.Adapter<MaidAdapter.MaidViewHolder> {

    var mCtx: Context
    var user: List<UserModel>
    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    override fun onBindViewHolder(holder: MaidViewHolder, position: Int) {
        val maidModel: UserModel = user.get(position)
        Glide.with(mCtx).load(maidModel.img!!)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(holder.img)
        holder.tvnama.text = maidModel.name.toString()
        holder.tv.text = maidModel.age.toString()
        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, DetailMaid::class.java)
            intent.putExtra("nama_maid", maidModel.name)
            intent.putExtra("id1", maidModel.id)
            intent.putExtra("foto_profile1", maidModel.img)
            intent.putExtra("umur1", maidModel.age)
            intent.putExtra("phone1", maidModel.phone)
            intent.putExtra("alamat1", maidModel.alamat)
            intent.putExtra("email1", maidModel.email)


            mCtx.startActivity(intent)
        }
    }

    constructor (mCtx: Context, list: List<UserModel>) {
        this.mCtx = mCtx
        this.user = list
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MaidViewHolder {
        val view: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.item_maid, p0, false)
        val maidViewHolder = MaidViewHolder(view)
        return maidViewHolder
    }

    override fun getItemCount(): Int {
        return user.size
    }

    inner class MaidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var tvnama: TextView
        var tv: TextView
        var img: ImageView


        init {
            ll = itemView.findViewById(R.id.lljob1)
            tvnama = itemView.findViewById(R.id.tvnamamaid1)
            img = itemView.findViewById(R.id.img1)
            tv = itemView.findViewById(R.id.status1)
        }
    }
}