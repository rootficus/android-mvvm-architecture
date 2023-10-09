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
    val amount: Long? = null,

    @SerializedName("balance")
    @Expose
    val balance: String? = null,

    ) : Serializable

