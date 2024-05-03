package com.rf.accessAli.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rf.accessAli.data.model.request.SignInRequest
import com.rf.accessAli.data.model.response.SignInResponse
import com.rf.accessAli.data.repository.SignInRepository
import com.rf.accessAli.ui.base.BaseViewModel
import com.rf.accessAli.utils.ResponseData
import com.rf.accessAli.utils.setError
import com.rf.accessAli.utils.setLoading
import com.rf.accessAli.utils.setSuccess
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


    fun setFullName(fullName: String?) {
        signInRepository.setFullName(fullName)
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

    fun setSignInDataModel(model: String?) {
        signInRepository.setSignInDataModel(model)
    }

    fun setIsLogin(isLogin: Boolean) {
        signInRepository.setIsLogin(isLogin)
    }

}