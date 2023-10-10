package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModemItemModel(
    @field:SerializedName("first_name")
    var firstName: String? = null,
    @field:SerializedName("last_name")
    var lastName: String? = null,
    @field:SerializedName("pincode")
    var pinCode: Long? = null,
) : Serializable
