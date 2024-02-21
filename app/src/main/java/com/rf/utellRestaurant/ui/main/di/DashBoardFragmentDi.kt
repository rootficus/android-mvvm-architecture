package com.rf.utellRestaurant.ui.main.di

import android.content.Context
import com.rf.utellRestaurant.data.remote.FionApiServices
import com.rf.utellRestaurant.data.repository.DashBoardRepository
import com.rf.utellRestaurant.roomDB.FionDatabase
import com.rf.utellRestaurant.sdkInit.di.AppComponent
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.fragment.DashBoardFragment
import com.rf.utellRestaurant.ui.main.fragment.HistoryFragment
import com.rf.utellRestaurant.ui.main.fragment.NotificationFragment
import com.rf.utellRestaurant.ui.main.fragment.OrderFragment
import com.rf.utellRestaurant.ui.main.fragment.OrderHistoryDescFragment
import com.rf.utellRestaurant.ui.main.fragment.OrdetDescFragment
import com.rf.utellRestaurant.ui.main.fragment.OrdetListFragment
import com.rf.utellRestaurant.ui.main.fragment.SettingFragment
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.ApplicationContext
import com.rf.utellRestaurant.utils.FragmentScope
import com.rf.utellRestaurant.utils.SharedPreference
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
    modules = [HistoryFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface HistoryFragmentComponent {
    fun inject(historyFragment: HistoryFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class HistoryFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [OrderFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface OrderFragmentComponent {
    fun inject(orderFragment: OrderFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class OrderFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [OrderListFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface OrderListFragmentComponent {
    fun inject(orderListFragment: OrdetListFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class OrderListFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [OrderDescFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface OrderDescFragmentComponent {
    fun inject(orderDescFragment: OrdetDescFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class OrderDescFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [OrderHistoryDescFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface OrderHistoryDescFragmentComponent {
    fun inject(orderHistoryDescFragment: OrderHistoryDescFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class OrderHistoryDescFragmentModule

