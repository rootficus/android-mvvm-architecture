package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetModemsByFilterResponse(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("operator")
    @Expose
    val operator: String? = null,

    @SerializedName("phone_number")
    @Expose
    val phoneNumber: String? = null,

    @SerializedName("device_id")
    @Expose
    val deviceId: String? = null,

    @SerializedName("sim_id")
    @Expose
    val simId: Int? = null,

    @SerializedName("token")
    @Expose
    val token: String? = null,

    @SerializedName("sms_type")
    @Expose val smsType: String? = null,

    @SerializedName("device_info")
    @Expose
    val deviceInfo: String? = null,

    @SerializedName("is_active")
    @Expose
    val isActive: Boolean? = null,

    @SerializedName("user_id")
    @Expose
    val userId: String? = null,

    @SerializedName("modem_action")
    @Expose
    val modemAction: String? = null,

    @SerializedName("pincode")
    @Expose
    val pincode: Long? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

): Serializable

