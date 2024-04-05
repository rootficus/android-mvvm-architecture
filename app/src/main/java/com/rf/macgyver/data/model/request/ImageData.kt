package com.rf.macgyver.data.model.request


data class Detailsdata(
    val name : String? = null,
    val phoneNo : Int? = null,
    val pictureUrl : String? = null,
    val address : String? = null
)

data class ImageData(
    val data: List<Data>? = null
)

data class Data(
    val price : Int? = null,
    val imageUrl : String? = null,
    var item: String? =null,
    var count :Int? = null,
    var time : String? = null
)
