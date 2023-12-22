package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetModemSlotsResponse(
    @SerializedName("modem_slot_id")
    @Expose
    val modemSlotId: Long? = null, //
    @SerializedName("phone_number")
    @Expose
    val phoneNumber: String? = null,//

    @SerializedName("mobile_banking_id")
    @Expose
    val mobileBankingId: Int? = null,//

) : Serializable

