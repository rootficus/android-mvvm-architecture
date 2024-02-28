package com.rf.utellRestaurant.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class SignInRequest(

    @SerializedName("user") var user: UserRequest,

    ) : Serializable

data class UserRequest(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,

    ) : Serializable
