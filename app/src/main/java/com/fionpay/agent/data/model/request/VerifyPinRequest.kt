package com.fionpay.agent.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class VerifyPinRequest(
    @SerializedName("pincode") var pinCode: String?,
) : Serializable
