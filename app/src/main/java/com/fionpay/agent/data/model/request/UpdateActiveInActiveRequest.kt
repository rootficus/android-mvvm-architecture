package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateActiveInActiveRequest(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("status")
    var status: String,
)