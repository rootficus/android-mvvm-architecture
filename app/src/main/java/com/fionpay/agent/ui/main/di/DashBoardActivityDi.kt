package com.fionpay.agent.ui.main.di

import android.content.Context
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.data.repository.DashBoardRepository
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.sdkInit.di.AppComponent
import com.fionpay.agent.ui.base.BaseActivityModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.DashBoardActivity
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
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
    modules = [DashBoardActivityModule::class, BaseActivityModule::class]
)
interface DashBoardActivityComponent {
    fun inject(dashBoardActivity: DashBoardActivity)
}

@Module
class DashBoardActivityModule {

    @Provides
    @ActivityScope
    fun provideApiServices(retrofit: Retrofit): FionApiServices =
        retrofit.create(FionApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideDashBoardRepository(
        apiServices: FionApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        fionDatabase: FionDatabase
    ): DashBoardRepository =
        DashBoardRepository(apiServices, context, sharedPreference, fionDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: DashBoardRepository): BaseViewModelFactory<DashBoardViewModel> =
        BaseViewModelFactory { DashBoardViewModel(signInRepository) }
}