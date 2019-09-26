package com.example.FiMaid.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserModel(
    var name: String? = null
    , var email: String? = null
    , var id: String? = null
    , var alamat: String? = null
    , var img: String? = null
    , var verified: String? = null
    , var key: String? = null
    , var age: String? = null
    , var phone: String? = null
    , var desc: String? = null
)