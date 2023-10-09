package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateAvailabilityRequest(
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("availablity")
    var availablity: String,
)