package com.rf.accessAli.utils

import androidx.lifecycle.MutableLiveData

/**
 * Akash.Singh
 * RootFicus.
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class ResponseDataOfArray<out T>(
    val status: Status,
    val data: List<T>? = null,
    val message: String? = null
)

fun <T> MutableLiveData<ResponseDataOfArray<T>>.setSuccess(data: List<T>? = null) {
    value = ResponseDataOfArray(Status.SUCCESS, data, null)
}

fun <T> MutableLiveData<ResponseDataOfArray<T>>.setLoading(data: List<T>? = null) {
    value = ResponseDataOfArray(Status.LOADING, data, null)
}

fun <T> MutableLiveData<ResponseDataOfArray<T>>.setError(
    msg: String? = null,
    data: List<T>? = null
) {
    value = ResponseDataOfArray(Status.ERROR, data, msg)
}

data class ResponseData<out T>(val status: Status, val data: T? = null, val message: String? = null)

fun <T> MutableLiveData<ResponseData<T>>.setSuccess(data: T? = null) {
    value = ResponseData(Status.SUCCESS, data, null)
}

fun <T> MutableLiveData<ResponseData<T>>.setLoading(data: T? = null) {
    value = ResponseData(Status.LOADING, data, null)
}

fun <T> MutableLiveData<ResponseData<T>>.setError(msg: String? = null, data: T? = null) {
    value = ResponseData(Status.ERROR, data, msg)
}
