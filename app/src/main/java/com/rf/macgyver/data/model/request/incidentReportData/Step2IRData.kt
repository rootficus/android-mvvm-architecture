package com.rf.macgyver.data.model.request.incidentReportData

import java.io.Serializable

data class Step2IRData (
    var incidentArea : String? = null,
    var incidentSeverity : String? = null,
    var weatherCondition : String? = null,
    var vehicleActivity : String? = null
) : Serializable