package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class GetSupportRequest(
    @field:SerializedName("page_size")
    var pageSize: Int = 0,
    @field:SerializedName("page_number")
    var pageNumber: Int? = null,
)