package com.rf.utellRestaurant.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.FragmentDashboardBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.adapter.DashBoardOrderItemAdapter
import com.rf.utellRestaurant.ui.main.di.DaggerDashBoardFragmentComponent
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModule
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModuleDi
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
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

    private fun initializeView() {
        val dataList = listOf(
            Triple("Paneer Tikka", "$2", "Completed"),
            Triple("Soya Chaap", "$3", "Completed"),
            Triple("Soya Chaap", "$3", "Completed"),
            Triple("Soya Chaap", "$3", "Completed"),
            Triple("Soya Chaap", "$3", "Completed")
        )

        val adapter = DashBoardOrderItemAdapter(dataList)
        mDataBinding.recyclerViewId?.layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId?.adapter = adapter
    }


}