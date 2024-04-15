package com.rf.macgyver.data.model.request

import java.io.Serializable

data class Step1DrData (
    var date : String? = null,
    var day : String? = null,
    var reportName: String? = null,
    var name : String? = null,
    var shift : String? = null,
    var vehicle :Vehicle? = null
) :Serializable

data class Vehicle(
    var vehicleNo : String? = null,
    var vehicleName : String? = null
): Serializable
