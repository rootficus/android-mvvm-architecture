package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateModemHoldBalanceModel(
    @field:SerializedName("modem_id")
    var modemId: String?,
    @field:SerializedName("hold_balance")
    var holdBalance: Double?,
)
