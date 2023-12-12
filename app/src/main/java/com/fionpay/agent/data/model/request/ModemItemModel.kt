package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModemItemModel(
    @field:SerializedName("first_name")
    var firstName: String? = null,
    @field:SerializedName("last_name")
    var lastName: String? = null,
    @field:SerializedName("pincode")
    var pinCode: Long? = null,
    @field:SerializedName("balance")
    var balance: Double? = null,
    @field:SerializedName("modem_slots")
    var modemSlots: List<ModelSlots>? = null,
) : Serializable

data class ModelSlots(
    @field:SerializedName("phone_number")
    var phoneNumber: String? = null,
    @field:SerializedName("mobile_banking_id")
    var mobileBankingId: Int? = null,
)
