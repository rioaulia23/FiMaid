package com.example.FiMaid.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class RequestModel(
    var id_boss: String? = null,
    var name: String? = null,
    var status: String? = null
) : Serializable