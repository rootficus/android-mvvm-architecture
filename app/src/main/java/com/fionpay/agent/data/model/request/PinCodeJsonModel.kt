package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class PinCodeJsonModel(
    @field:SerializedName("pincode")
    var pincode: Int,
)
