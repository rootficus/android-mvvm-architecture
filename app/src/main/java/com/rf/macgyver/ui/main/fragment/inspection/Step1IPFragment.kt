package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.InspectionFormData
import com.rf.macgyver.databinding.AlertCamIpBinding
import com.rf.macgyver.databinding.FragmentStep1IpBinding
import com.rf.macgyver.databinding.PopupStep2IpBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep1IPFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step1IPFragment : BaseFragment<FragmentStep1IpBinding>(R.layout.fragment_step1_ip) {

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

        // Check if the bundle is not null
        if (bundle != null) {
            // Retrieve the Transaction object from the bundle
            val inspectionFormData: InspectionFormData? =
                bundle.getSerializable("inspectionFormData") as? InspectionFormData

            // Now you can use the transaction object as needed
            if (inspectionFormData != null) {
                // Do something with the transaction object
            }
        }

        mDataBinding.nextTxt.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_step1_inspection_to_navigation_step3_inspection)
        }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.cancelTxt.setOnClickListener {
            navController.navigateUp()
        }

        mDataBinding.bearingsPlusBtn.setOnClickListener{
            val text = mDataBinding.bearingsHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.smellPlusBtn.setOnClickListener{
            val text = mDataBinding.smellHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.engineStartUpPlusBtn.setOnClickListener{
            val text = mDataBinding.engineHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.vibrationPlusBtn.setOnClickListener{
            val text = mDataBinding.vibrationHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.vibrationPlusBtn.setOnClickListener{
            val text = mDataBinding.vibrationHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.hatchStartUpPlusBtn.setOnClickListener{
            val text = mDataBinding.hatchSHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.soundsPlusBtn.setOnClickListener{
            val text = mDataBinding.soundsHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.spinningPlusBtn.setOnClickListener{
            val text = mDataBinding.spinningHeading.text.toString()
            initializePlusButtonAlert(text)
        }

        mDataBinding.coolantPlusBtn.setOnClickListener{
            val text = mDataBinding.coolantHeading.text.toString()
            initializePlusButtonAlert(text)
        }
        mDataBinding.radiatorCamBtn.setOnClickListener{
            initializeCamAlert()
        }

        mDataBinding.beltsCamBtn.setOnClickListener{
            initializeCamAlert()
        }
    }

    private fun initializeDagger() {
        DaggerStep1IPFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializePlusButtonAlert(text :String){

        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = PopupStep2IpBinding.inflate(layoutInflater)
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
        val view = AlertCamIpBinding.inflate(layoutInflater)
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