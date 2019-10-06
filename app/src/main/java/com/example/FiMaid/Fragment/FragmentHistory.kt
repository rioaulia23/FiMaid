package com.example.FiMaid.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.FiMaid.Adapter.RequestAdapter
import com.example.FiMaid.Helper.PrefHelper
import com.example.FiMaid.Model.UserModel
import com.example.FiMaid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.history.*

class FragmentHistory : Fragment() {
    lateinit var dbRef: DatabaseReference
    lateinit var pref: PrefHelper
    private var confiadosAdapter: RequestAdapter? = null
    private lateinit var fAuth: FirebaseAuth
    private var recyclerView: RecyclerView? = null
    private var list: ArrayList<UserModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rc_history.layoutManager = LinearLayoutManager(activity!!)
        getData()
    }

    fun getData() {
        FirebaseDatabase.getInstance().getReference("user/")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    list = ArrayList()
                    p0.children.forEach {
                        if (it.child("request").exists()) {
                            it.child("request").children.forEach { it2 ->
                                if (it2.key!! == FirebaseAuth.getInstance().uid) {
                                    val dataUser = it.getValue(UserModel::class.java)
                                    list.add(dataUser!!)
                                }
                            }
                        }
                    }
                    rc_history.adapter = RequestAdapter(activity!!, list)
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
    }
}