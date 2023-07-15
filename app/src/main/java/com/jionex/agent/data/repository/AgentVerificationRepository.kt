package com.jionex.agent.data.repository

import android.content.Context
import com.jionex.agent.data.model.UserInfo
import com.jionex.agent.data.model.response.UserResponseResult
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.ui.base.BaseRepository
import com.jionex.agent.utils.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgentVerificationRepository (val apiServices: JionexApiServices,
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

    fun setUserId(userId: String?) {
        userId?.let { sharedPreference.setUserId(it) }
    }

    fun setEmail(email: String?) {
        email?.let { sharedPreference.setEmail(it) }
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
}