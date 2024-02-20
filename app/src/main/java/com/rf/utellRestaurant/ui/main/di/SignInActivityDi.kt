package com.rf.utellRestaurant.ui.main.di

import android.content.Context
import com.rf.utellRestaurant.data.remote.FionApiServices
import com.rf.utellRestaurant.data.repository.SignInRepository
import com.rf.utellRestaurant.roomDB.FionDatabase
import com.rf.utellRestaurant.sdkInit.di.AppComponent
import com.rf.utellRestaurant.ui.base.BaseActivityModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.activity.SignInActivity
import com.rf.utellRestaurant.ui.main.viewmodel.SignInViewModel

import com.rf.utellRestaurant.utils.ActivityScope
import com.rf.utellRestaurant.utils.ApplicationContext
import com.rf.utellRestaurant.utils.SharedPreference
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