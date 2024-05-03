package com.rf.accessAli.data.repository

import android.content.Context
import com.rf.accessAli.data.model.request.SignInRequest
import com.rf.accessAli.data.model.response.SignInResponse
import com.rf.accessAli.data.remote.AccessAliApiServices
import com.rf.accessAli.roomDB.AccessAliDatabase
import com.rf.accessAli.ui.base.BaseRepository
import com.rf.accessAli.utils.SharedPreference

class SignInRepository(
    val apiServices: AccessAliApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val accessAliDatabase: AccessAliDatabase
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


    fun setPassword(password: String?) {
        password?.let { sharedPreference.setPassword(password) }
    }

    fun setSignInDataModel(model: String?) {
        model?.let { sharedPreference.setSignInDataModel(model) }
    }


    fun setFullName(fullName: String?) {
        fullName?.let { sharedPreference.setFullName(it) }
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