package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class GetModemsByFilterRequest(
    @field:SerializedName("page_size")
    var page_size: Int = 0,
    @field:SerializedName("page_number")
    var page_number: Int? = null,
/*    @field:SerializedName("sender")
    var sender: String? = null,
    @field:SerializedName("agent_account_no")
    var agent_account_no: Int? = null,
    @field:SerializedName("transaction_id")
    var transaction_id: String? = null,
    @field:SerializedName("type")
    var type: String? = null,
    @field:SerializedName("from")
    var from: String? = null,
    @field:SerializedName("to")
    var to: String? = null,*/
    @field:SerializedName("is_active")
    var is_active: Boolean? = true,
)