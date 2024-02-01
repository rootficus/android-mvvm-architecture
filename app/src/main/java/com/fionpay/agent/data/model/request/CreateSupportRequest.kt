package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateSupportRequest(
    @field:SerializedName("subject")
    var subject: String? = null,
    @field:SerializedName("description")
    var description: String? = null,
)