package com.fionpay.agent.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReturnBalanceRequest (
    @SerializedName("amount")
    @Expose
    var amount: Double?,

    @SerializedName("notes")
    @Expose
    var notes: String?,
)