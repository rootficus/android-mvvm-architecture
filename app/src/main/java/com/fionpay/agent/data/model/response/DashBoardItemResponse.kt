package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashBoardItemResponse(
    @SerializedName("today_cash_id")
    @Expose
    val todayCashId: Double? = 0.0,

    @SerializedName("today_cash_out")
    @Expose
    val todayCashOut: Double? = 0.0,

    @SerializedName("current_balance")
    @Expose
    val currentBalance: Double? = 0.0,

    @SerializedName("number_of_modems")
    @Expose
    val numberOfModems: Int? = 0,

    @SerializedName("total_cash_id")
    @Expose
    val totalCashId: Long? = 0,

    @SerializedName("total_cash_out")
    @Expose
    val totalCashOut: Long? = 0,

    )