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

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("sim_type")
    @Expose
    val simType: String? = null,

    @SerializedName("operator")
    @Expose
    val operator: String? = null,

    @SerializedName("phone_number")
    @Expose val phoneNumber: String? = null,

    @SerializedName("pincode")
    @Expose
    val pinCode: Long? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("balance")
    @Expose
    val balance: String? = null,

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


    ) : Serializable

