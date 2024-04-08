package com.rf.macgyver.ui.main.di

import android.content.Context
import com.rf.macgyver.data.remote.FionApiServices
import com.rf.macgyver.data.repository.SignInRepository
import com.rf.macgyver.roomDB.FionDatabase
import com.rf.macgyver.sdkInit.di.AppComponent
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.fragment.CompanyInfoFragment
import com.rf.macgyver.ui.main.fragment.SignInFragment
import com.rf.macgyver.ui.main.fragment.SignUpFragment
import com.rf.macgyver.ui.main.fragment.StartFragment
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.utils.ApplicationContext
import com.rf.macgyver.utils.FragmentScope
import com.rf.macgyver.utils.SharedPreference
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
    modules = [SignUpFragmentModule::class, SignInFragmentModuleDi::class, BaseFragmentModule::class]
)
interface SignUpFragmentComponent {
    fun inject(signUpFragment: SignUpFragment)
}

@Module(includes = [SignInFragmentModuleDi::class])
class SignUpFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [CompanyInfoFragmentModule::class, SignInFragmentModuleDi::class, BaseFragmentModule::class]
)
interface CompanyInfoFragmentComponent {
    fun inject(companyInfoFragment: CompanyInfoFragment)
}

@Module(includes = [SignInFragmentModuleDi::class])
class CompanyInfoFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [StartFragmentModule::class, SignInFragmentModuleDi::class, BaseFragmentModule::class]
)
interface StartFragmentComponent {
    fun inject(startFragment: StartFragment)
}

@Module(includes = [SignInFragmentModuleDi::class])
class StartFragmentModule


