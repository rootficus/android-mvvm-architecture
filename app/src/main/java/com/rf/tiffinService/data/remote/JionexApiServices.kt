package com.rf.tiffinService.data.remote

import com.rf.tiffinService.ui.base.BaseResponseModel
import com.rf.tiffinService.data.model.request.SignInRequest
import com.rf.tiffinService.data.model.response.SignInResponse
import retrofit2.Call
import retrofit2.http.*

interface FionApiServices {


    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/login")
    fun signInNow(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>


}