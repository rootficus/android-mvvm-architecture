package com.rf.macgyver.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rf.macgyver.data.model.request.SignInRequest
import com.rf.macgyver.data.model.response.SignInResponse
import com.rf.macgyver.data.repository.SignInRepository
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.ui.base.BaseViewModel
import com.rf.macgyver.utils.ResponseData
import com.rf.macgyver.utils.setError
import com.rf.macgyver.utils.setLoading
import com.rf.macgyver.utils.setSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val signInRepository: SignInRepository) :
    BaseViewModel() {

    private val signInResponseModel = MutableLiveData<ResponseData<SignInResponse>>()


    fun signInNow(signInRequest: SignInRequest) {
        signInResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            signInRepository.signInNow({ success -> signInResponseModel.setSuccess(success) },
                { error -> signInResponseModel.setError(error) },
                signInRequest,
                { message -> signInResponseModel.setError(message) })
        }
    }

    fun getLoginDetails(email: String): LoginDetails?{
        return signInRepository.getLoginDetails(email )
    }

    fun updateCompanyInfo(id :String? , value1: String?, value2: String?, value3 : String?){
        signInRepository.updateCompanyInfo(id,value1,value2,value3)
    }

    fun updateMotiveHVI(id :String? , value1: ArrayList<String>){
        signInRepository.updateMotiveHVI(id,value1)
    }


    fun insertLoginDetails(loginDetails: LoginDetails){
        signInRepository.insertLoginDetails(loginDetails)
    }

    fun updateLoginDetails(loginDetails: LoginDetails){
        signInRepository.updateLoginDetails(loginDetails)
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