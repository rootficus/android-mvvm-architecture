package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.AlertCamDrBinding
import com.rf.macgyver.databinding.FragmentStep2DRBinding
import com.rf.macgyver.databinding.PopupStep2DrBinding
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

    private lateinit var text : String
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

        mDataBinding.airCompressorPlusBtn.setOnClickListener{
            text = mDataBinding.airCompressorHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.motorBearingPlusBtn.setOnClickListener{
            text = mDataBinding.motorBearingHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.coolerTempsPlusBtn.setOnClickListener{
            text = mDataBinding.coolerTempsHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.ispectCouplerPlusBtn.setOnClickListener{
            text = mDataBinding.inspectCouplerHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.engineTempsPlusBtn.setOnClickListener{
            text = mDataBinding.engineTempsHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.coolantTempsPlusBtn.setOnClickListener{
            text = mDataBinding.coolantTempsHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.engineStartUpPlusBtn.setOnClickListener{
            text = mDataBinding.engineHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.vibrationPlusBtn.setOnClickListener{
            text = mDataBinding.vibrationHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.engineCamBtn.setOnClickListener{
            initializeCamAlert()
        }

        mDataBinding.vibrationCamBtn.setOnClickListener{
            initializeCamAlert()
        }

        mDataBinding.smellCamBtn.setOnClickListener{
            initializeCamAlert()
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

    private fun initializePlusButtonAlert(text :String){

        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = PopupStep2DrBinding.inflate(layoutInflater)
        view.headingId.text = text
        mBuilder.setView(view.root)
        val dialog: AlertDialog = mBuilder.create()
        view.cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        view.DoneTxt.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun initializeCamAlert(){
        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = AlertCamDrBinding.inflate(layoutInflater)
        mBuilder.setView(view.root)
        val dialog: AlertDialog = mBuilder.create()
        view.cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        view.doneTxt.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}