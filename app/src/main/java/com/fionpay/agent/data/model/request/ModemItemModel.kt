package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class ModemItemModel(
    @field:SerializedName("first_name")
    var firstName: String,
    @field:SerializedName("last_name")
    var lastName: String,
    @field:SerializedName("pincode")
    var pinCode: Long,
)
