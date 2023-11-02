package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class CheckNumberAvailabilityRequest(
    @SerializedName("phone_number")
    var phoneNumber: String? = null,
    @SerializedName("mobile_banking_id")
    var mobileBankingId: Int? = null,
)