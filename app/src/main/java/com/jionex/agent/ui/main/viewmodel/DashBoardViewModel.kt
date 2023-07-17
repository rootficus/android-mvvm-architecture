package com.jionex.agent.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jionex.agent.data.model.UserInfo
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.request.VerifyPinRequest
import com.jionex.agent.data.model.response.SignInResponse
import com.jionex.agent.data.model.response.VerifyPinResponse
import com.jionex.agent.data.repository.DashBoardRepository
import com.jionex.agent.sdkInit.JionexSDK.sharedPreference
import com.jionex.agent.ui.base.BaseViewModel
import com.jionex.agent.utils.ResponseData
import com.jionex.agent.utils.setError
import com.jionex.agent.utils.setLoading
import com.jionex.agent.utils.setSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashBoardViewModel@Inject constructor(private val dashBoardRepository: DashBoardRepository) :
    BaseViewModel() {

    
    val signInResponseModel = MutableLiveData<ResponseData<SignInResponse>>()
    fun signInNow(signInRequest: SignInRequest) {
        signInResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.signInNow({ success -> signInResponseModel.setSuccess(success) },
                { error -> signInResponseModel.setError(error) },
                signInRequest,
                { message -> signInResponseModel.setError(message) })
        }
    }


    fun setUserId(userId: String?) {
        dashBoardRepository.setUserId(userId)
    }

    fun setEmail(email: String?) {
        dashBoardRepository.setEmail(email)
    }

    fun setFullName(full_name: String?) {
        dashBoardRepository.setFullName(full_name)
    }

    fun getFullName(): String? {
        return dashBoardRepository.getFullName()
    }

    fun setPinCode(pin_code: Int?) {
        dashBoardRepository.setPinCode(pin_code)
    }

    fun setCountry(country: String?) {
        dashBoardRepository.setCountry(country)
    }

    fun setParentId(parent_id: String?) {
        dashBoardRepository.setParentId(parent_id)
    }

    fun setPhoneNumber(phone: String?) {
        dashBoardRepository.setPhoneNumber(phone)
    }

    fun setUserName(user_name: String?) {
        dashBoardRepository.setUserName(user_name)
    }

    fun setUserRole(role_id: String?) {
        dashBoardRepository.setUserRole(role_id)
    }

    fun isLogin() : Boolean{
        return dashBoardRepository.isLogin()
    }

    fun setToken(token: String) {
        dashBoardRepository.setToken(token)
    }

    fun setPassword(password: String) {
        dashBoardRepository.setPassword(password)
    }

    fun getEmail(): String? {
        return dashBoardRepository.getEmail()
    }

    fun getPassword(): String? {
        return dashBoardRepository.getPassword()
    }

    fun setIsLogin(isLogin: Boolean) {
        dashBoardRepository.setIsLogin(isLogin)
    }

    fun getUserId(): String? {
        return dashBoardRepository.getUserId()
    }

    fun getPinCode(): Int? {
        return dashBoardRepository.getPinCode()
    }
}