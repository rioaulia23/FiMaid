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
import com.example.FiMaid.DetailRequest
import com.example.FiMaid.Model.UserModel
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class RequestAdapter : RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    var user: List<UserModel>
    var mCtx: Context

    lateinit var dbRef: DatabaseReference
    lateinit var fauth: FirebaseAuth

    override fun onBindViewHolder(holder: RequestAdapter.RequestViewHolder, position: Int) {
        val userModel: UserModel = user.get(position)
        Glide.with(mCtx).load(userModel.img!!)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(holder.img)
        holder.tvnama.text = userModel.name.toString()
        holder.tv.text = userModel.verified.toString()

        holder.ll.setOnClickListener {
            val intent = Intent(mCtx, DetailRequest::class.java)
            intent.putExtra("nama_user7", userModel.name)
            intent.putExtra("id7", userModel.id)
            intent.putExtra("foto_profile7", userModel.img)
            intent.putExtra("umur7", userModel.age)
            intent.putExtra("phone7", userModel.phone)
            intent.putExtra("desc7", userModel.desc)
            intent.putExtra("alamat7", userModel.alamat)
            intent.putExtra("email7", userModel.email)
            intent.putExtra("uang7", userModel.salary)
            intent.putExtra("uidmaid", userModel.id)


            mCtx.startActivity(intent)
        }
    }

    constructor (mCtx: Context, list: List<UserModel>) {
        this.mCtx = mCtx
        this.user = list
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RequestViewHolder {
        val view: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.item_history, p0, false)
        val maidViewHolder = RequestViewHolder(view)
        return maidViewHolder
    }

    override fun getItemCount(): Int {
        return user.size
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll: LinearLayout
        var tvnama: TextView
        var tv: TextView
        var img: ImageView


        init {
            ll = itemView.findViewById(R.id.llrequest)
            tvnama = itemView.findViewById(R.id.tvnamarequest)
            img = itemView.findViewById(R.id.imgrequest)
            tv = itemView.findViewById(R.id.statusrequest)
        }
    }
}