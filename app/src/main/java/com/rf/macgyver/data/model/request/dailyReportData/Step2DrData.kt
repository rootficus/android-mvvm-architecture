package com.rf.macgyver.data.model.request.dailyReportData

import android.net.Uri
import java.io.Serializable

data class Step2DrData(
    var questionData : QuestionData? = null
) : Serializable

data class QuestionData(
    var title : String? = null,
    var selectedAnswer : String? = null,
    var note : String? = null ,
    var uri : ArrayList<Uri>? = arrayListOf()
) : Serializable