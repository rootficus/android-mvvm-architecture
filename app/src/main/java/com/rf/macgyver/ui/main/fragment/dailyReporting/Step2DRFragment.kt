package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.rf.macgyver.R
import com.rf.macgyver.databinding.AddVehicleAlertBinding
import com.rf.macgyver.databinding.FragmentStep2DRBinding
import com.rf.macgyver.databinding.PopupInspectionStep3Binding
import com.rf.macgyver.databinding.PopupStep2DrBinding
import com.rf.macgyver.databinding.PopupStep2IpBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep2DRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step2DRFragment : BaseFragment<FragmentStep2DRBinding>(R.layout.fragment_step2_d_r) {

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

        val binding = PopupStep2DrBinding.inflate(layoutInflater)

        mDataBinding.airCompressorPlusBtn.setOnClickListener{

            val text = mDataBinding.airCompressorHeading.text.toString()
            binding.headingId.setText(text)
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2DrBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.airCompressorHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }
        mDataBinding.motorBearingPlusBtn.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2DrBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.motorBearingHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }
        mDataBinding.coolerTempsPlusBtn.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2DrBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.coolerTempsHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }
        mDataBinding.ispectCouplerPlusBtn.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = PopupStep2DrBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.inspectCouplerHeading.text.toString()
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }

        val navController = Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.completedTxt.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_step2_report_to_navigation_step3_report)
        }
        mDataBinding.backTxt.setOnClickListener{
            navController.navigateUp()
        }

    }

    private fun initializeDagger() {
        DaggerStep2DRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}