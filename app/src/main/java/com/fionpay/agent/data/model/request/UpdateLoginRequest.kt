package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateLoginRequest(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("login_status")
    var loginStatus: String,
)