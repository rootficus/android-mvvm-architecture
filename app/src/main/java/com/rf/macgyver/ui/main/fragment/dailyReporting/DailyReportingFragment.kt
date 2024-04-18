package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.dailyReportData.Step1DrData
import com.rf.macgyver.data.model.request.dailyReportData.Step3DrData
import com.rf.macgyver.data.model.request.dailyReportData.Vehicle
import com.rf.macgyver.databinding.FragmentDailyReportingBinding
import com.rf.macgyver.roomDB.model.DailyReporting
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.adapter.DailyReportingItemAdapter
import com.rf.macgyver.ui.main.di.DaggerDailyReportingFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject


class DailyReportingFragment  : BaseFragment<FragmentDailyReportingBinding>(R.layout.fragment_daily_reporting) {

    private var dataList: ArrayList<Step1DrData?> = arrayListOf()
    @Inject
    lateinit var sharedPreference: SharedPreference

    var uniqueId: String? = null
    private val step1Data: Step1DrData = Step1DrData()
    val vehicle: Vehicle = Vehicle()

    private lateinit var itemTabLayout: TabLayout

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewmodel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private var isPasswordVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView()

    }

    private fun initializeView() {
        val bundle = arguments
        uniqueId =
            bundle?.getString("uniqueId")
        if (uniqueId != null) {
            Log.d("uniqueId", uniqueId!!)
        }else{
            Log.d("uniqueId","null")
        }
        val dailyReportingList = uniqueId?.let { viewmodel.getDailyReportingData(it) }
        val dailyReportingDataList  = dailyReportingList?.let { ArrayList(it) }
        val navController = Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backArrowBtn.setOnClickListener{
            navController.navigateUp()
        }
        if (dailyReportingDataList != null) {
            for (i in 0 until dailyReportingDataList.size){
                step1Data.reportName = dailyReportingDataList[i]?.reportName
                step1Data.date = dailyReportingDataList[i]?.date
                step1Data.day = dailyReportingDataList[i]?.day
                step1Data.name = dailyReportingDataList[i]?.name
                vehicle.vehicleName = dailyReportingDataList[i]?.vehicleName
                vehicle.vehicleNo = dailyReportingDataList[i]?.vehicleNo
                step1Data.vehicle = vehicle
                step1Data.shift = dailyReportingDataList[i]?.shift

                dataList.add(step1Data)
            }
        }


        val itemAdapter = DailyReportingItemAdapter(dataList, requireActivity())
        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter

        mDataBinding.startNewButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_daily_report_to_navigation_step1_report,bundle)
        }

        itemTabLayout = mDataBinding.tabLayout

        itemTabLayout.addTab(itemTabLayout.newTab().setText("Running"))
        itemTabLayout.addTab(itemTabLayout.newTab().setText("Submitted"))
    }

    override fun onResume() {
        super.onResume()

    }

    private fun initializeDagger() {
        DaggerDailyReportingFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}