package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddModemSlotsModel(
    @field:SerializedName("modem_id")
    var modemId: String? = null,
    @field:SerializedName("modem_slots")
    var arrayListModemSlotList: List<AddModelSlot>? = null,
) : Serializable

data class AddModelSlot(
    @field:SerializedName("phone_number")
    var phoneNumber: String? = null,
    @field:SerializedName("mobile_banking_id")
    var mobileBankingId: Int? = null,
)
