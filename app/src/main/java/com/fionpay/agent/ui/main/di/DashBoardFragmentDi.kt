package com.fionpay.agent.ui.main.di

import android.content.Context
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.data.repository.DashBoardRepository
import com.fionpay.agent.data.repository.SignInRepository
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.sdkInit.di.AppComponent
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.fragment.BLManagerFragment
import com.fionpay.agent.ui.main.fragment.DashBoardFragment
import com.fionpay.agent.ui.main.fragment.ModemFragment
import com.fionpay.agent.ui.main.fragment.SMSInboxFragment
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.ApplicationContext
import com.fionpay.agent.utils.FragmentScope
import com.fionpay.agent.utils.SharedPreference
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
    ): DashBoardRepository = DashBoardRepository(apiServices, context, sharedPreference, fionDatabase)

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


@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [BLManagerFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface BLManagerFragmentComponent {
    fun inject(blManagerFragment: BLManagerFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class BLManagerFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [SMSInboxFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface SMSInboxFragmentComponent {
    fun inject(smsInboxFragment: SMSInboxFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class SMSInboxFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ModemFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface ModemFragmentComponent {
    fun inject(modemFragment: ModemFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class ModemFragmentModule
