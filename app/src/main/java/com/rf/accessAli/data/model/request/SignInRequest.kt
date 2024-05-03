package com.rf.accessAli.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class SignInRequest(

    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,

    ) : Serializable