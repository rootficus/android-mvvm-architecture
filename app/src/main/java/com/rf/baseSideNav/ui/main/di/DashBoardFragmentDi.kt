package com.rf.baseSideNav.ui.main.di

import android.content.Context
import com.rf.baseSideNav.data.remote.FionApiServices
import com.rf.baseSideNav.data.repository.DashBoardRepository
import com.rf.baseSideNav.roomDB.FionDatabase
import com.rf.baseSideNav.sdkInit.di.AppComponent
import com.rf.baseSideNav.ui.base.BaseFragmentModule
import com.rf.baseSideNav.ui.base.BaseViewModelFactory
import com.rf.baseSideNav.ui.main.fragment.DashBoardFragment
import com.rf.baseSideNav.ui.main.viewmodel.DashBoardViewModel
import com.rf.baseSideNav.utils.ApplicationContext
import com.rf.baseSideNav.utils.FragmentScope
import com.rf.baseSideNav.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class DashBoardFragmentModuleDi {

    @Provides
    @FragmentScope
    fun provideApiServices(retrofit: Retrofit): FionApiServices =
        retrofit.create(FionApiServices::class.java)

    @Provides
    @FragmentScope
    fun provideDashBoardRepository(
        apiServices: FionApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        fionDatabase: FionDatabase
    ): DashBoardRepository =
        DashBoardRepository(apiServices, context, sharedPreference, fionDatabase)

    @FragmentScope
    @Provides
    fun provideViewModelFactory(dashBoardRepository: DashBoardRepository): BaseViewModelFactory<DashBoardViewModel> =
        BaseViewModelFactory { DashBoardViewModel(dashBoardRepository) }
}

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [DashBoardFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface DashBoardFragmentComponent {
    fun inject(signInFragment: DashBoardFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class DashBoardFragmentModule


