package com.fionpay.agent.utils

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Akash.Singh
 * RootFicus.
 */
class AuthInterceptor(private val sharedPreference: SharedPreference) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().apply {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer ${sharedPreference.getToken()}")
            header("Connection", "close")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}