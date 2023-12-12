package com.fionpay.agent.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fionpay.agent.data.model.request.SignInRequest
import com.fionpay.agent.data.model.request.VerifyPinRequest
import com.fionpay.agent.data.model.response.SignInResponse
import com.fionpay.agent.data.repository.SignInRepository
import com.fionpay.agent.ui.base.BaseViewModel
import com.fionpay.agent.utils.ResponseData
import com.fionpay.agent.utils.setError
import com.fionpay.agent.utils.setLoading
import com.fionpay.agent.utils.setSuccess
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

    val verifyUserByPinCodeResponseModel = MutableLiveData<ResponseData<Any>>()
    fun verifyUserByPinCode(verifyPinRequest: VerifyPinRequest) {
        verifyUserByPinCodeResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            signInRepository.verifyUserByPinCode({ success ->
                verifyUserByPinCodeResponseModel.setSuccess(
                    success
                )
            },
                { error -> verifyUserByPinCodeResponseModel.setError(error) },
                verifyPinRequest,
                { message -> verifyUserByPinCodeResponseModel.setError(message) })
        }
    }


    fun setUserId(userId: String?) {
        signInRepository.setUserId(userId)
    }

    fun setEmail(email: String?) {
        signInRepository.setEmail(email)
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

    fun getEmail(): String? {
        return signInRepository.getEmail()
    }

    fun getPassword(): String? {
        return signInRepository.getPassword()
    }

    fun setIsLogin(isLogin: Boolean) {
        signInRepository.setIsLogin(isLogin)
    }

}