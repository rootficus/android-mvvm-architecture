package com.jionex.agent.sdkInit.di

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.utils.ApplicationContext
import com.jionex.agent.utils.AuthInterceptor
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.BuildConfig
import com.jionex.agent.sdkInit.JionexSDK
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {
    fun inject(mfSDK: JionexSDK)
    fun getSharedPreference(): SharedPreference
    fun getRetrofit(): Retrofit
    fun getNetwork(): NetworkHelper

    @ApplicationContext
    fun getContext(): Context
    fun getJionexDatabase(): JionexDatabase
}

@Module
class AppModule(private val mApplication: Application) {

    @ApplicationContext
    @Singleton
    @Provides
    fun getContext(): Context = mApplication

    @Provides
    @Singleton
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreference: SharedPreference): OkHttpClient = OkHttpClient
        .Builder().apply {
            addInterceptor(
                AuthInterceptor(sharedPreference)
            )
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .cache(null)//new Cache(sContext.getCacheDir(),10*1024*1024)
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreference =
        SharedPreference(context)

    @Provides
    @Singleton
    fun provideNetwork(@ApplicationContext context: Context): NetworkHelper = NetworkHelper(context)

    @Provides
    @Singleton
    fun provideJionexDatabase(@ApplicationContext context: Context): JionexDatabase =
        JionexDatabase.getDatabase(context)

}