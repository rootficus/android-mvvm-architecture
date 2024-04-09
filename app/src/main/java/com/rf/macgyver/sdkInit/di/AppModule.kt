package com.rf.macgyver.sdkInit.di

import android.app.Application
import android.content.Context
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.utils.ApplicationContext
import com.rf.macgyver.utils.AuthInterceptor
import com.rf.macgyver.utils.SharedPreference
import com.google.gson.GsonBuilder
import com.rf.macgyver.BuildConfig
import com.rf.macgyver.utils.NetworkHelper
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
    fun inject(mfSDK: UtellSDK)
    fun getSharedPreference(): SharedPreference
    fun getRetrofit(): Retrofit
    fun getNetwork(): NetworkHelper

    @ApplicationContext
    fun getContext(): Context
    fun getFionDatabase(): MagDatabase
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
    fun providesRetrofit(okHttpClient: OkHttpClient, BASEURL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .client(okHttpClient)
            .baseUrl(BASEURL)
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
    fun provideFionDatabase(@ApplicationContext context: Context): MagDatabase =
        MagDatabase.getDatabase(context)

}