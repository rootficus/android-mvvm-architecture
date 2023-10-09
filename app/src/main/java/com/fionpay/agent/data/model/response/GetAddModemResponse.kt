package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetAddModemResponse(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("sim_type")
    @Expose
    val simType: String? = null,

    @SerializedName("operator")
    @Expose
    val operator: String? = null,

    @SerializedName("phone_number")
    @Expose val phoneNumber: String? = null,

    @SerializedName("device_id")
    @Expose
    val deviceId: String? = null,

    @SerializedName("sim_id")
    @Expose
    val simId: String? = null,

    @SerializedName("token")
    @Expose
    val token: String? = null,

    @SerializedName("device_info")
    @Expose
    val deviceInfo: String? = null,

    @SerializedName("is_active")
    @Expose
    val isActive: String? = null,

    @SerializedName("user_id")
    @Expose
    val userId: String? = null,

    @SerializedName("modem_action")
    @Expose
    val modemAction: String? = null,

    @SerializedName("first_name")
    @Expose
    val firstName: String? = null,

    @SerializedName("last_name")
    @Expose
    val lastName: String? = null,

    @SerializedName("modem_status")
    @Expose
    val modemStatus: String? = null,


    @SerializedName("pincode")
    @Expose
    val pinCode: Long? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("balance")
    @Expose
    val balance: String? = null,

    @SerializedName("last_seen")
    @Expose
    val lastSeen: String? = null,
    @SerializedName("pin")
    @Expose
    val pin: String? = null,
    @SerializedName("bank_id")
    @Expose
    val bankId: String? = null,
    @SerializedName("is_default")
    @Expose
    val isDefault: String? = null,
    @SerializedName("is_hold")
    @Expose
    val isHold: String? = null,


    ) : Serializable

