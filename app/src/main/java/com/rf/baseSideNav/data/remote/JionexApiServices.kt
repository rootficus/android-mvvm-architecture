package com.rf.baseSideNav.data.remote

import com.rf.baseSideNav.ui.base.BaseResponseModel
import com.rf.baseSideNav.data.model.request.SignInRequest
import com.rf.baseSideNav.data.model.response.SignInResponse
import retrofit2.Call
import retrofit2.http.*

interface FionApiServices {


    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/login")
    fun signInNow(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>


}