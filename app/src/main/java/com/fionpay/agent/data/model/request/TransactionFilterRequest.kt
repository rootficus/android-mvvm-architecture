package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class TransactionFilterRequest(
    @SerializedName("start_date")
    var startDate: String? = null,
    @SerializedName("end_date")
    var endDate: String? = null,
    @SerializedName("payment_type")
    var paymentType: String? = null,
    @SerializedName("bank_type")
    var bankType: String? = null,
    @SerializedName("modem_slot_id")
    var modemSlotId: Any? = null,

)