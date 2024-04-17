package com.rf.macgyver.data.model.request.incidentReportData

import java.io.Serializable

data class Step1IRData (
    var incidentNo : String? = null,
    var incidentDate : String? = null,
    var incidentTime: String? = null,
    var incidentLocation: String? = null,
    var vehicleNo: String? = null,
    var vehicleName: String? = null,
    var operatorName : String? = null,
    var typeOfIncident : ArrayList<String> = arrayListOf()
) :Serializable