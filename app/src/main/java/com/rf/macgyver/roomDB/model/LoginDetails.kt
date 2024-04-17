package com.rf.macgyver.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Keep
@Entity(tableName = "loginDetails")
data class LoginDetails(
    @PrimaryKey
    var emailId: String,
    var uniqueToken :  String?,
    var password: String? = null,
    var username: String? = null,
    var noOfVehicles: String? = null,
    var userRole: String? = null,
    var companyIndustry: String? = null,
    var motiveHVI: ArrayList<String> = arrayListOf()
)