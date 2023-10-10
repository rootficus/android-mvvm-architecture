package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class GetPendingModemRequest(
    @field:SerializedName("page_size")
    var page_size: Int = 0,
    @field:SerializedName("page_number")
    var page_number: Int? = null,

)