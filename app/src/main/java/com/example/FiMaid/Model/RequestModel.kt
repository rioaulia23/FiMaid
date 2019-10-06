package com.example.FiMaid.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class RequestModel(
    var key: String? = null,
    var id_boss: String? = null,
    var name: String? = null,
    var status: String? = null
) : Serializable