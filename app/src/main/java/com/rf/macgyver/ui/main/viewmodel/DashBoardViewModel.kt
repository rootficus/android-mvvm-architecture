package com.rf.macgyver.ui.main.viewmodel

import com.rf.macgyver.data.repository.DashBoardRepository
import com.rf.macgyver.roomDB.model.DailyReporting
import com.rf.macgyver.roomDB.model.IncidentReport
import com.rf.macgyver.roomDB.model.InspectionForm
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.ui.base.BaseViewModel
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(private val dashBoardRepository: DashBoardRepository) :
    BaseViewModel() {

    fun getLoginDetailsUsingToken(uniqueToken: String): LoginDetails?{
        return dashBoardRepository.getLoginDetailsUsingToken(uniqueToken )
    }
    fun insertInspectionForm(inspectionForm: InspectionForm)
    {
        dashBoardRepository.insertInspectionForm(inspectionForm)
    }

    fun getInspectionForm(uniqueToken : String) : InspectionForm?
    {
        return dashBoardRepository.getInspectionForm(uniqueToken)

    }
    fun updateInspectionForm(inspectionForm: InspectionForm)
    {
        dashBoardRepository.updateInspectionForm(inspectionForm)
    }
    fun insertIncidentReport(incidentReport: IncidentReport)
    {
        dashBoardRepository.insertIncidentReport(incidentReport)
    }

    fun getIncidentReport(uniqueToken : String) : List<IncidentReport>?
    {
        return dashBoardRepository.getIncidentReport(uniqueToken)

    }
    fun updateIncidentReport(incidentReport: IncidentReport)
    {
        dashBoardRepository.updateIncidentReport(incidentReport)
    }
        fun insertDailyReporting(dailyReporting: DailyReporting){
            dashBoardRepository.insertDailyReporting(dailyReporting)
        }
    fun getDailyReportingData(uniqueToken : String) : List<DailyReporting>?
    {
        return dashBoardRepository.getDailyReportingData(uniqueToken)
    }

    fun setFullName(fullName: String?) {
        dashBoardRepository.setFullName(fullName)
    }

    fun getFullName(): String? {
        return dashBoardRepository.getFullName()
    }

    fun setProfileImage(fullName: String?) {
        dashBoardRepository.setProfileImage(fullName)
    }

    fun getProfileImage(): String? {
        return dashBoardRepository.getProfileImage()
    }

    fun getEmail(): String? {
        return dashBoardRepository.getEmail()
    }

    fun getUserId(): String? {
        return dashBoardRepository.getUserId()
    }

    fun setBLSuccess(success: Int?) {
        dashBoardRepository.setBLSuccess(success)
    }

    fun getBLSuccess(): Int {
        return dashBoardRepository.getBLSuccess()
    }

    fun setBLPending(pending: Int?) {
        dashBoardRepository.setBLPending(pending)
    }

    fun getBLPending(): Int {
        return dashBoardRepository.getBLPending()
    }

    fun setBLRejected(rejected: Int?) {
        dashBoardRepository.setBLRejected(rejected)
    }

    fun getBLRejected(): Int {
        return dashBoardRepository.getBLRejected()
    }

    fun setBLApproved(approved: Int?) {
        dashBoardRepository.setBLApproved(approved)
    }

    fun getBLApproved(): Int {
        return dashBoardRepository.getBLApproved()
    }

    fun setDashBoardDataModel(model: String?) {
        dashBoardRepository.setDashBoardDataModel(model)
    }

    fun getDashBoardDataModel(): String? {
        return dashBoardRepository.getDashBoardDataModel()
    }

    fun setBalance(balance: Float) {
        return dashBoardRepository.setBalance(balance)
    }

    fun getBalance(): Float {
        return dashBoardRepository.getBalance()
    }

    fun setAvailableBalance(balance: Float) {
        return dashBoardRepository.setAvailableBalance(balance)
    }

    fun getAvailableBalance(): Float {
        return dashBoardRepository.getAvailableBalance()
    }

    fun setHoldBalance(balance: Float) {
        return dashBoardRepository.setHoldBalance(balance)
    }

    fun getHoldBalance(): Float {
        return dashBoardRepository.getHoldBalance()
    }

    fun setBLDanger(danger: Int?) {
        dashBoardRepository.setBLDanger(danger)
    }

    fun getBLDanger(): Int {
        return dashBoardRepository.getBLDanger()
    }

}