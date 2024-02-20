package com.rf.utellRestaurant.data.remote

import com.rf.utellRestaurant.ui.base.BaseResponseModel
import com.rf.utellRestaurant.data.model.request.SignInRequest
import com.rf.utellRestaurant.data.model.response.SignInResponse
import retrofit2.Call
import retrofit2.http.*

interface FionApiServices {


    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/login")
    fun signInNow(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>


}