package com.rf.accessAli.data.remote

import com.rf.accessAli.roomDB.model.UniversityData
import retrofit2.Call
import retrofit2.http.*

interface AccessAliApiServices {

    @Headers("Content-Type:application/json")
    @GET("search?country=United%20Arab%20Emirates")
    fun getUniversityData(): Call<List<UniversityData>>

}