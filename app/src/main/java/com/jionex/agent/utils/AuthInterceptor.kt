package com.jionex.agent.utils

import com.jionex.agent.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
/**
 * Akash.Singh
 * RootFicus.
 */
class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().apply {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer \$2y\$10\$bOGgeIDpm5LOS3Nm2nqUa.ZKZY/O0rBAvLxsOfNr7pSRO.LGumseu")
            header("Connection","close")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}