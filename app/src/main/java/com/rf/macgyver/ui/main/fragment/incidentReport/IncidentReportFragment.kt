package com.rf.macgyver.ui.main.fragment.incidentReport

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.incidentReportData.Step1IRData
import com.rf.macgyver.databinding.FragmentIncidentReportBinding
import com.rf.macgyver.roomDB.model.IncidentReport
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.adapter.IncidentReportItemAdapter
import com.rf.macgyver.ui.main.di.DaggerIncidentReportFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject


class IncidentReportFragment :
    BaseFragment<FragmentIncidentReportBinding>(R.layout.fragment_incident_report) {

        private var dataList:ArrayList<Step1IRData> = arrayListOf()

    @Inject
    lateinit var sharedPreference: SharedPreference

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
        val uniqueToken : String? =
            bundle?.getString("uniqueId")
        val incidentReportList  = uniqueToken?.let { viewmodel.getIncidentReport(it) }
        val incidentReportDatalist : ArrayList<IncidentReport>? = incidentReportList?.let { ArrayList(it) }

        val step1IRData = Step1IRData()
        if (incidentReportDatalist != null) {
            for(i in 0 until incidentReportDatalist.size){
                step1IRData.incidentNo = incidentReportDatalist[i].incidentNo
                step1IRData.incidentDate = incidentReportDatalist[i].incidentDate
                step1IRData.incidentLocation = incidentReportDatalist[i].incidentLocation
                step1IRData.vehicleName = incidentReportDatalist[i].vehicleName
                step1IRData.vehicleNo = incidentReportDatalist[i].vehicleNo
                step1IRData.operatorName = incidentReportDatalist[i].operatorName

                dataList.add(step1IRData)
            }
        }
        mDataBinding.createReportButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_incident_report_to_navigation_step1_incident, bundle)
        }

        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backArrowBtn.setOnClickListener {
            navController.navigateUp()
        }



        val itemAdapter = IncidentReportItemAdapter(dataList, requireActivity())
        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter
    }

    private fun initializeDagger() {
        DaggerIncidentReportFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}