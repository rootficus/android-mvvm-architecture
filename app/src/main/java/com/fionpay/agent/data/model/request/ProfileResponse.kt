package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProfileResponse(
    @field:SerializedName("full_name")
    var fullName: String? = null,
    @field:SerializedName("profile_image_url")
    var profileImageUrl: String? = null,
) : Serializable
