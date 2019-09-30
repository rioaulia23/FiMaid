package com.example.FiMaid.Model

data class UserModel(
    var name: String? = ""
    , var email: String? = ""
    , var id: String? = ""
    , var alamat: String? = ""
    , var img: String? = ""
    , var verified: String? = ""
    , var key: String? = ""
    , var age: String? = ""
    , var phone: String? = ""
    , var desc: String? = ""
    , var salary: String? = ""
    , var request: RequestModel? = null
)