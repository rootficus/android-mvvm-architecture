package com.rf.macgyver.ui.main.fragment.incidentReport

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.incidentReportData.Step1IRData
import com.rf.macgyver.databinding.FragmentStep1IRBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep1IRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step1IRFragment : BaseFragment<FragmentStep1IRBinding>(R.layout.fragment_step1_i_r) {


    private lateinit var text :String

    private val checkBoxList = arrayListOf<String>()
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
        val uniqueToken : String? = bundle?.getString("uniqueId")
        mDataBinding.collisionCB.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.collisionCB.text.toString()
            updateList(text, isChecked)
        }
        mDataBinding.chemicalSpillsCB.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.chemicalSpillsCB.text.toString()
            updateList(text, isChecked)
        }

        mDataBinding.brakeFailureCB.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.brakeFailureCB.text.toString()
            updateList(text, isChecked)
        }

        var step1IRData : Step1IRData = Step1IRData()
        mDataBinding.nextTxt.setOnClickListener {
            val incidentNo = mDataBinding.etIncidentNoId.text.toString()
            val incidentDate = mDataBinding.etIncidentDateId.text.toString()
            val incidentTime = mDataBinding.etIncidentTimeId.text.toString()
            val incidentLocation = mDataBinding.etIncidentLocationId.text.toString()
            val vehicleNo = mDataBinding.etVehicleNo.text.toString()
            val vehicleName = mDataBinding.etVehicleName.text.toString()
            val operatorName = mDataBinding.etOperatorNameId.text.toString()

            if(incidentNo.isEmpty() || incidentDate.isEmpty() || incidentTime.isEmpty() || incidentLocation.isEmpty() || vehicleNo.isEmpty() ||
                vehicleName.isEmpty() || operatorName.isEmpty() || checkBoxList.isEmpty()){
                Toast.makeText(context, "Enter all the details", Toast.LENGTH_SHORT).show()
            }else {

                step1IRData = Step1IRData(
                    incidentNo = incidentNo
                    ,incidentDate = incidentDate
                    ,incidentTime = incidentTime
                    ,incidentLocation = incidentLocation
                    ,vehicleNo = vehicleNo
                    ,vehicleName = vehicleName
                    ,operatorName = operatorName
                    , typeOfIncident = checkBoxList)

                val bundle1 = Bundle().apply {
                    putSerializable("step1IrData", step1IRData)
                    putString("uniqueToken", uniqueToken)
                }

                Navigation.findNavController(requireView())
                    .navigate(
                        R.id.action_navigation_step1_incident_to_navigation_step2_incident,
                        bundle1
                    )
            }
        }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.cancelTxt.setOnClickListener {
            navController.navigateUp()
        }

    }

    private fun initializeDagger() {
        DaggerStep1IRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun updateList(text: String, isChecked: Boolean) {
        if (isChecked) {
            checkBoxList.add(text)
        } else {
            checkBoxList.remove(text)
        }
    }
}