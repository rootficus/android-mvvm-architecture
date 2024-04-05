package com.rf.macgyver.data.remote

import com.rf.macgyver.ui.base.BaseResponseModel
import com.rf.macgyver.data.model.request.SignInRequest
import com.rf.macgyver.data.model.response.SignInResponse
import retrofit2.Call
import retrofit2.http.*

interface FionApiServices {


    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/login")
    fun signInNow(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>


}