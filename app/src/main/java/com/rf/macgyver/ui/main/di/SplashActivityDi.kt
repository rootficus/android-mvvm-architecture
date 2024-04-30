package com.rf.utellRestaurant.ui.main.di

import android.content.Context
import com.rf.macgyver.data.remote.MagApiServices
import com.rf.macgyver.data.repository.SignInRepository
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.sdkInit.di.AppComponent
import com.rf.macgyver.ui.base.BaseActivityModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.activity.SplashActivity
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.utils.ActivityScope
import com.rf.macgyver.utils.ApplicationContext
import com.rf.macgyver.utils.SharedPreference
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
    fun provideApiServices(retrofit: Retrofit): MagApiServices =
        retrofit.create(MagApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideLoginRepository(
        apiServices: MagApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        fionDatabase: MagDatabase
    ): SignInRepository =
        SignInRepository(apiServices, context, sharedPreference, fionDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: SignInRepository): BaseViewModelFactory<SignInViewModel> =
        BaseViewModelFactory { SignInViewModel(signInRepository) }


}