package com.rf.macgyver.ui.main.di

import android.content.Context
import com.rf.macgyver.data.remote.FionApiServices
import com.rf.macgyver.data.repository.DashBoardRepository
import com.rf.macgyver.roomDB.FionDatabase
import com.rf.macgyver.sdkInit.di.AppComponent
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.fragment.DashBoardFragment
import com.rf.macgyver.ui.main.fragment.dailyReporting.DailyReportingFragment
import com.rf.macgyver.ui.main.fragment.dailyReporting.Step1DRFragment
import com.rf.macgyver.ui.main.fragment.dailyReporting.Step2DRFragment
import com.rf.macgyver.ui.main.fragment.dailyReporting.Step3DRFragment
import com.rf.macgyver.ui.main.fragment.incidentReport.IncidentReportFragment
import com.rf.macgyver.ui.main.fragment.incidentReport.Step1IRFragment
import com.rf.macgyver.ui.main.fragment.incidentReport.Step2IRFragment
import com.rf.macgyver.ui.main.fragment.incidentReport.Step3IRFragment
import com.rf.macgyver.ui.main.fragment.inspection.InspectionFragment
import com.rf.macgyver.ui.main.fragment.inspection.Step1IPFragment
import com.rf.macgyver.ui.main.fragment.inspection.Step2IPFragment
import com.rf.macgyver.ui.main.fragment.inspection.Step3IPFragment
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.ApplicationContext
import com.rf.macgyver.utils.FragmentScope
import com.rf.macgyver.utils.SharedPreference
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
    modules = [DailyReportingFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface DailyReportingFragmentComponent {
    fun inject(dailyReportingFragment: DailyReportingFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class DailyReportingFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step1DRFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step1DRFragmentComponent {
    fun inject(step1DRFragment: Step1DRFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step1DRFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step2DRFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step2DRFragmentComponent {
    fun inject(step2DRFragment: Step2DRFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step2DRFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step3DRFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step3DRFragmentComponent {
    fun inject(step2DRFragment: Step3DRFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step3DRFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step1IRFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step1IRFragmentComponent {
    fun inject(step1IRFragment: Step1IRFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step1IRFragmentModule
@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step2IRFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step2IRFragmentComponent {
    fun inject(step2IRFragment: Step2IRFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step2IRFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step3IRFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step3IRFragmentComponent {
    fun inject(step3IRFragment: Step3IRFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step3IRFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [IncidentReportFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface IncidentReportFragmentComponent {
    fun inject(incidentReportFragment: IncidentReportFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class IncidentReportFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [InspectionFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface InspectionFragmentComponent {
    fun inject(inspectionFragment: InspectionFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class InspectionFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step1IPFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step1IPFragmentComponent {
    fun inject(step1IPFragment: Step1IPFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step1IPFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step2IPFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step2IPFragmentComponent {
    fun inject(step2IPFragment: Step2IPFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step2IPFragmentModule

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [Step3IPFragmentModule::class, DashBoardFragmentModuleDi::class, BaseFragmentModule::class]
)
interface Step3IPFragmentComponent {
    fun inject(step3IPFragment: Step3IPFragment)
}

@Module(includes = [DashBoardFragmentModuleDi::class])
class Step3IPFragmentModule

