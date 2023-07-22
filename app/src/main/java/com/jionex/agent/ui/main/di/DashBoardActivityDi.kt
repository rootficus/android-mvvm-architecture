package com.jionex.agent.ui.main.di

import android.content.Context
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.data.repository.DashBoardRepository
import com.jionex.agent.data.repository.SignInRepository
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.sdkInit.di.AppComponent
import com.jionex.agent.ui.base.BaseActivityModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.activity.DashBoardActivity
import com.jionex.agent.viewmodel.DashBoardViewModel
import com.jionex.agent.viewmodel.SignInViewModel
import com.jionex.agent.utils.ActivityScope
import com.jionex.agent.utils.ApplicationContext
import com.jionex.agent.utils.SharedPreference
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
    fun provideApiServices(retrofit: Retrofit): JionexApiServices =
        retrofit.create(JionexApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideDashBoardRepository(
        apiServices: JionexApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        jionexDatabase: JionexDatabase
    ): DashBoardRepository =
        DashBoardRepository(apiServices, context, sharedPreference, jionexDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: DashBoardRepository): BaseViewModelFactory<DashBoardViewModel> =
        BaseViewModelFactory { DashBoardViewModel(signInRepository) }
}