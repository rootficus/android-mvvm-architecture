package com.rf.accessAli.data.remote

import com.rf.accessAli.data.model.request.CompanyDetailsRequest
import com.rf.accessAli.data.model.request.CreateChallanRequest
import com.rf.accessAli.ui.base.BaseResponseModel
import com.rf.accessAli.data.model.request.SignInRequest
import com.rf.accessAli.data.model.response.CompanyDetailsResponse
import com.rf.accessAli.data.model.response.CreateChallanResponse
import com.rf.accessAli.data.model.response.SignInResponse
import retrofit2.Call
import retrofit2.http.*

interface AccessAliApiServices {


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