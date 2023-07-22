package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.jionex.agent.R
import com.jionex.agent.databinding.FragmentDashboardBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.di.DaggerDashBoardFragmentComponent
import com.jionex.agent.ui.main.di.DashBoardFragmentModule
import com.jionex.agent.viewmodel.DashBoardViewModel
import com.jionex.agent.viewmodel.SignInViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
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
        initializeView(view)
    }

    private fun initializeDagger() {
        DaggerDashBoardFragmentComponent.builder().appComponent(JionexSDK.appComponent)
            .dashBoardFragmentModule(DashBoardFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(view: View) {

    }

}