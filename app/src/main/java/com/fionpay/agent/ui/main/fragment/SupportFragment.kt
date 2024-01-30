package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentSupportBinding
import com.fionpay.agent.databinding.ModemBottomSheetBinding
import com.fionpay.agent.databinding.SupportBottomSheetBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.DaggerSupportFragmentComponent
import com.fionpay.agent.ui.main.di.SupportFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject


class SupportFragment : BaseFragment<FragmentSupportBinding>(R.layout.fragment_support) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initializeDagger() {
        DaggerSupportFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .supportFragmentModule(SupportFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
    private fun initialization() {
        mDataBinding.addSupport.setOnClickListener {
            val dialog = BottomSheetDialog(requireActivity())
            val refundBottomSheetBinding = SupportBottomSheetBinding.inflate(layoutInflater)
            setBottomSheetUI(refundBottomSheetBinding, dialog)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(refundBottomSheetBinding.root)
            dialog.show()
        }
    }


    private fun setBottomSheetUI(
        refundBottomSheetBinding: SupportBottomSheetBinding,
        dialog: BottomSheetDialog
    ) {

    }
}