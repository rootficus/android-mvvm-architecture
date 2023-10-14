package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransactionModemResponse(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("bank_image")
    @Expose
    var bankImage: String? = null,

    @SerializedName("customer")
    @Expose
    var customer: String? = null,

    @SerializedName("transation_id")
    @Expose
    var transactionId: String? = null,

    @SerializedName("amount")
    @Expose
    var amount: String? = null,

    @SerializedName("bank_type")
    @Expose
    var bankType: String? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null,

    @SerializedName("payment_type")
    @Expose
    var paymentType: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    )
