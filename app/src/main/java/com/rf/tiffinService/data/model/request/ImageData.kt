package com.rf.tiffinService.data.model.request


data class ImageData(
    val data: List<Data>? = null
)

data class Data(
    var item: String? =null,
    var count :Int? = null
)
