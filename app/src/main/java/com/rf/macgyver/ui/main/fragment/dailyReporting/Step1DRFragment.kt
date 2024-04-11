package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.AddVehicleAlertBinding
import com.rf.macgyver.databinding.FragmentStep1DRBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep1DRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Step1DRFragment : BaseFragment<FragmentStep1DRBinding>(R.layout.fragment_step1_d_r) {

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
        mDataBinding.vehicleSelectTxt.setOnClickListener {
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())

            val view = AddVehicleAlertBinding.inflate(layoutInflater)
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()
        }

        mDataBinding.nextTxt.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_step1_report_to_navigation_step2_report)
        }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.cancelTxt.setOnClickListener {
            navController.navigateUp()
        }

        val currentDate = getCurrentDate()
        mDataBinding.dateHeading.text = "Date : $currentDate"
    }


    private fun initializeDagger() {
        DaggerStep1DRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }


    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}