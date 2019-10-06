package com.example.FiMaid.FragmentMaid

import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FiMaid.Adapter.MaidAdapter
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.Model.RequestModel
import com.example.FiMaid.Model.UserModel
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.home.*

class JobFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.job, container, false)
    }

    private var list: MutableList<UserModel> = ArrayList()
    var listview = rc_list
    private var userAdapter: MaidAdapter? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var pref: PrefHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = PrefHelper(context!!)
        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView = view.findViewById(R.id.rc_maid)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)
        fAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("user/${fAuth.uid}/request")
        dbRef.orderByChild("status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                list = ArrayList()
                for (dataRequest in data.children) {
                    val addDataAll = dataRequest.getValue(RequestModel::class.java)
                    FirebaseDatabase.getInstance().getReference("user/${addDataAll!!.id_boss}")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {

                                e("cpoooooooool", addDataAll.id_boss!!)
                                list.add(
                                    UserModel(
                                        p0.child("name").value.toString(),
                                        p0.child("email").value.toString(),
                                        p0.child("id").value.toString(),
                                        p0.child("alamat").value.toString(),
                                        p0.child("img").value.toString(),
                                        p0.child("verified").value.toString(),
                                        p0.key,
                                        p0.child("age").value.toString(),
                                        p0.child("phone").value.toString(),
                                        "",
                                        "",
                                        addDataAll
                                    )
                                )

                                userAdapter = MaidAdapter(context!!, list)
                                recyclerView!!.adapter = userAdapter

                            }

                            override fun onCancelled(p0: DatabaseError) {
                            }

                        })
                }
                e("cBoss", list.size.toString())
                for (boss in list) {
                    e("bos", boss.name!!)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(
                    "TAG_ERROR", p0.message
                )
            }
        })
    }
}