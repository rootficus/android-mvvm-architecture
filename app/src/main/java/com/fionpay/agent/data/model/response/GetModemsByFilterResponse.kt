package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetModemsListResponse(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("first_name")
    @Expose
    val firstName: String? = null,

    @SerializedName("last_name")
    @Expose
    val lastName: String? = null,

    @SerializedName("balance")
    @Expose
    var balance: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("availablity")
    @Expose
    val availability: String? = null,

    @SerializedName("login_status")
    @Expose
    val loginStatus: String? = null,

    @SerializedName("total_cash_in")
    @Expose
    val totalCashIn: Long? = null,

    @SerializedName("total_cash_out")
    @Expose
    val totalCashOut: Long? = null,

    @SerializedName("today_cash_in")
    @Expose
    val todayCashIn: Long? = null,

    @SerializedName("today_cash_out")
    @Expose
    val todayCashOut: Long? = null,

    @SerializedName("slots")
    @Expose
    val slots: List<Slots>? = null,

    @SerializedName("pincode")
    @Expose
    val pinCode: Long? = null,

    ) : Serializable

data class Slots(
    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("operator")
    @Expose
    val operator: String? = null,

    @field:SerializedName("mobile_banking_id")
    @Expose
    var mobileBankingId: Int? = null,

    @SerializedName("bank_name")
    @Expose
    val bankName: String? = null,

    @SerializedName("bank_image")
    @Expose
    val bankImage: String? = null,

    @SerializedName("phone_number")
    @Expose
    val phoneNumber: String? = null,

    @SerializedName("modem_slot_id")
    @Expose
    val modemSlotId: String? = null,

    )

