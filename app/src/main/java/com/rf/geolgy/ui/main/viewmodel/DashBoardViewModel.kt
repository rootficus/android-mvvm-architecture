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

}