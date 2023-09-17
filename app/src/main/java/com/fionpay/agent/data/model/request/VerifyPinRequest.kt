package com.fionpay.agent.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class VerifyPinRequest(

    @SerializedName("userId") var userId: String?,

    @SerializedName("pincode") var pincode: String?,


) : Serializable
