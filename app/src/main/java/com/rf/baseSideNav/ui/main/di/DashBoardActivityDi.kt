package com.rf.baseSideNav.ui.main.di

import android.content.Context
import com.rf.baseSideNav.data.remote.FionApiServices
import com.rf.baseSideNav.data.repository.DashBoardRepository
import com.rf.baseSideNav.roomDB.FionDatabase
import com.rf.baseSideNav.sdkInit.di.AppComponent
import com.rf.baseSideNav.ui.base.BaseActivityModule
import com.rf.baseSideNav.ui.base.BaseViewModelFactory
import com.rf.baseSideNav.ui.main.activity.DashBoardActivity
import com.rf.baseSideNav.ui.main.viewmodel.DashBoardViewModel
import com.rf.baseSideNav.utils.ActivityScope
import com.rf.baseSideNav.utils.ApplicationContext
import com.rf.baseSideNav.utils.SharedPreference
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