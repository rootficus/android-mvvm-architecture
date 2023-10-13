package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashBoardItemResponse(
    @SerializedName("current_balance")
    @Expose
    val currentBalance: Double? = 0.0,

    @SerializedName("total_balance")
    @Expose
    val totalBalance: Double? = 0.0,

    @SerializedName("today_cash_in")
    @Expose
    val todayCashIn: Double? = 0.0,

    @SerializedName("today_cash_out")
    @Expose
    val todayCashOut: Double? = 0.0,

    @SerializedName("monthly_cash_out")
    @Expose
    val monthlyCashOut: Int? = 0,
    @SerializedName("monthly_cash_in")
    @Expose
    val monthlyCashIn: Int? = 0,

    @SerializedName("number_of_pending_request")
    @Expose
    val numberOfPendingRequest: Long? = 0,

    @SerializedName("approved_transaction")
    @Expose
    val approvedTransaction: Long? = 0,

    @SerializedName("rejected_transaction")
    @Expose
    val rejectedTransaction: Long? = 0,

    @SerializedName("total_setup_modems")
    @Expose
    val totalSetupModems: Long? = 0,

    @SerializedName("active_modems")
    @Expose
    val activeModems: Long? = 0,

    )