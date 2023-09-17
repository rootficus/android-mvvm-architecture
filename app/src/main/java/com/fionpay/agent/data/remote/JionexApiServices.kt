package com.fionpay.agent.data.remote

import com.fionpay.agent.data.model.request.GetModemsByFilterRequest
import com.fionpay.agent.data.model.request.MessagesJsonModel
import com.fionpay.agent.data.model.request.ModemJsonModel
import com.fionpay.agent.data.model.request.PinCodeJsonModel
import com.fionpay.agent.data.model.request.SignInRequest
import com.fionpay.agent.data.model.request.UpdateBalanceRequest
import com.fionpay.agent.data.model.request.VerifyPinRequest
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsByFilterResponse
import com.fionpay.agent.data.model.response.GetStatusCountResponse
import com.fionpay.agent.data.model.response.SignInResponse
import com.fionpay.agent.data.model.response.UserResponseResult
import com.fionpay.agent.ui.base.BaseResponseModel
import com.fionpay.agent.ui.base.BaseResponseModelFilter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface FionApiServices {

    @Headers("Content-Type:application/json")
    @POST("api/v1/agents/insert_multiple_messages")
    fun InsertOrUpdateMultiMessage(@Body jsonObject: MessagesJsonModel): Call<JSONObject>

    @Headers("Content-Type:application/json")
    @POST("api/v1/agents/verify_agent_by_pincode")
    fun verifyUserByPincode(@Body pinCode: PinCodeJsonModel): Call<UserResponseResult>

    @Headers("Content-Type:application/json")
    @POST("api/v1/agents/insert_or_update_modem")
    fun InsertOrUpdateModem(@Body jsonObject: ModemJsonModel): Call<JSONObject>

    @Headers("Content-Type:application/json")
    @POST("api/v1/users/sign_in")
    fun signInNow(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/users/verify_user_by_pincode")
    fun verifyUserByPinCode(
        @Header("Authorization") authHeader: String?,
        @Body verifyPinRequest: VerifyPinRequest
    ): Call<BaseResponseModel<Any>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/balance_manager/todays_data")
    fun getBalanceByFilter(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModelFilter<GetBalanceManageRecord>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/messages/todays_data")
    fun getMessageByFilter(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModelFilter<GetMessageManageRecord>>
    @Headers("Content-Type:application/json")
    @POST("api/v1/modems/get_modems_by_filter")
    fun getModemsByFilter(
        @Header("Authorization") authHeader: String?,
        @Body getModemsByFilterRequest: GetModemsByFilterRequest
    ): Call<BaseResponseModelFilter<GetModemsByFilterResponse>>


    @Headers("Content-Type:application/json")
    @POST("api/v1/balance_manager/update_status")
    fun updateBLStatus(@Header("Authorization") authHeader: String?,@Body updateBalanceRequest: UpdateBalanceRequest): Call<BaseResponseModel<GetBalanceManageRecord>>

    @Headers("Content-Type:application/json")
    @GET("/api/v1/balance_manager/daily_status_count")
    fun getStatusCount(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModel<GetStatusCountResponse>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/users/dashboard_data")
    fun dashboardData(): Call<BaseResponseModel<DashBoardItemResponse>>



}