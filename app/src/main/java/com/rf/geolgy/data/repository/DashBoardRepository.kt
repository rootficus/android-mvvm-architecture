package com.rf.geolgy.data.repository

import android.content.Context
import com.rf.geolgy.data.model.request.CompanyDetailsRequest
import com.rf.geolgy.data.model.request.CreateChallanRequest
import com.rf.geolgy.data.model.response.CompanyDetailsResponse
import com.rf.geolgy.data.model.response.CreateChallanResponse
import com.rf.geolgy.data.remote.GeolgyApiServices
import com.rf.geolgy.roomDB.GeolgyDatabase
import com.rf.geolgy.ui.base.BaseRepository
import com.rf.geolgy.utils.SharedPreference

class DashBoardRepository(
    val apiServices: GeolgyApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val geolgyDatabase: GeolgyDatabase
) : BaseRepository() {

    fun getCompanyDetails(
        success: (companyDetailsResponse: CompanyDetailsResponse) -> Unit,
        companyDetailsRequest: CompanyDetailsRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getCompanyDetails(companyDetailsRequest).apply {
            execute(this, success, fail, context, message)
        }
    }
    fun createChallan(
        success: (createChallanResponse: CreateChallanResponse) -> Unit,
        createChallanRequest: CreateChallanRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.createChallan(createChallanRequest).apply {
            execute(this, success, fail, context, message)
        }
    }


    fun setSignInDataModel(model: String?) {
        model?.let { sharedPreference.setSignInDataModel(model) }
    }

    fun getSignInDataModel(): String? {
        return sharedPreference.getSignInDataModel()
    }

}