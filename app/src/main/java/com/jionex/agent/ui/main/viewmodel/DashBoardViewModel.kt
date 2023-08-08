package com.jionex.agent.ui.main.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jionex.agent.data.model.request.GetBalanceByFilterRequest
import com.jionex.agent.data.model.request.GetMessageByFilterRequest
import com.jionex.agent.data.model.request.GetModemsByFilterRequest
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.response.GetBalanceByFilterResponse
import com.jionex.agent.data.model.response.GetMessageByFilterResponse
import com.jionex.agent.data.model.response.GetModemsByFilterResponse
import com.jionex.agent.data.model.response.GetStatusCountResponse
import com.jionex.agent.data.model.response.SignInResponse
import com.jionex.agent.data.repository.DashBoardRepository
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


    enum class DarkModeConfig {
        YES,
        NO
    }
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

    val getBalanceByFilterResponseModel = MutableLiveData<ResponseData<List<GetBalanceByFilterResponse>>>()
    fun getBalanceByFilter(getBalanceByFilterRequest: GetBalanceByFilterRequest) {
        getBalanceByFilterResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getBalanceByFilter({ success -> getBalanceByFilterResponseModel.setSuccess(success) },
                { error -> getBalanceByFilterResponseModel.setError(error) },
                getBalanceByFilterRequest,
                { message -> getBalanceByFilterResponseModel.setError(message) })
        }
    }
    val getMessageByFilterResponseModel = MutableLiveData<ResponseData<List<GetMessageByFilterResponse>>>()
    fun getMessageByFilter(getMessageByFilterRequest: GetMessageByFilterRequest) {
        getMessageByFilterResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getMessageByFilter({ success -> getMessageByFilterResponseModel.setSuccess(success) },
                { error -> getMessageByFilterResponseModel.setError(error) },
                getMessageByFilterRequest,
                { message -> getMessageByFilterResponseModel.setError(message) })
        }
    }

    val getModemsByFilterResponseModel = MutableLiveData<ResponseData<List<GetModemsByFilterResponse>>>()
    fun getModemsByFilter(getModemsByFilterRequest: GetModemsByFilterRequest) {
        getModemsByFilterResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getModemsByFilter({ success -> getModemsByFilterResponseModel.setSuccess(success) },
                { error -> getModemsByFilterResponseModel.setError(error) },
                getModemsByFilterRequest,
                { message -> getModemsByFilterResponseModel.setError(message) })
        }
    }

    val getStatusCountResponseModel = MutableLiveData<ResponseData<GetStatusCountResponse>>()
    fun getStatusCount() {
        getStatusCountResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getStatusCount({ success -> getStatusCountResponseModel.setSuccess(success) },
                { error -> getStatusCountResponseModel.setError(error) },
                { message -> getStatusCountResponseModel.setError(message) })
        }
    }

    fun checkNightTheme(mode: Boolean)
    {
        if (mode) {
            shouldEnableDarkMode(DarkModeConfig.YES)
        } else {
            shouldEnableDarkMode(DarkModeConfig.NO)
        }

    }

    private fun shouldEnableDarkMode(config: DarkModeConfig) {
        when (config) {
            DarkModeConfig.YES -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            DarkModeConfig.NO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //delegate.applyDayNight()
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