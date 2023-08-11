package com.jionex.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashBoardItemResponse(
    @SerializedName("total_modem")
    @Expose
    val totalModem: Int? = 0,

    @SerializedName("today_trx_amount")
    @Expose
    val todayTrxAmount: Double? = 0.0,

    @SerializedName("total_trx_amount")
    @Expose
    val totalTrxAmount: Double? = 0.0,

    @SerializedName("total_transaction")
    @Expose
    val totalTransaction: Double? = 0.0,

    @SerializedName("today_transaction")
    @Expose
    val todayTransaction: Long? = 0,

    @SerializedName("total_pending")
    @Expose
    val totalPending: Long? = 0,

    )