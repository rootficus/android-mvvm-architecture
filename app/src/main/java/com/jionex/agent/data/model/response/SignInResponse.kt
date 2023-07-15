package com.jionex.agent.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class SignInResponse(
    @SerializedName("permissions")
    @Expose
    var permissions: Permissions?,

    @SerializedName("token")
    @Expose
    var token: String?,

    @SerializedName("userDetail")
    @Expose
    var userDetail: UserDetail?,

    @SerializedName("userRoles")
    @Expose
    var userRoles: List<String>?,

) : Serializable


@Keep
data class UserDetail(
    @SerializedName("company_id")
    @Expose
    var company_id: String?,

    @SerializedName("country")
    @Expose
    var country: String?,

    @SerializedName("created_at")
    @Expose
    var created_at: String?,

    @SerializedName("email")
    @Expose
    var email: String?,

    @SerializedName("full_name")
    @Expose
    var full_name: String?,

    @SerializedName("id")
    @Expose
    var id: String?,
    @SerializedName("merchant_id")
    @Expose
    var merchant_id: String?,

    @SerializedName("parent_id")
    @Expose
    var parent_id: String?,

    @SerializedName("phone")
    @Expose
    var phone: String?,

    @SerializedName("role_id")
    @Expose
    var role_id: String?,

    @SerializedName("updated_at")
    @Expose
    var updated_at: String?,

    @SerializedName("user_name")
    @Expose
    var user_name: String?,

    ) : Serializable

@Keep
data class Permissions(

    @SerializedName("modules")
    @Expose
    var modules: List<Any>?

) : Serializable

@Keep
data class Modules(

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("permissions")
    @Expose
    var permissions: String?,

    @SerializedName("isEnabled")
    @Expose
    var isEnabled: Boolean?,
) : Serializable
