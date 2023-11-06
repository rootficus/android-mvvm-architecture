package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetAddModemResponse(
    @SerializedName("modem")
    @Expose
    val modem: Modem? = null, //
    @SerializedName("modem_slots")
    @Expose
    val modemSlots: List<ModemSlot>? = null, //

) : Serializable

data class Modem(
    @SerializedName("id")
    @Expose
    val id: String? = null, //

    @SerializedName("device_id")
    @Expose
    val deviceId: String? = null,//

    @SerializedName("token")
    @Expose
    val token: String? = null,//

    @SerializedName("device_info")
    @Expose
    val deviceInfo: String? = null,//

    @SerializedName("is_active")
    @Expose
    val isActive: String? = null,//

    @SerializedName("user_id")
    @Expose
    val userId: String? = null,//

    @SerializedName("first_name")
    @Expose
    val firstName: String? = null,//

    @SerializedName("last_name")
    @Expose
    val lastName: String? = null,//

    @SerializedName("modem_status")
    @Expose
    val modemStatus: String? = null,//

    @SerializedName("pincode")
    @Expose
    val pinCode: Long? = null,//

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,//

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,//

    @SerializedName("balance")
    @Expose
    val balance: String? = null,//

    @SerializedName("last_seen")
    @Expose
    val lastSeen: String? = null,//

    @SerializedName("is_hold")
    @Expose
    val isHold: String? = null,//
    @SerializedName("is_login")
    @Expose
    val isLogin: Boolean? = null,//
    @SerializedName("availablity")
    @Expose
    val availablity: Boolean? = null,//
) : Serializable

data class ModemSlot(
    @SerializedName("id")
    @Expose
    val id: Long? = null, //

    @SerializedName("modem_id")
    @Expose
    val modemId: String? = null,//

    @SerializedName("sim_type")
    @Expose
    val simType: String? = null,//

    @SerializedName("operator")
    @Expose
    val operator: String? = null,//

    @SerializedName("phone_number")
    @Expose
    val phoneNumber: String? = null,//

    @SerializedName("sim_id")
    @Expose
    val simId: String? = null,//

    @SerializedName("mobile_banking_id")
    @Expose
    val mobileBankingId: Int? = null,//

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,//

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,//

    @SerializedName("status")
    @Expose
    val status: Boolean? = null,//

    @SerializedName("verify_bank")
    @Expose
    val verifyBank: Boolean? = null,//

    @SerializedName("verify_phone")
    @Expose
    val verifyPhone: Boolean? = null,//

    @SerializedName("last_bank_balance")
    @Expose
    val lastBankBalance: String? = null,//
) : Serializable

