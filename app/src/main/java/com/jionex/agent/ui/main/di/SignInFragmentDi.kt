package com.jionex.agent.ui.main.di

import android.content.Context
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.data.repository.SignInRepository
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.sdkInit.di.AppComponent
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.fragment.SignInFragment
import com.jionex.agent.ui.main.fragment.VerifyPinFragment
import com.jionex.agent.ui.main.viewmodel.SignInViewModel
import com.jionex.agent.utils.ApplicationContext
import com.jionex.agent.utils.FragmentScope
import com.jionex.agent.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class SignInFragmentModuleDi {

    @Provides
    @FragmentScope
    fun provideApiServices(retrofit: Retrofit): JionexApiServices =
        retrofit.create(JionexApiServices::class.java)

    @Provides
    @FragmentScope
    fun provideLoginRepository(
        apiServices: JionexApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        jionexDatabase: JionexDatabase
    ): SignInRepository = SignInRepository(apiServices, context, sharedPreference, jionexDatabase)

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
