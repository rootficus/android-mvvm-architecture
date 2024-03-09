package com.rf.geolgy.ui.main.di

import android.content.Context
import com.rf.geolgy.data.remote.GeolgyApiServices
import com.rf.geolgy.data.repository.SignInRepository
import com.rf.geolgy.roomDB.GeolgyDatabase
import com.rf.geolgy.sdkInit.di.AppComponent
import com.rf.geolgy.ui.base.BaseFragmentModule
import com.rf.geolgy.ui.base.BaseViewModelFactory
import com.rf.geolgy.ui.main.fragment.SignInFragment
import com.rf.geolgy.ui.main.viewmodel.SignInViewModel
import com.rf.geolgy.utils.ApplicationContext
import com.rf.geolgy.utils.FragmentScope
import com.rf.geolgy.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class SignInFragmentModuleDi {

    @Provides
    @FragmentScope
    fun provideApiServices(retrofit: Retrofit): GeolgyApiServices =
        retrofit.create(GeolgyApiServices::class.java)

    @Provides
    @FragmentScope
    fun provideLoginRepository(
        apiServices: GeolgyApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        geolgyDatabase: GeolgyDatabase
    ): SignInRepository = SignInRepository(apiServices, context, sharedPreference, geolgyDatabase)

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


