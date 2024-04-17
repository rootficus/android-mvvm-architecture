package com.rf.geolgy.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rf.geolgy.data.model.request.CompanyDetailsRequest
import com.rf.geolgy.data.model.request.CreateChallanRequest
import com.rf.geolgy.data.model.response.CompanyDetailsResponse
import com.rf.geolgy.data.model.response.CreateChallanResponse
import com.rf.geolgy.data.repository.DashBoardRepository
import com.rf.geolgy.ui.base.BaseViewModel
import com.rf.geolgy.utils.ResponseData
import com.rf.geolgy.utils.setError
import com.rf.geolgy.utils.setLoading
import com.rf.geolgy.utils.setSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(private val dashBoardRepository: DashBoardRepository) :
    BaseViewModel() {

    val items: ArrayList<String> = arrayListOf("1","2","3","4","5","6","7","8")

    val getCompanyDetailsResponseModel = MutableLiveData<ResponseData<CompanyDetailsResponse>>()
    fun getCompanyDetails(companyDetailsRequest: CompanyDetailsRequest) {
        getCompanyDetailsResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getCompanyDetails({ success ->
                getCompanyDetailsResponseModel.setSuccess(
                    success
                )
            },
                companyDetailsRequest,
                { error -> getCompanyDetailsResponseModel.setError(error) },
                { message -> getCompanyDetailsResponseModel.setError(message) })
        }
    }

    val createChallanResponseModel = MutableLiveData<ResponseData<CreateChallanResponse>>()
    fun createChallan(createChallanRequest: CreateChallanRequest) {
        createChallanResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.createChallan({ success ->
                createChallanResponseModel.setSuccess(
                    success
                )
            },
                createChallanRequest,
                { error -> createChallanResponseModel.setError(error) },
                { message -> createChallanResponseModel.setError(message) })
        }
    }


    fun setSignInDataModel(model: String?) {
        dashBoardRepository.setSignInDataModel(model)
    }

    fun getSignInDataModel(): String? {
        return dashBoardRepository.getSignInDataModel()
    }

    fun setFullName(fullName: String?) {
        dashBoardRepository.setFullName(fullName)
    }

    fun setToken(token: String) {
        dashBoardRepository.setToken(token)
    }

    fun setPassword(password: String) {
        dashBoardRepository.setPassword(password)
    }

    fun setIsLogin(isLogin: Boolean) {
        dashBoardRepository.setIsLogin(isLogin)
    }

    fun setEditText6(text: String?) {
        text?.let { dashBoardRepository.setEditText6(text) }
    }

    fun getEditText6(): String? {
        return dashBoardRepository.getEditText6()
    }

    fun setEditText7(text: String?) {
        text?.let { dashBoardRepository.setEditText7(text) }
    }

    fun getEditText7(): String? {
        return dashBoardRepository.getEditText7()
    }

    fun setEditText8(text: Int?) {
        text?.let { dashBoardRepository.setEditText8(text) }
    }

    fun getEditText8(): Int? {
        return dashBoardRepository.getEditText8()
    }

    fun setEditText10(text: String?) {
        text?.let { dashBoardRepository.setEditText10(text) }
    }

    fun getEditText10(): String? {
        return dashBoardRepository.getEditText10()
    }
    fun setEdit2Text10(text: String?) {
        text?.let { dashBoardRepository.setEdit2Text10(text) }
    }

    fun getEdit2Text10(): String? {
        return dashBoardRepository.getEdit2Text10()
    }

    fun setEditText14(text: String?) {
        text?.let { dashBoardRepository.setEditText14(text) }
    }

    fun getEditText14(): String? {
        return dashBoardRepository.getEditText14()
    }

    fun setExpireHour(hour: Int?) {
        hour?.let { dashBoardRepository.setExpireHour(hour) }
    }

    fun getExpireHour(): Int? {
        return dashBoardRepository.getExpireHour()
    }
}