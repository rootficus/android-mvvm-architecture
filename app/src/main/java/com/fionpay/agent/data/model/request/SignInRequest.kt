package com.fionpay.agent.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class SignInRequest(

    @SerializedName("email") var email: String,

    @SerializedName("password") var password: String,

    @SerializedName("device_token") var deviceToken: String,

    ) : Serializable
