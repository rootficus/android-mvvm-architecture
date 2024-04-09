package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentInspectionBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerInspectionFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class InspectionFragment : BaseFragment<FragmentInspectionBinding>(R.layout.fragment_inspection) {

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
        mDataBinding.startNewButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_inspection_to_navigation_step1_inspection)
        }
    }

    private fun initializeDagger() {
        DaggerInspectionFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}