package com.rf.accessAli.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rf.accessAli.data.repository.DashBoardRepository
import com.rf.accessAli.roomDB.model.UniversityData
import com.rf.accessAli.ui.base.BaseViewModel
import com.rf.accessAli.utils.ResponseData
import com.rf.accessAli.utils.setError
import com.rf.accessAli.utils.setLoading
import com.rf.accessAli.utils.setSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(private val dashBoardRepository: DashBoardRepository) :
    BaseViewModel() {

     val universityResponseModel = MutableLiveData<ResponseData<List<UniversityData>>>()
    fun getUniversityData() {
        universityResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getUniversityData({ success -> universityResponseModel.setSuccess(success) },
                { error -> universityResponseModel.setError(error) },
                { message -> universityResponseModel.setError(message) })
        }
    }

    fun insertUniversityData(universityData: List<com.rf.accessAli.roomDB.model.UniversityData>)
    {
        dashBoardRepository.insertUniversityData(universityData)
    }

    fun getUniversityDataDB() : List<UniversityData>?
    {
        return dashBoardRepository.getUniversityData()

    }

}