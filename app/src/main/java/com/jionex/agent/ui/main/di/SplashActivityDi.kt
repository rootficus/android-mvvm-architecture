package com.jionex.agent.ui.main.di

import android.content.Context
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.data.repository.SignInRepository
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.sdkInit.di.AppComponent
import com.jionex.agent.ui.base.BaseActivityModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.activity.SplashActivity
import com.jionex.agent.ui.main.viewmodel.SignInViewModel
import com.jionex.agent.utils.ActivityScope
import com.jionex.agent.utils.ApplicationContext
import com.jionex.agent.utils.SharedPreference
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
    fun provideApiServices(retrofit: Retrofit): JionexApiServices =
        retrofit.create(JionexApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideLoginRepository(
        apiServices: JionexApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        jionexDatabase: JionexDatabase
    ): SignInRepository =
        SignInRepository(apiServices, context, sharedPreference, jionexDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: SignInRepository): BaseViewModelFactory<SignInViewModel> =
        BaseViewModelFactory { SignInViewModel(signInRepository) }
}