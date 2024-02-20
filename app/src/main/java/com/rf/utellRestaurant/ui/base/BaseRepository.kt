package com.rf.utellRestaurant.ui.base

import android.content.Context
import android.util.Log
import com.rf.utellRestaurant.utils.APIResponseCode
import com.rf.utellRestaurant.utils.enqueue
import com.rf.utellRestaurant.R
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.Objects


/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseRepository {

    /**
     * Common response execute function
     * if it is type of 'BaseResponseModel<T>'
     * And you need only result as response
     */
    fun <P> execute1(
        context: Context,
        response: Response<BaseResponseModel2<P>>,
        success: (payload: ArrayList<P>) -> Unit,
        fail: (error: String) -> Unit
    ) {
        try {
            if (response.isSuccessful) {
                response.body()?.let { body_ ->
                    body_.data?.let { results_ ->
                        when (body_.status ?: -1) {
                            APIResponseCode.ResponseCode100.codeValue -> {
                                success.invoke(results_)
                            }

                            else -> {
                                fail.invoke(
                                    body_.message
                                        ?: context.getString(R.string.error_something_went_wrong)
                                )
                            }
                        }

                    } ?: kotlin.run {
                        fail.invoke(
                            body_.message ?: context.getString(R.string.error_something_went_wrong)
                        )
                    }
                } ?: kotlin.run {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                }
            } else {
                fail.invoke(context.getString(R.string.error_something_went_wrong))
            }
        } catch (e: Exception) {
            fail.invoke(context.getString(R.string.error_something_went_wrong))
        }

    }

    /**
     * Common response execute function
     * if it is of type of Call<BaseResponseModel<T>>?'
     * And you need only result as response
     * when 100 is success
     */
    fun <P> execute(
        call: Call<BaseResponseModel<P>>,
        success: (payload: P) -> Unit,
        fail: (error: String) -> Unit,
        context: Context,
        message: (message: String) -> Unit
    ) {
        call.enqueue {
            onResponse = { response_ ->
                try {
                    when (response_.code()) {
                        APIResponseCode.ResponseCode200.codeValue -> {
                            response_.body()?.let { body_ ->
                                body_.data?.let { results_ ->
                                    if (body_.status != null) {
                                        when (body_.status) {
                                            200.0 -> {
                                                success.invoke(results_)
                                            }

                                            200 -> {
                                                success.invoke(results_)
                                                // message.invoke(body_.message.toString())
                                            }

                                            true -> {
                                                success.invoke(results_)
                                            }

                                            else -> {
                                                fail.invoke(
                                                    body_.message
                                                        ?: context.getString(R.string.error_something_went_wrong)
                                                )
                                            }
                                        }
                                    } else if (body_.message.equals("success")) {
                                        success.invoke(results_)
                                    } else if (body_.message!!.contains("Status")) {
                                        success.invoke(results_)
                                    }
                                } ?: kotlin.run {
                                    fail.invoke(
                                        body_.message
                                            ?: context.getString(R.string.error_something_went_wrong)
                                    )
                                }
                            } ?: kotlin.run {
                                fail.invoke(context.getString(R.string.error_something_went_wrong))
                            }
                        }

                        APIResponseCode.ResponseCode201.codeValue -> {
                            response_.body()?.let { body_ ->
                                body_.data?.let { results_ ->
                                    when (body_.status) {
                                        200.0 -> {
                                            success.invoke(results_)
                                        }

                                        200 -> {
                                            success.invoke(results_)
                                            // message.invoke(body_.message.toString())
                                        }

                                        true -> {
                                            success.invoke(results_)
                                        }

                                        else -> {
                                            fail.invoke(
                                                body_.message
                                                    ?: context.getString(R.string.error_something_went_wrong)
                                            )
                                        }
                                    }
                                } ?: kotlin.run {
                                    fail.invoke(
                                        body_.message
                                            ?: context.getString(R.string.error_something_went_wrong)
                                    )
                                }
                            } ?: kotlin.run {
                                fail.invoke(context.getString(R.string.error_something_went_wrong))
                            }
                        }

                        APIResponseCode.ResponseCode404.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }

                        APIResponseCode.ResponseCode401.codeValue -> {
                            getErrorBody(response_)?.let {
                                fail.invoke(it)
                            }
                        }

                        APIResponseCode.ResponseCode403.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }

                        APIResponseCode.ResponseCode422.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }

                        APIResponseCode.ResponseCode500.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                    }
                } catch (e: Exception) {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                }
            }

            onFailure = {
                if (it.toString().contains("failed to connect")) {
                    fail.invoke(context.getString(R.string.no_network))
                } else {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                    Log.i("Error Something", "::" + it.toString())
                }
            }
        }
    }


    /**
     * Common response execute function
     * if it is of type of Call<BaseResponseModel<T>>?'
     * And you need only result as response
     * when success i dyanamic
     */
    fun <P> execute(
        call: Call<BaseResponseModel<P>>,
        success: (payload: P) -> Unit,
        fail: (error: String) -> Unit,
        context: Context,
        code: Int
    ) {
        call.enqueue {
            onResponse = { response_ ->
                try {
                    when (response_.code()) {
                        APIResponseCode.ResponseCode200.codeValue -> {
                            response_.body()?.let { body_ ->
                                body_.data?.let { results_ ->
                                    when (body_.status ?: -1) {
                                        code -> {
                                            success.invoke(results_)
                                        }

                                        else -> {
                                            fail.invoke(
                                                body_.message
                                                    ?: context.getString(R.string.error_something_went_wrong)
                                            )
                                        }
                                    }
                                } ?: kotlin.run {
                                    fail.invoke(
                                        body_.message
                                            ?: context.getString(R.string.error_something_went_wrong)
                                    )
                                }
                            } ?: kotlin.run {
                                fail.invoke(context.getString(R.string.error_something_went_wrong))
                            }
                        }

                        APIResponseCode.ResponseCode403.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                    }
                } catch (e: Exception) {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                }
            }

            onFailure = {
                if (it.toString().contains("failed to connect")) {
                    fail.invoke(context.getString(R.string.no_network))
                } else {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                }
            }
        }
    }

    open fun getErrorBody(response: Response<*>): String? {
        val errorBody = response.errorBody()
        val errorMessage: String = try {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Objects.isNull(errorBody)
                } else {
                    errorBody == null
                }
            ) {
                response.message()
            } else {
                JSONObject(errorBody?.string()).getString("error")
            }
        } catch (e: IOException) {
            throw Exception("could not read error body", e)
        }
        return errorMessage
    }

    open fun getErrorMessage(response: Response<*>): String? {
        val errorBody = response.errorBody()
        val errorMessage: String = try {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Objects.isNull(errorBody)
                } else {
                    errorBody == null
                }
            ) {
                response.message()
            } else {
                JSONObject(errorBody?.string()).getString("message")
            }
        } catch (e: IOException) {
            throw Exception("could not read error body", e)
        }
        return errorMessage
    }


    fun <P> execute2(
        call: Call<BaseResponseModel2<P>>,
        success: (payload: ArrayList<P>) -> Unit,
        fail: (error: String) -> Unit,
        context: Context,
        message: (message: String) -> Unit
    ) {
        call.enqueue {
            onResponse = { response_ ->
                try {
                    when (response_.code()) {
                        APIResponseCode.ResponseCode200.codeValue -> {
                            response_.body()?.let { body_ ->
                                body_.data?.let { results_ ->
                                    if (body_.status != null) {
                                        when (body_.status) {
                                            200.0 -> {
                                                success.invoke(results_)
                                            }

                                            200 -> {
                                                success.invoke(results_)
                                                // message.invoke(body_.message.toString())
                                            }

                                            true -> {
                                                success.invoke(results_)
                                            }

                                            else -> {
                                                fail.invoke(
                                                    body_.message
                                                        ?: context.getString(R.string.error_something_went_wrong)
                                                )
                                            }
                                        }
                                    } else if (body_.message.equals("success")) {
                                        success.invoke(results_)
                                    } else if (body_.message!!.contains("Status")) {
                                        success.invoke(results_)
                                    }
                                } ?: kotlin.run {
                                    fail.invoke(
                                        body_.message
                                            ?: context.getString(R.string.error_something_went_wrong)
                                    )
                                }
                            } ?: kotlin.run {
                                fail.invoke(context.getString(R.string.error_something_went_wrong))
                            }
                        }

                        APIResponseCode.ResponseCode201.codeValue -> {
                            response_.body()?.let { body_ ->
                                body_.data?.let { results_ ->
                                    when (body_.status ?: -1) {
                                        200 -> {
                                            success.invoke(results_)
                                            // message.invoke(body_.message.toString())
                                        }

                                        else -> {
                                            fail.invoke(
                                                body_.message
                                                    ?: context.getString(R.string.error_something_went_wrong)
                                            )
                                        }
                                    }
                                } ?: kotlin.run {
                                    fail.invoke(
                                        body_.message
                                            ?: context.getString(R.string.error_something_went_wrong)
                                    )
                                }
                            } ?: kotlin.run {
                                fail.invoke(context.getString(R.string.error_something_went_wrong))
                            }
                        }

                        APIResponseCode.ResponseCode403.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }

                        else -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                    }

                } catch (e: Exception) {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                }
            }

            onFailure = {
                if (it.toString().contains("failed to connect")) {
                    fail.invoke(context.getString(R.string.no_network))
                } else {
                    Log.i("Error", "" + it.toString())
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                }
            }
        }
    }
}