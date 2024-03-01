package com.rf.baseSideNav.data.repository

import android.content.Context
import com.rf.baseSideNav.data.remote.FionApiServices
import com.rf.baseSideNav.roomDB.FionDatabase
import com.rf.baseSideNav.ui.base.BaseRepository
import com.rf.baseSideNav.utils.SharedPreference

class DashBoardRepository(
    val apiServices: FionApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val fionDatabase: FionDatabase
) : BaseRepository() {

    fun getUserId(): String? {
        return sharedPreference.getUserId()
    }

    fun setFullName(fullName: String?) {
        fullName?.let { sharedPreference.setFullName(it) }
    }

    fun getProfileImage(): String? {
        return sharedPreference.getProfileImage()
    }

    fun setProfileImage(image: String?) {
        image?.let { sharedPreference.setProfileImage(it) }
    }

    fun getFullName(): String? {
        return sharedPreference.getFullName()
    }

    fun getEmail(): String? {
        return sharedPreference.getEmail()
    }

    fun setBLSuccess(success: Int?) {
        sharedPreference.setBLSuccess(success)
    }

    fun getBLSuccess(): Int {
        return sharedPreference.getBLSuccess()
    }

    fun setBLPending(pending: Int?) {
        sharedPreference.setBLPending(pending)
    }

    fun getBLPending(): Int {
        return sharedPreference.getBLPending()
    }

    fun setBLRejected(rejected: Int?) {
        sharedPreference.setBLRejected(rejected)
    }

    fun getBLRejected(): Int {
        return sharedPreference.getBLRejected()
    }

    fun setBLApproved(approved: Int?) {
        sharedPreference.setBLApproved(approved)
    }

    fun getBLApproved(): Int {
        return sharedPreference.getBLApproved()
    }

    fun setBLDanger(danger: Int?) {
        sharedPreference.setBLDanger(danger)
    }

    fun getBLDanger(): Int {
        return sharedPreference.getBLDanger()
    }

    fun setDashBoardDataModel(model: String?) {
        sharedPreference.setDashBoardDataModel(model)
    }

    fun getDashBoardDataModel(): String? {
        return sharedPreference.getDashBoardDataModel()
    }

    fun setBalance(balance: Float) {
        return sharedPreference.setBalance(balance)
    }

    fun getBalance(): Float {
        return sharedPreference.getBalance()
    }

    fun setAvailableBalance(balance: Float) {
        return sharedPreference.setAvailableBalance(balance)
    }

    fun getAvailableBalance(): Float {
        return sharedPreference.getAvailableBalance()
    }

    fun setHoldBalance(balance: Float) {
        return sharedPreference.setHoldBalance(balance)
    }

    fun getHoldBalance(): Float {
        return sharedPreference.getHoldBalance()
    }

}