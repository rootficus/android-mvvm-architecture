package com.rf.accessAli.ui.main.di

import android.content.Context
import com.rf.accessAli.data.remote.AccessAliApiServices
import com.rf.accessAli.data.repository.SignInRepository
import com.rf.accessAli.roomDB.AccessAliDatabase
import com.rf.accessAli.sdkInit.di.AppComponent
import com.rf.accessAli.ui.base.BaseFragmentModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.fragment.SignInFragment
import com.rf.accessAli.ui.main.viewmodel.SignInViewModel
import com.rf.accessAli.utils.ApplicationContext
import com.rf.accessAli.utils.FragmentScope
import com.rf.accessAli.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class SignInFragmentModuleDi {

    @Provides
    @FragmentScope
    fun provideApiServices(retrofit: Retrofit): AccessAliApiServices =
        retrofit.create(AccessAliApiServices::class.java)

    @Provides
    @FragmentScope
    fun provideLoginRepository(
        apiServices: AccessAliApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        accessAliDatabase: AccessAliDatabase
    ): SignInRepository = SignInRepository(apiServices, context, sharedPreference, accessAliDatabase)

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


