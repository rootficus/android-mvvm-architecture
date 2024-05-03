package com.rf.accessAli.ui.main.di

import android.content.Context
import com.rf.accessAli.data.remote.AccessAliApiServices
import com.rf.accessAli.data.repository.SignInRepository
import com.rf.accessAli.roomDB.AccessAliDatabase
import com.rf.accessAli.sdkInit.di.AppComponent
import com.rf.accessAli.ui.base.BaseActivityModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.activity.SignInActivity
import com.rf.accessAli.ui.main.viewmodel.SignInViewModel

import com.rf.accessAli.utils.ActivityScope
import com.rf.accessAli.utils.ApplicationContext
import com.rf.accessAli.utils.SharedPreference
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
    fun provideApiServices(retrofit: Retrofit): AccessAliApiServices =
        retrofit.create(AccessAliApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideLoginRepository(
        apiServices: AccessAliApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        accessAliDatabase: AccessAliDatabase
    ): SignInRepository =
        SignInRepository(apiServices, context, sharedPreference, accessAliDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: SignInRepository): BaseViewModelFactory<SignInViewModel> =
        BaseViewModelFactory { SignInViewModel(signInRepository) }
}