package com.rf.geolgy.data.repository

import android.content.Context
import com.rf.geolgy.data.model.request.SignInRequest
import com.rf.geolgy.data.model.response.SignInResponse
import com.rf.geolgy.data.remote.GeolgyApiServices
import com.rf.geolgy.roomDB.GeolgyDatabase
import com.rf.geolgy.ui.base.BaseRepository
import com.rf.geolgy.utils.SharedPreference

class SignInRepository(
    val apiServices: GeolgyApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val geolgyDatabase: GeolgyDatabase
) : BaseRepository() {

    fun signInNow(
        success: (signInResponse: SignInResponse) -> Unit,
        fail: (error: String) -> Unit,
        signInRequest: SignInRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.signIn(signInRequest).apply {
            execute(this, success, fail, context, message)
        }
    }




    fun setUserId(userId: String?) {
        userId?.let { sharedPreference.setUserId(it) }
    }


    fun setPassword(password: String?) {
        password?.let { sharedPreference.setPassword(password) }
    }

    fun getPassword(): String? {
        return sharedPreference.getPassword()
    }
    fun setSignInDataModel(model: String?) {
        model?.let { sharedPreference.setSignInDataModel(model) }
    }

    fun getSignInDataModel(): String? {
        return sharedPreference.getSignInDataModel()
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
}