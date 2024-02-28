package com.rf.utellRestaurant.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class StatusResponse(

    @SerializedName("status")
    @Expose
    var status: String?,

    ) : Serializable