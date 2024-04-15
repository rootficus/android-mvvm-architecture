package com.rf.macgyver.data.model.request

data class Step2DrData(
    var questionData : QuestionData? = null
)

data class QuestionData(
    var title : String? = null,
    var selectedAnswer : String? = null,
    var note : String? = null ,
    var uri : String? = null
)