package com.rf.tiffinService.ui.main.di

import android.content.Context
import com.rf.tiffinService.data.remote.FionApiServices
import com.rf.tiffinService.data.repository.DashBoardRepository
import com.rf.tiffinService.roomDB.FionDatabase
import com.rf.tiffinService.sdkInit.di.AppComponent
import com.rf.tiffinService.ui.base.BaseActivityModule
import com.rf.tiffinService.ui.base.BaseViewModelFactory
import com.rf.tiffinService.ui.main.activity.DashBoardActivity
import com.rf.tiffinService.ui.main.viewmodel.DashBoardViewModel
import com.rf.tiffinService.utils.ActivityScope
import com.rf.tiffinService.utils.ApplicationContext
import com.rf.tiffinService.utils.SharedPreference
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