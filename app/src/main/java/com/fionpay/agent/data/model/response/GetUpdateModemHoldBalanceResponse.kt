package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetUpdateModemHoldBalanceResponse(
    @SerializedName("modem_id")
    @Expose
    val modemId: String? = null,

    @SerializedName("balance")
    @Expose
    val balance: Float? = null,

    @SerializedName("hold_balance")
    @Expose
    val holdBalance: Float? = null,

    @SerializedName("available_balance")
    @Expose
    val availableBalance: Float? = null
) : Serializable

