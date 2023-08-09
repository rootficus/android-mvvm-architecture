package com.jionex.agent.data.repository

import android.content.Context
import com.jionex.agent.data.model.UserInfo
import com.jionex.agent.data.model.request.GetBalanceByFilterRequest
import com.jionex.agent.data.model.request.GetMessageByFilterRequest
import com.jionex.agent.data.model.request.GetModemsByFilterRequest
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.response.GetBalanceByFilterResponse
import com.jionex.agent.data.model.response.GetMessageByFilterResponse
import com.jionex.agent.data.model.response.GetModemsByFilterResponse
import com.jionex.agent.data.model.response.GetStatusCountResponse
import com.jionex.agent.data.model.response.SignInResponse
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.ui.base.BaseRepository
import com.jionex.agent.utils.SharedPreference

class DashBoardRepository (val apiServices: JionexApiServices,
                           val context: Context,
                           val sharedPreference: SharedPreference,
                           val jionexDatabase: JionexDatabase
) : BaseRepository() {


    fun checkOnVerificationAPI(
        success: (loginResponse: UserInfo) -> Unit,
        fail: (error: String) -> Unit,
        pinCode: String,
        message: (msg: String) -> Unit,
    ) {
        /*apiServices.verifyUserByPincode(pinCode).apply {
            execute(this, success, fail, context, message)
        }*/
    }

    fun signInNow(
        success: (signInResponse: SignInResponse) -> Unit,
        fail: (error: String) -> Unit,
        signInRequest: SignInRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.signInNow(signInRequest).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun getBalanceByFilter(
        success: (getBalanceByFilterResponse: List<GetBalanceByFilterResponse>) -> Unit,
        fail: (error: String) -> Unit,
        getBalanceByFilterRequest: GetBalanceByFilterRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.getBalanceByFilter("Bearer " + sharedPreference.getToken()).apply {
            executeFilter(this, success, fail, context, message)
        }
    }
    fun getMessageByFilter(
        success: (getMessageByFilterResponse: List<GetMessageByFilterResponse>) -> Unit,
        fail: (error: String) -> Unit,
        getMessageByFilterRequest: GetMessageByFilterRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.getMessageByFilter("Bearer " + sharedPreference.getToken()).apply {
            executeFilter(this, success, fail, context, message)
        }
    }
    fun getModemsByFilter(
        success: (getMessageByFilterResponse: List<GetModemsByFilterResponse>) -> Unit,
        fail: (error: String) -> Unit,
        getModemsByFilterRequest: GetModemsByFilterRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.getModemsByFilter("Bearer " + sharedPreference.getToken(), getModemsByFilterRequest).apply {
            executeFilter(this, success, fail, context, message)
        }
    }
    fun getStatusCount(
        success: (getStatusCountResponse: GetStatusCountResponse) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getStatusCount("Bearer " + sharedPreference.getToken()).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun setUserId(userId: String?) {
        userId?.let { sharedPreference.setUserId(it) }
    }

    fun getUserId(): String? {
        return sharedPreference.getUserId()
    }

    fun setEmail(email: String?) {
        email?.let { sharedPreference.setEmail(it) }
    }

    fun setPassword(password:String?){
        password?.let { sharedPreference.setPassword(password) }
    }

    fun getPassword(): String? {
        return sharedPreference.getPassword()
    }

    fun setFullName(full_name: String?) {
        full_name?.let { sharedPreference.setFullName(it) }
    }

    fun setPinCode(pin_code: Int?) {
        pin_code?.let { sharedPreference.setPinCode(it) }
    }

    fun setCountry(country: String?) {
        country?.let { sharedPreference.setCountry(it) }
    }

    fun setParentId(parent_id: String?) {
        parent_id?.let { sharedPreference.setParentId(it) }
    }

    fun setPhoneNumber(phone: String?) {
        phone?.let { sharedPreference.setPhoneNumber(it) }
    }

    fun setUserName(user_name: String?) {
        user_name?.let { sharedPreference.setUserName(it) }
    }

    fun setUserRole(role_id: String?) {
        role_id?.let { sharedPreference.setUserRole(it) }
    }

    fun isLogin(): Boolean {
        return sharedPreference.isLogin()
    }

    fun setIsLogin(isLogin: Boolean?) {
        isLogin?.let { sharedPreference.setIsLogin(it) }
    }

    fun setToken(token: String?) {
        token?.let { sharedPreference.setToken(it) }
    }

    fun getFullName(): String? {
        return sharedPreference.getFullName()
    }

    fun getEmail():String?{
        return sharedPreference.getEmail()
    }

    fun getPinCode(): Int? {
        return sharedPreference.getPinCode()
    }
}