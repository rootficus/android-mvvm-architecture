package com.rf.tiffinService.data.repository

import android.content.Context
import com.rf.tiffinService.data.model.request.SignInRequest
import com.rf.tiffinService.data.model.response.SignInResponse
import com.rf.tiffinService.data.remote.FionApiServices
import com.rf.tiffinService.roomDB.FionDatabase
import com.rf.tiffinService.ui.base.BaseRepository
import com.rf.tiffinService.utils.SharedPreference

class SignInRepository(
    val apiServices: FionApiServices,
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




    fun setUserId(userId: String?) {
        userId?.let { sharedPreference.setUserId(it) }
    }

    fun setEmail(email: String?) {
        email?.let { sharedPreference.setEmail(it) }
    }

    fun setPassword(password: String?) {
        password?.let { sharedPreference.setPassword(password) }
    }

    fun getPassword(): String? {
        return sharedPreference.getPassword()
    }

    fun setFullName(fullName: String?) {
        fullName?.let { sharedPreference.setFullName(it) }
    }

    fun setPinCode(pinCode: String?) {
        pinCode?.let { sharedPreference.setPinCode(it) }
    }

    fun setPhoneNumber(phone: String?) {
        phone?.let { sharedPreference.setPhoneNumber(it) }
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

    fun getEmail(): String? {
        return sharedPreference.getEmail()
    }
}