package com.example.FiMaid.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FiMaid.Adapter.UserAdapter
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.Model.UserModel
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.home.*


class FragmentHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home, container, false)
    }


    private var list: MutableList<UserModel> = ArrayList()
    var listview = rc_list
    private var userAdapter: UserAdapter? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var fAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var pref: PrefHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = PrefHelper(context!!)
        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView = view.findViewById(R.id.rc_list)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)
        fAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()
            .getReference("user")
        dbRef.orderByChild("role").equalTo("maid")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    list = ArrayList()
                    for (dataSnapshot in data.children) {
                        val addDataAll =
                            dataSnapshot.getValue(UserModel::class.java)
                        addDataAll!!.key = dataSnapshot.key
                        if (addDataAll.verified.toString() == "verified") {
                            list.add(addDataAll)
                        }
                        userAdapter = UserAdapter(context!!, list)
                        recyclerView!!.adapter = userAdapter
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

