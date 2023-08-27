package com.jionex.agent.ui.main.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jionex.agent.data.model.request.GetBalanceByFilterRequest
import com.jionex.agent.data.model.request.GetMessageByFilterRequest
import com.jionex.agent.data.model.request.GetModemsByFilterRequest
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.response.DashBoardItemResponse
import com.jionex.agent.data.model.response.GetBalanceManageRecord
import com.jionex.agent.data.model.response.GetMessageManageRecord
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
import kotlinx.coroutines.GlobalScope
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

    val dashBoardItemResponseModel = MutableLiveData<ResponseData<DashBoardItemResponse>>()
    fun dashBoardData(){
        dashBoardItemResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.dashBoardData({ success -> dashBoardItemResponseModel.setSuccess(success) },
                { error -> dashBoardItemResponseModel.setError(error) },
                { message -> dashBoardItemResponseModel.setError(message) })
        }
    }

    val getBalanceManageRecordModel = MutableLiveData<ResponseData<List<GetBalanceManageRecord>>>()
    fun getBalanceByFilter(getBalanceByFilterRequest: GetBalanceByFilterRequest) {
        getBalanceManageRecordModel.setLoading(null)

        if (dashBoardRepository.getCountBalanceManageRecord(-1)>0){
            var arrayList = getBalanceByFilterRequest.balance_manager_filter?.let {
                getBalanceManageRecord(it)
            }
            getBalanceManageRecordModel.setSuccess(arrayList)
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                dashBoardRepository.getBalanceByFilter(
                    { success ->
                        GlobalScope.launch {
                            success.forEach {
                                dashBoardRepository.insertBalanceManagerRecord(it)
                            }
                        }
                        getBalanceManageRecordModel.setSuccess(success)
                    },
                    { error -> getBalanceManageRecordModel.setError(error) },
                    getBalanceByFilterRequest,
                    { message -> getBalanceManageRecordModel.setError(message) })
            }
        }

    }

    fun getBalanceManageRecord(it: Int) = dashBoardRepository.getBalanceManageRecord(it)

    fun getCountBalanceManageRecord(it: Int)  = dashBoardRepository.getCountBalanceManageRecord(it)


    val getMessageManageRecordModel = MutableLiveData<ResponseData<List<GetMessageManageRecord>>>()
    fun getMessageByFilter(getMessageByFilterRequest: GetMessageByFilterRequest) {
        getMessageManageRecordModel.setLoading(null)

        if (dashBoardRepository.getCountMessageManageRecord(-1)>0){
            var arrayList = getMessageByFilterRequest.message_type?.let {
                getMessageManageRecord(it)
            }
            getMessageManageRecordModel.setSuccess(arrayList)
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                dashBoardRepository.getMessageByFilter(
                    { success ->
                        GlobalScope.launch {
                            success.forEach {
                                dashBoardRepository.insertGetMessageManageRecord(it)
                            }
                        }
                        getMessageManageRecordModel.setSuccess(success)
                    },
                    { error -> getMessageManageRecordModel.setError(error) },
                    getMessageByFilterRequest,
                    { message -> getMessageManageRecordModel.setError(message) })
            }
        }
    }
    fun getMessageManageRecord(it: Int) = dashBoardRepository.getMessageManageRecord(it)

    fun getCountMessageManageRecord(it: Int)  = dashBoardRepository.getCountMessageManageRecord(it)

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

    fun setTotalPending(totalPending: Long) {
        dashBoardRepository.setTotalPending(totalPending)
    }

    fun getTotalPending(): Long {
        return dashBoardRepository.getTotalPending()
    }

    fun setTotalTransactions(totalTransactions: String) {
        dashBoardRepository.setTotalTransactions(totalTransactions)
    }

    fun getTotalTransactions(): String? {
        return dashBoardRepository.getTotalTransactions()
    }

    fun setTodayTransactions(todayTransactions: Long) {
        dashBoardRepository.setTodayTransactions(todayTransactions)
    }

    fun getTodayTransactions(): Long {
        return dashBoardRepository.getTodayTransactions()
    }

    fun setTotalTrxAmount(totalTrxAmount: String) {
        dashBoardRepository.setTotalTrxAmount(totalTrxAmount)
    }

    fun getTotalTrxAmount(): String? {
        return dashBoardRepository.getTotalTrxAmount()
    }

    fun setTodayTrxAmount(todayTrxAmount: String) {
        dashBoardRepository.setTodayTrxAmount(todayTrxAmount)
    }

    fun getTodayTrxAmount(): String? {
        return dashBoardRepository.getTodayTrxAmount()
    }

    fun setTotalModem(totalModem: Int) {
        dashBoardRepository.setTotalModem(totalModem)
    }

    fun getTotalModem(): Int {
        return dashBoardRepository.getTotalModem()
    }

    fun setBLSuccess(success: Int?) {
        dashBoardRepository.setBLSuccess(success)
    }

    fun getBLSuccess():Int{
        return dashBoardRepository.getBLSuccess()
    }

    fun setBLPending(pending: Int?) {
        dashBoardRepository.setBLPending(pending)
    }

    fun getBLPending():Int{
        return dashBoardRepository.getBLPending()
    }

    fun setBLRejected(rejected: Int?) {
        dashBoardRepository.setBLRejected(rejected)
    }

    fun getBLRejected():Int{
        return dashBoardRepository.getBLRejected()
    }

    fun setBLApproved(approved: Int?) {
        dashBoardRepository.setBLApproved(approved)
    }

    fun getBLApproved():Int{
        return dashBoardRepository.getBLApproved()
    }

    fun setBLDanger(danger: Int?) {
        dashBoardRepository.setBLDanger(danger)
    }

    fun getBLDanger():Int{
        return dashBoardRepository.getBLDanger()
    }

    fun deleteLocalBlManager() {
        dashBoardRepository.deleteLocalBlManager()
    }

    fun deleteLocalMessageManager() {
        dashBoardRepository.deleteLocalMessageManager()
    }

}