package com.rf.utellRestaurant.data.remote

import com.rf.utellRestaurant.ui.base.BaseResponseModel
import com.rf.utellRestaurant.data.model.request.SignInRequest
import com.rf.utellRestaurant.data.model.response.SignInResponse
import com.rf.utellRestaurant.data.model.response.StatusResponse
import retrofit2.Call
import retrofit2.http.*

interface FionApiServices {


    @Headers("Content-Type:application/json")
    @POST("api/v1/users/sign_in")
    fun signInNow(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>
    @Headers("Content-Type:application/json")
    @POST("api/v1/vendors/get_status")
    fun getVendorStatus(): Call<BaseResponseModel<StatusResponse>>
    @Headers("Content-Type:application/json")
    @POST("api/v1/vendors/change_status")
    fun setVendorStatus(@Body statusResponse: StatusResponse): Call<BaseResponseModel<StatusResponse>>


}