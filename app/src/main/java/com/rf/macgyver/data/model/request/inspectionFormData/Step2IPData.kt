package com.rf.macgyver.data.model.request.inspectionFormData

data class Step2IPData (
    var faultyItems :String? = null,
    var overallCond : String? = null,
    var vehicleStatus : String? = null,
    var priority : String? = null,
    var isSafeToUse : String? = null,
    var isDeployed :String? = null,
    var additionalNote : String? = null
)