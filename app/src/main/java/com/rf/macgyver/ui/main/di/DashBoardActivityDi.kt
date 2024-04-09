package com.rf.macgyver.ui.main.di

import android.content.Context
import com.rf.macgyver.data.remote.MagApiServices
import com.rf.macgyver.data.repository.DashBoardRepository
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.sdkInit.di.AppComponent
import com.rf.macgyver.ui.base.BaseActivityModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.activity.DashBoardActivity
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
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
    modules = [DashBoardActivityModule::class, BaseActivityModule::class]
)
interface DashBoardActivityComponent {
    fun inject(dashBoardActivity: DashBoardActivity)
}

@Module
class DashBoardActivityModule {

    @Provides
    @ActivityScope
    fun provideApiServices(retrofit: Retrofit): MagApiServices =
        retrofit.create(MagApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideDashBoardRepository(
        apiServices: MagApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        magDatabase: MagDatabase
    ): DashBoardRepository =
        DashBoardRepository(apiServices, context, sharedPreference, magDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: DashBoardRepository): BaseViewModelFactory<DashBoardViewModel> =
        BaseViewModelFactory { DashBoardViewModel(signInRepository) }
}