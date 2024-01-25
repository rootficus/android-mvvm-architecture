package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashBoardItemResponse(
    @SerializedName("balance")
    @Expose
    val balance: Float? = 0f,

    @SerializedName("hold_balance")
    @Expose
    val holdBalance: Float? = 0f,

    @SerializedName("available_balance")
    @Expose
    val availableBalance: Float? = null,

    @SerializedName("today_cash_in")
    @Expose
    val todayCashIn: Float? = 0f,

    @SerializedName("today_cash_out")
    @Expose
    val todayCashOut: Float? = 0f,

    @SerializedName("monthly_cash_out")
    @Expose
    val monthlyCashOut: Float? = 0f,
    @SerializedName("monthly_cash_in")
    @Expose
    val monthlyCashIn: Float? = 0f,

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