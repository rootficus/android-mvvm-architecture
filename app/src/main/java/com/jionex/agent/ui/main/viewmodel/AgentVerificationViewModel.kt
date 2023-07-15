package com.jionex.agent.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jionex.agent.data.model.UserInfo
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.request.VerifyPinRequest
import com.jionex.agent.data.model.response.SignInResponse
import com.jionex.agent.data.model.response.VerifyPinResponse
import com.jionex.agent.data.repository.AgentVerificationRepository
import com.jionex.agent.ui.base.BaseViewModel
import com.jionex.agent.utils.ResponseData
import com.jionex.agent.utils.setError
import com.jionex.agent.utils.setLoading
import com.jionex.agent.utils.setSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AgentVerificationViewModel@Inject constructor(private val agentVerificationRepository: AgentVerificationRepository) :
    BaseViewModel() {


    val agentVerificationResponseModel = MutableLiveData<ResponseData<UserInfo>>()

    fun checkOnVerificationAPI(pinCode: String) {
        agentVerificationResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            agentVerificationRepository.checkOnVerificationAPI(
                { success -> agentVerificationResponseModel.setSuccess(success) },
                { error -> agentVerificationResponseModel.setError(error) },
                pinCode,
                { message -> agentVerificationResponseModel.setError(message) }
            )
        }
    }

    val signInResponseModel = MutableLiveData<ResponseData<SignInResponse>>()
    fun signInNow(signInRequest: SignInRequest) {
        signInResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            agentVerificationRepository.signInNow({ success -> signInResponseModel.setSuccess(success) },
                { error -> signInResponseModel.setError(error) },
                signInRequest,
                { message -> signInResponseModel.setError(message) })
        }
    }
    val verifyUserByPinCodeResponseModel = MutableLiveData<ResponseData<VerifyPinResponse>>()
    fun verifyUserByPinCode(verifyPinRequest: VerifyPinRequest) {
        verifyUserByPinCodeResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            agentVerificationRepository.verifyUserByPinCode({ success -> verifyUserByPinCodeResponseModel.setSuccess(success) },
                { error -> verifyUserByPinCodeResponseModel.setError(error) },
                verifyPinRequest,
                { message -> verifyUserByPinCodeResponseModel.setError(message) })
        }
    }


    fun setUserId(userId: String?) {
        agentVerificationRepository.setUserId(userId)
    }

    fun setEmail(email: String?) {
        agentVerificationRepository.setEmail(email)
    }

    fun setFullName(full_name: String?) {
        agentVerificationRepository.setFullName(full_name)
    }

    fun setPinCode(pin_code: Int?) {
        agentVerificationRepository.setPinCode(pin_code)
    }

    fun setCountry(country: String?) {
        agentVerificationRepository.setCountry(country)
    }

    fun setParentId(parent_id: String?) {
        agentVerificationRepository.setParentId(parent_id)
    }

    fun setPhoneNumber(phone: String?) {
        agentVerificationRepository.setPhoneNumber(phone)
    }

    fun setUserName(user_name: String?) {
        agentVerificationRepository.setUserName(user_name)
    }

    fun setUserRole(role_id: String?) {
        agentVerificationRepository.setUserRole(role_id)
    }
}