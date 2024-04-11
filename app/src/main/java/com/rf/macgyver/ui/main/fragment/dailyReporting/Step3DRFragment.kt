package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentStep3DRBinding
import com.rf.macgyver.databinding.SuccessAlertDrBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep3DRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step3DRFragment : BaseFragment<FragmentStep3DRBinding>(R.layout.fragment_step3_d_r) {


    private var downtimeData = arrayOf("hours", "minutes")
    private var runtimeData = arrayOf("hours", "minutes")
    private var worklogData = arrayOf("hours", "minutes")

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
        mDataBinding.generateReportTxt.setOnClickListener {

            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = SuccessAlertDrBinding.inflate(layoutInflater)
            mBuilder.setView(view.root)
            val dialog: AlertDialog = mBuilder.create()
            view.backTxt.setOnClickListener {
                dialog.dismiss()
            }
            view.viewReportTxt.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backTxt.setOnClickListener {
            navController.navigateUp()
        }

        val spinner1Adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, downtimeData)
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.downtimeSpinner.adapter = spinner1Adapter

        val spinner2Adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, runtimeData)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.runtimeSpinner.adapter = spinner2Adapter

        val spinner3Adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, worklogData)
        spinner3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.worklohSpinner.adapter = spinner3Adapter

    }

    private fun initializeDagger() {
        DaggerStep3DRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}