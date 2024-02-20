package com.rf.utellRestaurant.ui.main.di

import android.content.Context
import com.rf.utellRestaurant.data.remote.FionApiServices
import com.rf.utellRestaurant.data.repository.SignInRepository
import com.rf.utellRestaurant.roomDB.FionDatabase
import com.rf.utellRestaurant.sdkInit.di.AppComponent
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.fragment.SignInFragment
import com.rf.utellRestaurant.ui.main.viewmodel.SignInViewModel
import com.rf.utellRestaurant.utils.ApplicationContext
import com.rf.utellRestaurant.utils.FragmentScope
import com.rf.utellRestaurant.utils.SharedPreference
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


