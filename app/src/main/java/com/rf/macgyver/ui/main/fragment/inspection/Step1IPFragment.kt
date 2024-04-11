package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.InspectionFormData
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
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.bearingsHeading.text.toString()
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
        mDataBinding.engineStartUpPlusBtn.setOnClickListener{
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.engineHeading.text.toString()
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
        mDataBinding.vibrationPlusBtn.setOnClickListener{
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.vibrationHeading.text.toString()
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
        mDataBinding.vibrationPlusBtn.setOnClickListener{
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.vibrationHeading.text.toString()
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
        mDataBinding.hatchStartUpPlusBtn.setOnClickListener{
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.hatchSHeading.text.toString()
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
        mDataBinding.soundsPlusBtn.setOnClickListener{
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.soundsHeading.text.toString()
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
        mDataBinding.spinningPlusBtn.setOnClickListener{
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.spinningHeading.text.toString()
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
        mDataBinding.coolantPlusBtn.setOnClickListener{
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = PopupStep2IpBinding.inflate(layoutInflater)
            view.headingId.text = mDataBinding.coolantHeading.text.toString()
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
    }

    private fun initializeDagger() {
        DaggerStep1IPFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}