package com.rf.accessAli.ui.main.di

import android.content.Context
import com.rf.accessAli.data.remote.AccessAliApiServices
import com.rf.accessAli.data.repository.DashBoardRepository
import com.rf.accessAli.roomDB.AccessAliDatabase
import com.rf.accessAli.sdkInit.di.AppComponent
import com.rf.accessAli.ui.base.BaseActivityModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.activity.DashBoardActivity
import com.rf.accessAli.ui.main.viewmodel.DashBoardViewModel
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
    modules = [DashBoardActivityModule::class, BaseActivityModule::class]
)
interface DashBoardActivityComponent {
    fun inject(dashBoardActivity: DashBoardActivity)
}

@Module
class DashBoardActivityModule {

    @Provides
    @ActivityScope
    fun provideApiServices(retrofit: Retrofit): AccessAliApiServices =
        retrofit.create(AccessAliApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideDashBoardRepository(
        apiServices: AccessAliApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        accessAliDatabase: AccessAliDatabase
    ): DashBoardRepository =
        DashBoardRepository(apiServices, context, sharedPreference, accessAliDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: DashBoardRepository): BaseViewModelFactory<DashBoardViewModel> =
        BaseViewModelFactory { DashBoardViewModel(signInRepository) }
}