package com.rf.geolgy.data.remote

import com.rf.geolgy.data.model.request.CompanyDetailsRequest
import com.rf.geolgy.data.model.request.CreateChallanRequest
import com.rf.geolgy.ui.base.BaseResponseModel
import com.rf.geolgy.data.model.request.SignInRequest
import com.rf.geolgy.data.model.response.CompanyDetailsResponse
import com.rf.geolgy.data.model.response.CreateChallanResponse
import com.rf.geolgy.data.model.response.SignInResponse
import retrofit2.Call
import retrofit2.http.*

interface GeolgyApiServices {


    @Headers("Content-Type:application/json")
    @POST("api/v1/companies/sign_in")
    fun signIn(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>
    @Headers("Content-Type:application/json")
    @POST("api/v1/companies/get_company_details")
    fun getCompanyDetails(@Body companyDetailsRequest: CompanyDetailsRequest): Call<BaseResponseModel<CompanyDetailsResponse>>
    @Headers("Content-Type:application/json")
    @POST("api/v1/companies/create_challan")
    fun createChallan(@Body createChallanRequest: CreateChallanRequest): Call<BaseResponseModel<CreateChallanResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/companies/logout")
    fun logout(): Call<BaseResponseModel<Any>>


}