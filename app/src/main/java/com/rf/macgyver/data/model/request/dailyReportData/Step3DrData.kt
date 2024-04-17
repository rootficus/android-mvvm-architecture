package com.rf.macgyver.data.model.request.dailyReportData

import java.io.Serializable

data class Step3DrData(
    var vehicleDowntime : String? = null,
    var downtimeNote : String?=null ,
    var vehicleRuntime : String? = null,
    var worklog : String? = null
) : Serializable
