package com.fionpay.agent.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAgentB2BRequest(
    @SerializedName("id")
    @Expose
    var id: String?,

    @SerializedName("modem_id")
    @Expose
    var modemId: String?,

    @SerializedName("start_date")
    @Expose
    var startDate: String?,

    @SerializedName("end_date")
    @Expose
    var endDate: String?,
)