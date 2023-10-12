package com.fionpay.agent.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class FilterResponse(
    @field:SerializedName("banks")
    var banks: List<Bank>? = null,
    @field:SerializedName("modems")
    var modems: List<Modem>? = null
) : Serializable

data class Bank(
    @SerializedName("bank_id")
    @Expose
    var bankId: Int? = null,

    @SerializedName("bank_name")
    @Expose
    var bankName: String? = null
)

data class Modem(
    @SerializedName("modem_slot_id")
    @Expose
    var modemSlotId: Int? = null,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null
)