package com.fionpay.agent.data.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class B2BResponse(
    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("modem")
    var modem: String? = null,

    @field:SerializedName("transaction_id")
    var transactionId: String? = null,

    @field:SerializedName("amount")
    var amount: String? = null,

    @field:SerializedName("payment_type")
    var paymentType: String? = null,

    @field:SerializedName("date")
    var date: String? = null,
) : Serializable
