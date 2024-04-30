package com.rf.macgyver.data.repository

import android.content.Context
import com.rf.macgyver.data.model.request.SignInRequest
import com.rf.macgyver.data.model.response.SignInResponse
import com.rf.macgyver.data.remote.MagApiServices
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.ui.base.BaseRepository
import com.rf.macgyver.utils.SharedPreference

class SignInRepository(
    val apiServices: MagApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val magDatabase: MagDatabase
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

    fun insertLoginDetails(loginDetails: LoginDetails)
    {
        magDatabase.magDao()?.insertLoginDetails(loginDetails)
    }
    fun updateLoginDetails(loginDetails: LoginDetails)
    {
        magDatabase.magDao()?.updateLoginDetails(loginDetails)
    }

    fun updateCompanyInfo(id :String? , value1: String?, value2: String?, value3 : String?){
        magDatabase.magDao()?.updateCompanyInfo(id,value1,value2,value3)
    }

    fun updateMotiveHVI(id :String? , value1: ArrayList<String>){
        magDatabase.magDao()?.updateMotiveHVI(id,value1)
    }

    fun getLoginDetails(email: String): LoginDetails?{
        return magDatabase.magDao()?.getLoginDetails(email )
    }

    fun getLoginDetails(): LoginDetails?{
        return magDatabase.magDao()?.getLoginDetails( )
    }
}