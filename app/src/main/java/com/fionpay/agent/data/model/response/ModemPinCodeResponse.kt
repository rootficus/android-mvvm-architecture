package com.fionpay.agent.data.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModemPinCodeResponse(
    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("pincode")
    var pincode: Int? = null,

) : Serializable
