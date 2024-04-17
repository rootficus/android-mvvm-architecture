package com.rf.macgyver.data.model.request.inspectionFormData

import java.io.Serializable

data class Step1IPData(
    val ipQuestionData : IPQuestionData
) :Serializable
data class IPQuestionData(
var title : String? = null,
var selectedAnswer : String? = null,
var note : String? = null ,
var uri : String? = null
) : Serializable
