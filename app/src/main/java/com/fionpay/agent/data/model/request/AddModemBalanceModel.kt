package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class AddModemBalanceModel(
    @field:SerializedName("modem_id")
    var modemId: String?,
    @field:SerializedName("amount")
    var amount: Double?,
)
