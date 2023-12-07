package com.fionpay.agent.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefundRequest (
    @SerializedName("modem_id")
    @Expose
    var modemId: String?,

    @SerializedName("amount")
    @Expose
    var amount: Double?,
)