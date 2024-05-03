package com.rf.accessAli.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(tableName = "UniversityData")
data class UniversityData(
    @SerializedName("alpha_two_code") var alphaTwoCode: String? = null,
    @PrimaryKey
    @SerializedName("name") var name: String,
    @SerializedName("country") var country: String? = null,
    @SerializedName("domain") var domain: ArrayList<String>? = null,
    @SerializedName("webpages") var webpages: ArrayList<String>? =null,
    @SerializedName("state-province") var stateProvince: String? = null
) : Serializable
