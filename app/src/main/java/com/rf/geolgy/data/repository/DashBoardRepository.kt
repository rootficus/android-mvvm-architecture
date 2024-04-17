package com.rf.geolgy.data.repository

import android.content.Context
import com.rf.geolgy.data.model.request.CompanyDetailsRequest
import com.rf.geolgy.data.model.request.CreateChallanRequest
import com.rf.geolgy.data.model.response.CompanyDetailsResponse
import com.rf.geolgy.data.model.response.CreateChallanResponse
import com.rf.geolgy.data.remote.GeolgyApiServices
import com.rf.geolgy.roomDB.GeolgyDatabase
import com.rf.geolgy.ui.base.BaseRepository
import com.rf.geolgy.utils.Constant
import com.rf.geolgy.utils.SharedPreference

class DashBoardRepository(
    val apiServices: GeolgyApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val geolgyDatabase: GeolgyDatabase
) : BaseRepository() {

    fun getCompanyDetails(
        success: (companyDetailsResponse: CompanyDetailsResponse) -> Unit,
        companyDetailsRequest: CompanyDetailsRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getCompanyDetails(companyDetailsRequest).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun createChallan(
        success: (createChallanResponse: CreateChallanResponse) -> Unit,
        createChallanRequest: CreateChallanRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.createChallan(createChallanRequest).apply {
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

    fun setEditText6(text: String?) {
        text?.let { sharedPreference.setEditText6(text) }
    }

    fun getEditText6(): String? {
        return sharedPreference.getEditText6()
    }

    fun setEditText7(text: String?) {
        text?.let { sharedPreference.setEditText7(text) }
    }

    fun getEditText7(): String? {
        return sharedPreference.getEditText7()
    }

    fun setEditText8(text: Int?) {
        text?.let { sharedPreference.setEditText8(text) }
    }

    fun getEditText8(): Int? {
        return sharedPreference.getEditText8()
    }

    fun setEditText10(text: String?) {
        text?.let { sharedPreference.setEditText10(text) }
    }

    fun getEditText10(): String? {
        return sharedPreference.getEditText10()
    }

    fun setEdit2Text10(text: String?) {
        text?.let { sharedPreference.setEdit2Text10(text) }
    }

    fun getEdit2Text10(): String? {
        return sharedPreference.getEdit2Text10()
    }

    fun setEditText14(text: String?) {
        text?.let { sharedPreference.setEditText14(text) }
    }

    fun getEditText14(): String? {
        return sharedPreference.getEditText14()
    }
    fun setExpireHour(hour: Int?) {
        hour?.let { sharedPreference.setExpireHour(hour) }
    }

    fun getExpireHour(): Int? {
        return sharedPreference.getExpireHour()
    }

}