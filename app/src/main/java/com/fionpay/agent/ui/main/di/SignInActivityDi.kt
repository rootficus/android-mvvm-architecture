package com.fionpay.agent.ui.main.di

import android.content.Context
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.data.repository.SignInRepository
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.sdkInit.di.AppComponent
import com.fionpay.agent.ui.base.BaseActivityModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.viewmodel.SignInViewModel

import com.fionpay.agent.utils.ActivityScope
import com.fionpay.agent.utils.ApplicationContext
import com.fionpay.agent.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [SignInModule::class, BaseActivityModule::class]
)
interface SignInComponent {
    fun inject(signInActivity: SignInActivity)
}

@Module
class SignInModule {

    @Provides
    @ActivityScope
    fun provideApiServices(retrofit: Retrofit): FionApiServices =
        retrofit.create(FionApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideLoginRepository(
        apiServices: FionApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        fionDatabase: FionDatabase
    ): SignInRepository =
        SignInRepository(apiServices, context, sharedPreference, fionDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: SignInRepository): BaseViewModelFactory<SignInViewModel> =
        BaseViewModelFactory { SignInViewModel(signInRepository) }
}