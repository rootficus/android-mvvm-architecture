package com.rf.geolgy.ui.main.di

import android.content.Context
import com.rf.geolgy.data.remote.GeolgyApiServices
import com.rf.geolgy.data.repository.DashBoardRepository
import com.rf.geolgy.roomDB.GeolgyDatabase
import com.rf.geolgy.sdkInit.di.AppComponent
import com.rf.geolgy.ui.base.BaseActivityModule
import com.rf.geolgy.ui.base.BaseViewModelFactory
import com.rf.geolgy.ui.main.activity.DashBoardActivity
import com.rf.geolgy.ui.main.viewmodel.DashBoardViewModel
import com.rf.geolgy.utils.ActivityScope
import com.rf.geolgy.utils.ApplicationContext
import com.rf.geolgy.utils.SharedPreference
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
    fun provideApiServices(retrofit: Retrofit): GeolgyApiServices =
        retrofit.create(GeolgyApiServices::class.java)

    @Provides
    @ActivityScope
    fun provideDashBoardRepository(
        apiServices: GeolgyApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        geolgyDatabase: GeolgyDatabase
    ): DashBoardRepository =
        DashBoardRepository(apiServices, context, sharedPreference, geolgyDatabase)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(signInRepository: DashBoardRepository): BaseViewModelFactory<DashBoardViewModel> =
        BaseViewModelFactory { DashBoardViewModel(signInRepository) }
}