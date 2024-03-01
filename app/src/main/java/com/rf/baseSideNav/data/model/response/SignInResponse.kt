package com.rf.baseSideNav.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class SignInResponse(
    @SerializedName("id")
    @Expose
    var id: String?,

    @SerializedName("token")
    @Expose
    var token: String?,

    @SerializedName("full_name")
    @Expose
    var fullName: String?,

    @SerializedName("country")
    @Expose
    var country: String?,

    @SerializedName("phone")
    @Expose
    var phone: String?,

    ) : Serializable
