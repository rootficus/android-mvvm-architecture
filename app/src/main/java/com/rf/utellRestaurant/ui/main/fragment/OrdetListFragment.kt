package com.rf.utellRestaurant.ui.main.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.FragmentOrderListBinding
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import javax.inject.Inject


class OrdetListFragment: BaseFragment<FragmentOrderListBinding>(R.layout.fragment_order_list) {

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
        /*DaggerOrderListFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure the hosting activity implements the callback interface
    }
    private fun initialization() {
        // Sample list of orders
    }


}