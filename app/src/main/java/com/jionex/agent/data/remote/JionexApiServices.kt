package com.jionex.agent.data.remote

import com.jionex.agent.data.model.request.GetBalanceByFilterRequest
import com.jionex.agent.data.model.request.GetMessageByFilterRequest
import com.jionex.agent.data.model.request.GetModemsByFilterRequest
import com.jionex.agent.data.model.request.MessagesJsonModel
import com.jionex.agent.data.model.request.ModemJsonModel
import com.jionex.agent.data.model.request.PinCodeJsonModel
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.request.VerifyPinRequest
import com.jionex.agent.data.model.response.GetBalanceByFilterResponse
import com.jionex.agent.data.model.response.GetMessageByFilterResponse
import com.jionex.agent.data.model.response.GetModemsByFilterResponse
import com.jionex.agent.data.model.response.GetStatusCountResponse
import com.jionex.agent.data.model.response.SignInResponse
import com.jionex.agent.data.model.response.UserResponseResult
import com.jionex.agent.ui.base.BaseResponseModel
import com.jionex.agent.ui.base.BaseResponseModelFilter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface JionexApiServices {

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
    @POST("api/v1/balance_manager/get_balance_by_filter")
    fun getBalanceByFilter(
        @Header("Authorization") authHeader: String?,
        @Body getBalanceByFilterRequest: GetBalanceByFilterRequest
    ): Call<BaseResponseModelFilter<GetBalanceByFilterResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/messages/get_messages_by_filter")
    fun getMessageByFilter(
        @Header("Authorization") authHeader: String?,
        @Body getMessageByFilterRequest: GetMessageByFilterRequest
    ): Call<BaseResponseModelFilter<GetMessageByFilterResponse>>
    @Headers("Content-Type:application/json")
    @POST("api/v1/modems/get_modems_by_filter")
    fun getModemsByFilter(
        @Header("Authorization") authHeader: String?,
        @Body getModemsByFilterRequest: GetModemsByFilterRequest
    ): Call<BaseResponseModelFilter<GetModemsByFilterResponse>>
    @Headers("Content-Type:application/json")
    @GET("/api/v1/balance_manager/daily_status_count")
    fun getStatusCount(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModel<GetStatusCountResponse>>

}