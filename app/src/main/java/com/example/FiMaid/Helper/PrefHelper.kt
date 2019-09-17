package com.example.FiMaid.Helper

import android.content.Context
import android.content.SharedPreferences

class PrefHelper {
    val COUNTER_ID = "counter"
    val USER_ID = "uidx"

    var mContext: Context
    var sharedSet: SharedPreferences

    constructor(ctx: Context) {
        mContext = ctx
        sharedSet = mContext.getSharedPreferences(
            "DB",
            Context.MODE_PRIVATE
        )
    }

    fun saveUID(uid: String) {
        val edit = sharedSet.edit()
        edit.putString(USER_ID, uid)
        edit.apply()
    }

    fun getUID(): String? {
        return sharedSet.getString(USER_ID, "")
    }

    fun saveCounterId(counter: Int) {
        val edit = sharedSet.edit()
        edit.putInt(COUNTER_ID, counter)
        edit.apply()
    }

    fun getCounterId(): Int {
        return sharedSet.getInt(COUNTER_ID, 1)
    }


}