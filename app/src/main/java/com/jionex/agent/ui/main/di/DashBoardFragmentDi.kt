package com.jionex.agent.ui.main.di

import android.content.Context
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.data.repository.DashBoardRepository
import com.jionex.agent.data.repository.SignInRepository
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.sdkInit.di.AppComponent
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.fragment.BLManagerFragment
import com.jionex.agent.ui.main.fragment.DashBoardFragment
import com.jionex.agent.ui.main.fragment.ModemFragment
import com.jionex.agent.ui.main.fragment.SMSInboxFragment
import com.jionex.agent.ui.main.fragment.SignInFragment
import com.jionex.agent.ui.main.fragment.VerifyPinFragment
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.ui.main.viewmodel.SignInViewModel
import com.jionex.agent.utils.ApplicationContext
import com.jionex.agent.utils.FragmentScope
import com.jionex.agent.utils.SharedPreference
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class DashBoardFragmentModuleDi {

    @Provides
    @FragmentScope
    fun provideApiServices(retrofit: Retrofit): JionexApiServices =
        retrofit.create(JionexApiServices::class.java)

    @Provides
    @FragmentScope
    fun provideDashBoardRepository(
        apiServices: JionexApiServices,
        @ApplicationContext context: Context,
        sharedPreference: SharedPreference,
        jionexDatabase: JionexDatabase
    ): DashBoardRepository = DashBoardRepository(apiServices, context, sharedPreference, jionexDatabase)

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
