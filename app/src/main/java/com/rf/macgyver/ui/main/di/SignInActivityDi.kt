package com.rf.macgyver.ui.main.di

import android.content.Context
import com.rf.macgyver.data.remote.FionApiServices
import com.rf.macgyver.data.repository.SignInRepository
import com.rf.macgyver.roomDB.FionDatabase
import com.rf.macgyver.sdkInit.di.AppComponent
import com.rf.macgyver.ui.base.BaseActivityModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.activity.SignInActivity
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