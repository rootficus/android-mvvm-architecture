package com.jionex.agent.data.remote

import com.google.gson.GsonBuilder
import com.jionex.agent.BuildConfig
import com.jionex.agent.utils.AuthInterceptor
import com.jionex.agent.utils.Utility
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClientInstance {

    companion object {
        private var retrofit: Retrofit? = null
        public fun getRetrofitInstance(): Retrofit? {
            if (retrofit == null) {
                var okHttpClient = OkHttpClient
                    .Builder().apply {
                        addInterceptor(AuthInterceptor())
                        if (BuildConfig.DEBUG) {
                            addInterceptor(HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            })
                        }
                    }
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
                retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(
                        GsonBuilder()
                            .setLenient()
                            .create()))
                    .client(okHttpClient)
                    .baseUrl(BuildConfig.BASE_URL)
                    .build()
            }
            return retrofit
        }
    }



}