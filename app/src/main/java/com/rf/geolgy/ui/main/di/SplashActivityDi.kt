package com.rf.geolgy.ui.main.di

import android.content.Context
import com.rf.geolgy.data.remote.GeolgyApiServices
import com.rf.geolgy.data.repository.SignInRepository
import com.rf.geolgy.roomDB.GeolgyDatabase
import com.rf.geolgy.sdkInit.di.AppComponent
import com.rf.geolgy.ui.base.BaseActivityModule
import com.rf.geolgy.ui.base.BaseViewModelFactory
import com.rf.geolgy.ui.main.activity.SplashActivity
import com.rf.geolgy.ui.main.viewmodel.SignInViewModel
import com.rf.geolgy.utils.ActivityScope
import com.rf.geolgy.utils.ApplicationContext
import com.rf.geolgy.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [SplashModule::class, BaseActivityModule::class]
)
interface SplashComponent {
    fun inject(splashActivity: SplashActivity)
}

@Module
class SplashModule {

    @Provides
    @ActivityScope
    fun provideApiServices(retrofit: Retrofit): GeolgyApiServices =
        retrofit.create(GeolgyApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideLoginRepository(
        apiServices: GeolgyApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        geolgyDatabase: GeolgyDatabase
    ): SignInRepository =
        SignInRepository(apiServices, context, sharedPreference, geolgyDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: SignInRepository): BaseViewModelFactory<SignInViewModel> =
        BaseViewModelFactory { SignInViewModel(signInRepository) }
}