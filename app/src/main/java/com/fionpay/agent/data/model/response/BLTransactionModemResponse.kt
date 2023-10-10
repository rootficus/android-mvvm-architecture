package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BLTransactionModemResponse(
    @SerializedName("customer_number")
    @Expose
    var customerNumber: String? = null,

    @SerializedName("amount")
    @Expose
    var amount: Int? = null,

    @SerializedName("old_balance")
    @Expose
    var oldBalance: Int? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    )
