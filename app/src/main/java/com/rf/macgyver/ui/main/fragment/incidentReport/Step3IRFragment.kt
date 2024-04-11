package com.rf.macgyver.ui.main.fragment.incidentReport

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentStep3IRBinding
import com.rf.macgyver.databinding.SuccessAlertIpBinding
import com.rf.macgyver.databinding.SuccessAlertIrBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep2IRFragmentComponent
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
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backTxt.setOnClickListener {
            navController.navigateUp()
        }

        mDataBinding.backOrGenerateCard.setOnClickListener {

            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = SuccessAlertIrBinding.inflate(layoutInflater)
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

    }

    private fun initializeDagger() {
        DaggerStep3IRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}