package com.rf.geolgy.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rf.geolgy.data.model.request.SignInRequest
import com.rf.geolgy.data.model.response.SignInResponse
import com.rf.geolgy.data.repository.SignInRepository
import com.rf.geolgy.ui.base.BaseViewModel
import com.rf.geolgy.utils.ResponseData
import com.rf.geolgy.utils.setError
import com.rf.geolgy.utils.setLoading
import com.rf.geolgy.utils.setSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val signInRepository: SignInRepository) :
    BaseViewModel() {

    val signInResponseModel = MutableLiveData<ResponseData<SignInResponse>>()
    fun signInNow(signInRequest: SignInRequest) {
        signInResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            signInRepository.signInNow({ success -> signInResponseModel.setSuccess(success) },
                { error -> signInResponseModel.setError(error) },
                signInRequest,
                { message -> signInResponseModel.setError(message) })
        }
    }


    fun setUserId(userId: String?) {
        signInRepository.setUserId(userId)
    }

    fun setFullName(fullName: String?) {
        signInRepository.setFullName(fullName)
    }


    fun setPinCode(pinCode: String?) {
        signInRepository.setPinCode(pinCode)
    }

    fun setPhoneNumber(phone: String?) {
        signInRepository.setPhoneNumber(phone)
    }

    fun isLogin(): Boolean {
        return signInRepository.isLogin()
    }
    fun setToken(token: String) {
        signInRepository.setToken(token)
    }

    fun setPassword(password: String) {
        signInRepository.setPassword(password)
    }

    fun getPassword(): String? {
        return signInRepository.getPassword()
    }
    fun setSignInDataModel(model: String?) {
        signInRepository.setSignInDataModel(model)
    }

    fun getSignInDataModel(): String? {
        return signInRepository.getSignInDataModel()
    }

    fun setIsLogin(isLogin: Boolean) {
        signInRepository.setIsLogin(isLogin)
    }

}