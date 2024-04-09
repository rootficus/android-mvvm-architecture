package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentStep2IpBinding
import com.rf.macgyver.databinding.PopupInspectionStep3Binding
import com.rf.macgyver.databinding.PopupStep2IpBinding
import com.rf.macgyver.databinding.SuccessAlertDrBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep2IPFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject


class Step2IPFragment  : BaseFragment<FragmentStep2IpBinding>(R.layout.fragment_step2_ip) {

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
        val navController = Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)
        val binding = PopupStep2IpBinding.inflate(layoutInflater)


        mDataBinding.completedTxt.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_step2_inspection_to_navigation_step3_inspection)
        }
        mDataBinding.backTxt.setOnClickListener{
            navController.navigateUp()
        }

        mDataBinding.airCompressorPlusBtn.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.airCompressorHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }
        mDataBinding.motorBearingPlusBtn.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.motorBearingHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }
        mDataBinding.coolerTempsPlusBtn.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.coolerTempsHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }
        mDataBinding.ispectCouplerPlusBtn.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.inspectCouplerHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }

    }

    private fun initializeDagger() {
        DaggerStep2IPFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}