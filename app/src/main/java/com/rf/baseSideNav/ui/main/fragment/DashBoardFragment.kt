package com.rf.baseSideNav.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.rf.baseSideNav.R
import com.rf.baseSideNav.databinding.FragmentDashboardBinding
import com.rf.baseSideNav.sdkInit.UtellSDK
import com.rf.baseSideNav.ui.base.BaseFragment
import com.rf.baseSideNav.ui.base.BaseFragmentModule
import com.rf.baseSideNav.ui.base.BaseViewModelFactory
import com.rf.baseSideNav.ui.main.di.DaggerDashBoardFragmentComponent
import com.rf.baseSideNav.ui.main.di.DashBoardFragmentModuleDi
import com.rf.baseSideNav.ui.main.viewmodel.DashBoardViewModel
import com.rf.baseSideNav.utils.NetworkHelper
import com.rf.baseSideNav.utils.SharedPreference
import javax.inject.Inject

class DashBoardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {


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
        initializeView()

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    private fun initializeDagger() {
        DaggerDashBoardFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {/*
        val dataList = listOf(
            Triple("Paneer Tikka", "$2", "Completed"),
            Triple("Soya Chaap", "$3", "Completed"),
            Triple("Soya Chaap", "$3", "Completed"),
            Triple("Soya Chaap", "$3", "Completed"),
            Triple("Soya Chaap", "$3", "Completed")
        )

        val adapter = DashBoardOrderItemAdapter(dataList)
        mDataBinding.recyclerViewId?.layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId?.adapter = adapter*/

    }

}