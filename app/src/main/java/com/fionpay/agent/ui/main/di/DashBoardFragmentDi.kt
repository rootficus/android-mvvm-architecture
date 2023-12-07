package com.fionpay.agent.ui.main.di

import android.content.Context
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.data.repository.DashBoardRepository
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.sdkInit.di.AppComponent
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.fragment.AddModemBalanceFragment
import com.fionpay.agent.ui.main.fragment.AddModemFragment
import com.fionpay.agent.ui.main.fragment.B2BFragment
import com.fionpay.agent.ui.main.fragment.BLManagerFragment
import com.fionpay.agent.ui.main.fragment.BalanceManagerFragment
import com.fionpay.agent.ui.main.fragment.ConfirmModemFragment
import com.fionpay.agent.ui.main.fragment.DashBoardFragment
import com.fionpay.agent.ui.main.fragment.EditProfileFragment
import com.fionpay.agent.ui.main.fragment.ModemFragment
import com.fionpay.agent.ui.main.fragment.NotificationFragment
import com.fionpay.agent.ui.main.fragment.PendingFragment
import com.fionpay.agent.ui.main.fragment.SMSInboxFragment
import com.fionpay.agent.ui.main.fragment.SettingFragment
import com.fionpay.agent.ui.main.fragment.SupportFragment
import com.fionpay.agent.ui.main.fragment.TransactionFragment
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

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [NotificationFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface NotificationFragmentComponent {
    fun inject(notificationFragment: NotificationFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class NotificationFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [AddModemFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface AddModemFragmentComponent {
    fun inject(addModemFragment: AddModemFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class AddModemFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [EditProfileFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface EditProfileFragmentComponent {
    fun inject(editProfileFragment: EditProfileFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class EditProfileFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [SettingFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface SettingFragmentComponent {
    fun inject(settingFragment: SettingFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class SettingFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [AddModemBalanceFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface AddModemBalanceFragmentComponent {
    fun inject(addModemBalanceFragment: AddModemBalanceFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class AddModemBalanceFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ConfirmModemFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface ConfirmModemFragmentComponent {
    fun inject(confirmModemFragment: ConfirmModemFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class ConfirmModemFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [PendingFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface PendingFragmentComponent {
    fun inject(pendingFragment: PendingFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class PendingFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [SupportFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface SupportFragmentComponent {
    fun inject(supportFragment: SupportFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class SupportFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [TransactionFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface TransactionFragmentComponent {
    fun inject(transactionFragment: TransactionFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class TransactionFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [BalanceManageFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface BalanceManagerFragmentComponent {
    fun inject(balanceManagerFragment: BalanceManagerFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class BalanceManageFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [B2BFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface B2BFragmentComponent {
    fun inject(b2BFragment: B2BFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class B2BFragmentModule
