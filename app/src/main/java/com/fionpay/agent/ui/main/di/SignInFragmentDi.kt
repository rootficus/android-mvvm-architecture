package com.fionpay.agent.ui.main.di

import android.content.Context
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.data.repository.SignInRepository
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.sdkInit.di.AppComponent
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.fragment.SignInFragment
import com.fionpay.agent.ui.main.fragment.VerifyPinFragment
import com.fionpay.agent.ui.main.viewmodel.SignInViewModel
import com.fionpay.agent.utils.ApplicationContext
import com.fionpay.agent.utils.FragmentScope
import com.fionpay.agent.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class SignInFragmentModuleDi {

    @Provides
    @FragmentScope
    fun provideApiServices(retrofit: Retrofit): FionApiServices =
        retrofit.create(FionApiServices::class.java)

    @Provides
    @FragmentScope
    fun provideLoginRepository(
        apiServices: FionApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        fionDatabase: FionDatabase
    ): SignInRepository = SignInRepository(apiServices, context, sharedPreference, fionDatabase)

    @FragmentScope
    @Provides
    fun provideViewModelFactory(signInRepository: SignInRepository): BaseViewModelFactory<SignInViewModel> =
        BaseViewModelFactory { SignInViewModel(signInRepository) }
}

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [SignInFragmentModule::class, SignInFragmentModuleDi::class, BaseFragmentModule::class]
)
interface SignInFragmentComponent {
    fun inject(signInFragment: SignInFragment)
}

@Module(includes = [SignInFragmentModuleDi::class])
class SignInFragmentModule


@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [VerifyPinFragmentModule::class, SignInFragmentModuleDi::class, BaseFragmentModule::class]
)
interface VerifyPinFragmentComponent {
    fun inject(verifyPinFragment: VerifyPinFragment)
}

@Module(includes = [SignInFragmentModuleDi::class])
class VerifyPinFragmentModule

