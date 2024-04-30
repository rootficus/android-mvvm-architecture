package com.rf.macgyver.ui.main.fragment.incidentReport

import PdfGeneratorIR.createIRPdf
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.incidentReportData.Step1IRData
import com.rf.macgyver.data.model.request.incidentReportData.Step2IRData
import com.rf.macgyver.data.model.request.incidentReportData.Step3IRData
import com.rf.macgyver.databinding.FragmentStep3IRBinding
import com.rf.macgyver.databinding.SuccessAlertIrBinding
import com.rf.macgyver.roomDB.model.IncidentReport
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep3IRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step3IRFragment : BaseFragment<FragmentStep3IRBinding>(R.layout.fragment_step3_i_r) {

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
        val step2Data: Step2IRData? =
            bundle?.getSerializable("step2IrData") as? Step2IRData

        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backTxt.setOnClickListener {
            navController.navigateUp()
        }

        mDataBinding.generateReportTxt.setOnClickListener {
            val incidentCause = mDataBinding.card1EditText.text.toString()
            val equipmentDamage = mDataBinding.card2EditText.text.toString()
            val comment = mDataBinding.card3EditText.text.toString()
            if (incidentCause.isEmpty() || equipmentDamage.isEmpty()) {
                Toast.makeText(context, "Enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                val step3IrData = Step3IRData(
                    incidentCause = incidentCause,
                    damages = equipmentDamage,
                    additionalComment = comment
                )
                val mBuilder = AlertDialog.Builder(requireActivity())
                val view = SuccessAlertIrBinding.inflate(layoutInflater)
                mBuilder.setView(view.root)
                val dialog: AlertDialog = mBuilder.create()
                dialog.show()
                view.backTxt.setOnClickListener {
                    dialog.dismiss()
                }
                view.viewReportTxt.setOnClickListener {
                    val entity = step1Data?.incidentNo?.let { it1 ->
                            IncidentReport(uniqueToken = uniqueToken,incidentNo = it1 , incidentDate = step1Data.incidentDate,incidentTime = step1Data.incidentTime,
                                typeOfIncident = step1Data.typeOfIncident, incidentLocation = step1Data.incidentLocation, vehicleNo = step1Data.vehicleNo ,
                                vehicleName = step1Data.vehicleName, operatorName = step1Data.operatorName, incidentArea = step2Data?.incidentArea,
                                incidentSeverity = step2Data?.incidentSeverity, weatherCondition = step2Data?.weatherCondition, vehicleActivity = step2Data?.vehicleActivity,
                                incidentCause = step3IrData.incidentCause, damagesList = step3IrData.damages, additionalComment = step3IrData.additionalComment)
                    }
                    if (entity != null) {
                        viewmodel.insertIncidentReport(entity)
                    }
                    val bundle1 = Bundle().apply {
                        putString("uniqueId", uniqueToken)
                    }
                    if (entity != null) {
                        createIRPdf(requireActivity(), entity)
                    }

                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_step3_incident_to_navigation_incident,bundle1)
                    dialog.dismiss()
                }
            }
        }
    }
    private fun initializeDagger() {
        DaggerStep3IRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}