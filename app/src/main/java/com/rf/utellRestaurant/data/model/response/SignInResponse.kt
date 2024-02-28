package com.rf.utellRestaurant.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class SignInResponse(

    @SerializedName("token")
    @Expose
    var token: String?,

    @SerializedName("user")
    @Expose
    var user: UserResponse?,

    ) : Serializable

data class UserResponse(

    @SerializedName("id")
    @Expose
    var id: Long?,

    @SerializedName("email")
    @Expose
    var email: String?,

    @SerializedName("first_name")
    @Expose
    var firstName: String?,

    @SerializedName("last_name")
    @Expose
    var lastName: String?,

    @SerializedName("created_at")
    @Expose
    var createdAt: String?,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String?,

    @SerializedName("vendor_id")
    @Expose
    var vendorId: String?,

    @SerializedName("role_id")
    @Expose
    var roleId: String?,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String?,

    @SerializedName("password")
    @Expose
    var password: String?,

    @SerializedName("password_confirmed")
    @Expose
    var passwordConfirmed: String?,

    ) : Serializable