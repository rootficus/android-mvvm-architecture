package com.fionpay.agent.data.remote

import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.AddModemSlotsModel
import com.fionpay.agent.data.model.request.CheckNumberAvailabilityRequest
import com.fionpay.agent.data.model.request.FilterResponse
import com.fionpay.agent.data.model.request.GetAgentB2BRequest
import com.fionpay.agent.data.model.request.GetPendingModemRequest
import com.fionpay.agent.data.model.request.MessagesJsonModel
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.request.ModemJsonModel
import com.fionpay.agent.data.model.request.PinCodeJsonModel
import com.fionpay.agent.data.model.request.ProfileResponse
import com.fionpay.agent.data.model.request.ReturnBalanceRequest
import com.fionpay.agent.data.model.request.SignInRequest
import com.fionpay.agent.data.model.request.TransactionFilterRequest
import com.fionpay.agent.data.model.request.UpdateActiveInActiveRequest
import com.fionpay.agent.data.model.request.UpdateAvailabilityRequest
import com.fionpay.agent.data.model.request.UpdateBalanceRequest
import com.fionpay.agent.data.model.request.UpdateLoginRequest
import com.fionpay.agent.data.model.request.VerifyPinRequest
import com.fionpay.agent.data.model.response.B2BResponse
import com.fionpay.agent.data.model.response.BLTransactionModemResponse
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.GetAddModemBalanceResponse
import com.fionpay.agent.data.model.response.GetAddModemResponse
import com.fionpay.agent.data.model.response.GetModemSlotsResponse
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.data.model.response.GetStatusCountResponse
import com.fionpay.agent.data.model.response.ModemPinCodeResponse
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.data.model.response.ReturnBalanceResponse
import com.fionpay.agent.data.model.response.SignInResponse
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.data.model.response.UserResponseResult
import com.fionpay.agent.ui.base.BaseResponseModel
import com.fionpay.agent.ui.base.BaseResponseModel2
import com.fionpay.agent.ui.base.BaseResponseModelFilter
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    @POST("api/v1/app/agents/login")
    fun signInNow(@Body signInRequest: SignInRequest): Call<BaseResponseModel<SignInResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/verify_agent_by_pincode")
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
    @GET("api/v1/app/agents/modems")
    fun getModemsList(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModelFilter<GetModemsListResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/add_modem")
    fun addModemItem(
        @Header("Authorization") authHeader: String?,
        @Body modemItemModel: ModemItemModel
    ): Call<BaseResponseModel<GetAddModemResponse>>



    @Headers("Content-Type:application/json")
    @POST("/api/v1/app/agents/add_modem_slots")
    fun addModemSlots(
        @Header("Authorization") authHeader: String?,
        @Body addModemSlotsModel: AddModemSlotsModel
    ): Call<BaseResponseModel2<GetModemSlotsResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/add_modem_balance")
    fun addModemBalance(
        @Header("Authorization") authHeader: String?,
        @Body addModemBalanceModel: AddModemBalanceModel
    ): Call<BaseResponseModel<GetAddModemBalanceResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/remove_modem_balance")
    fun removeModemBalance(
        @Header("Authorization") authHeader: String?,
        @Body addModemBalanceModel: AddModemBalanceModel
    ): Call<BaseResponseModel<GetAddModemBalanceResponse>>


    @Headers("Content-Type:application/json")
    @POST("api/v1/balance_manager/update_status")
    fun updateBLStatus(
        @Header("Authorization") authHeader: String?,
        @Body updateBalanceRequest: UpdateBalanceRequest
    ): Call<BaseResponseModel<GetBalanceManageRecord>>

    @Headers("Content-Type:application/json")
    @GET("/api/v1/balance_manager/daily_status_count")
    fun getStatusCount(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModel<GetStatusCountResponse>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/app/agents/dashboard")
    fun dashboardData(): Call<BaseResponseModel<DashBoardItemResponse>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/app/agents/bl_transactions")
    fun getBlTransactionsData(): Call<BaseResponseModel2<BLTransactionModemResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/approved_deposit_requests")
    fun getTransactionsData(
        @Header("Authorization") authHeader: String?,
        @Body transactionFilterRequest: TransactionFilterRequest?
    ): Call<BaseResponseModel2<TransactionModemResponse>>

    @Headers("Content-Type:application/json")
    @PUT("api/v1/app/agents/update_active_inactive_status")
    fun updateActiveInActiveStatus(
        @Header("Authorization") authHeader: String?,
        @Body updateActiveInActiveRequest: UpdateActiveInActiveRequest
    ): Call<BaseResponseModel<Any>>

    @Headers("Content-Type:application/json")
    @PUT("api/v1/app/agents/update_availability_status")
    fun updateAvailabilityStatus(
        @Header("Authorization") authHeader: String?,
        @Body updateAvailabilityRequest: UpdateAvailabilityRequest
    ): Call<BaseResponseModel<Any>>

    @Headers("Content-Type:application/json")
    @PUT("api/v1/app/agents/update_login_status")
    fun updateLoginStatus(
        @Header("Authorization") authHeader: String?,
        @Body updateLoginRequest: UpdateLoginRequest
    ): Call<BaseResponseModel<Any>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/pending_deposit_requests")
    fun getPendingRequest(
        @Header("Authorization") authHeader: String?,
        @Body pendingModemRequest: GetPendingModemRequest
    ): Call<BaseResponseModel2<PendingModemResponse>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/app/agents/generate_pincode")
    fun generatePinCode(
        @Header("Authorization") authHeader: String?
    ): Call<BaseResponseModel<Any>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/app/agents/{agentId}")
    fun getAgentProfile(
        @Header("Authorization") authHeader: String?,
        @Path("agentId") type: String,
    ): Call<BaseResponseModel<ProfileResponse>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/app/agents/transaction_filters")
    fun getTransactionFilters(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModel<FilterResponse>>

    @PUT("api/v1/app/agents/{agentId}")
    @Multipart
    fun updateAgentProfile(
        @Header("Authorization") authHeader: String?,
        @Part("full_name") fullName: RequestBody?,
        @Part filePart: MultipartBody.Part,
        @Path("agentId") type: String,
    ): Call<BaseResponseModel<ProfileResponse>>

    @PUT("api/v1/app/agents/{agentId}")
    @Multipart
    fun updateAgentWithoutProfile(
        @Header("Authorization") authHeader: String?,
        @Part("full_name") fullName: RequestBody?,
        @Path("agentId") type: String,
    ): Call<BaseResponseModel<ProfileResponse>>

    @POST("api/v1/app/agents/approved_deposit_requests")
    fun transactionFilterApi(
        @Header("Authorization") authHeader: String?,
        @Body transactionFilterRequest: TransactionFilterRequest?,
    ): Call<BaseResponseModel2<TransactionModemResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/check_number_bank_availability")
    fun checkNumberBankAvailability(
        @Header("Authorization") authHeader: String?,
        @Body checkNumberAvailabilityRequest: CheckNumberAvailabilityRequest,
    ): Call<BaseResponseModel<Any>>

    @Headers("Content-Type:application/json")
    @POST("/api/imagesUpload")   //imageVideoUpload
    @Multipart
    fun imageVideoUpload(
        @Header("Authorization") authHeader: String?,
        @Part("type") type: RequestBody,
        @Part("file_type") fileType: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("product_id") productId: RequestBody,
        @Part filePart: MultipartBody.Part
    ): Call<BaseResponseModel<Any>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/get_agent_b2b")
    fun getB2BRecord(
        @Header("Authorization") authHeader: String?,
        @Body getAgentB2BRequest: GetAgentB2BRequest,
    ): Call<BaseResponseModel2<B2BResponse>>

    @Headers("Content-Type:application/json")
    @GET("api/v1/app/agents/modems_pincodes")
    fun getModemPinCodes(
        @Header("Authorization") authHeader: String?,
    ): Call<BaseResponseModel2<ModemPinCodeResponse>>

    @Headers("Content-Type:application/json")
    @POST("api/v1/app/agents/return_balance_to_dist")
    fun returnBalanceToDistributor(
        @Header("Authorization") authHeader: String?,
        @Body returnBalanceRequest: ReturnBalanceRequest,
    ): Call<BaseResponseModel<ReturnBalanceResponse>>
}