package com.jionex.agent.data.remote

import com.jionex.agent.data.model.request.MessagesJsonModel
import com.jionex.agent.data.model.request.ModemJsonModel
import com.jionex.agent.data.model.request.PinCodeJsonModel
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.request.VerifyPinRequest
import com.jionex.agent.data.model.response.SignInResponse
import com.jionex.agent.data.model.response.UserResponseResult
import com.jionex.agent.data.model.response.VerifyPinResponse
import com.jionex.agent.ui.base.BaseResponseModel
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


}