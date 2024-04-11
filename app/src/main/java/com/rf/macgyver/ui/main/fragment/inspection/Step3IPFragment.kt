package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentStep3IpBinding
import com.rf.macgyver.databinding.PopupInspectionStep3Binding
import com.rf.macgyver.databinding.SuccessAlertIrBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep3IPFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step3IPFragment : BaseFragment<FragmentStep3IpBinding>(R.layout.fragment_step3_ip) {

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

        mDataBinding.availableBtn.setOnClickListener {
            mDataBinding.priorityLayout.visibility = View.GONE
        }
        mDataBinding.maintenanceRequiredId.setOnClickListener {
            mDataBinding.priorityLayout.visibility = View.VISIBLE
        }
        mDataBinding.availableBtn.setOnClickListener {
            mDataBinding.priorityLayout.visibility = View.VISIBLE
        }
        val popupView =
            LayoutInflater.from(requireContext()).inflate(R.layout.popup_inspection_step3, null)

        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )
        mDataBinding.overallCondSelectId.setOnClickListener {
            val location = IntArray(2)
            mDataBinding.overallCondSelectId.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, x, y)

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
        DaggerStep3IPFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}