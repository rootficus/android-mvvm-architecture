package com.fionpay.agent.data.repository

import android.content.Context
import com.fionpay.agent.data.model.request.SignInRequest
import com.fionpay.agent.data.model.request.VerifyPinRequest
import com.fionpay.agent.data.model.response.SignInResponse
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.ui.base.BaseRepository
import com.fionpay.agent.utils.SharedPreference

class SignInRepository (val apiServices: FionApiServices,
                        val context: Context,
                        val sharedPreference: SharedPreference,
                        val fionDatabase: FionDatabase
) : BaseRepository() {

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
    fun verifyUserByPinCode(
        success: (verifyPinResponse: Any) -> Unit,
        fail: (error: String) -> Unit,
        verifyPinRequest: VerifyPinRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.verifyUserByPinCode("Bearer " + sharedPreference.getToken(),verifyPinRequest).apply {
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