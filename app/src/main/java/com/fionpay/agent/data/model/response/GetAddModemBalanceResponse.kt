package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetAddModemBalanceResponse(
    @SerializedName("modem_id")
    @Expose
    val modemId: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("balance")
    @Expose
    val balance: Float? = null,

    @SerializedName("hold_balance")
    @Expose
    val holdBalance: Float? = null,

    @SerializedName("available_balance")
    @Expose
    val availableBalance: Float? = null,

    @SerializedName("agent_balance")
    @Expose
    val agentBalance: Float? = null
) : Serializable

