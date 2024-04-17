package com.rf.macgyver.ui.main.fragment.incidentReport

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.dailyReportData.Step1DrData
import com.rf.macgyver.data.model.request.incidentReportData.Step1IRData
import com.rf.macgyver.data.model.request.incidentReportData.Step2IRData
import com.rf.macgyver.databinding.FragmentStep2IRBinding
import com.rf.macgyver.databinding.FragmentStep2IpBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep1IRFragmentComponent
import com.rf.macgyver.ui.main.di.DaggerStep2IRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step2IRFragment : BaseFragment<FragmentStep2IRBinding>(R.layout.fragment_step2_i_r) {

    private var incidentSeverityOptions = arrayOf<String>("Fatal Incident")
    private var weatherConditionOptions = arrayOf<String>("Clear")
    private var vehicleActivityOptions = arrayOf<String>("Digging")


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
        val uniqueToken : String? = bundle?.getString("uniqueToken")

        val step1Data: Step1IRData? =
            bundle?.getSerializable("step1IrData") as? Step1IRData

        val navController = Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.completedTxt.setOnClickListener{
            val incidentSeverityVal = mDataBinding.incidentSeveritySpinner.selectedItem.toString()
            val weatherCondVal = mDataBinding.weatherConditionSpinner.selectedItem.toString()
            val vehicleActivityVal = mDataBinding.vehicleActivitySpinner.selectedItem.toString()
            val incidentAreaVal = mDataBinding.etIncidentAreaId.text.toString()
            if(incidentAreaVal.isEmpty() || incidentSeverityVal.isEmpty() || weatherCondVal.isEmpty() || vehicleActivityVal.isEmpty()){
                Toast.makeText(context, "Fill all the details", Toast.LENGTH_SHORT).show()
            }else {
                val step2IrData = Step2IRData(
                    incidentArea = incidentAreaVal,
                    incidentSeverity = incidentSeverityVal,
                    vehicleActivity = vehicleActivityVal,
                    weatherCondition = weatherCondVal
                )
                val bundle1 = Bundle().apply{
                    putSerializable("step1IrData", step1Data)
                    putSerializable("step2IrData", step2IrData)
                    putString("uniqueToken",uniqueToken)
                }
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_step2_incident_to_navigation_step3_incident, bundle1)
            }
        }
        mDataBinding.backTxt.setOnClickListener{
            navController.navigateUp()
        }
        val spinner1Adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, incidentSeverityOptions)
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.incidentSeveritySpinner.adapter = spinner1Adapter

        val spinner2Adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, weatherConditionOptions)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.weatherConditionSpinner.adapter = spinner2Adapter

        val spinner3Adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vehicleActivityOptions)
        spinner3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.vehicleActivitySpinner.adapter = spinner3Adapter
    }

    private fun initializeDagger() {
        DaggerStep2IRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
             .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
             .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}