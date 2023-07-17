package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jionex.agent.R
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.databinding.FragmentSignInBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.di.DaggerDashBoardActivityComponent
import com.jionex.agent.ui.main.di.DaggerDashBoardFragmentComponent
import com.jionex.agent.ui.main.di.DaggerSignInFragmentComponent
import com.jionex.agent.ui.main.di.DashBoardFragmentModule
import com.jionex.agent.ui.main.di.SignInFragmentModule
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.ui.main.viewmodel.SignInViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
import javax.inject.Inject


class DashBoardFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_dashboard) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewmodel: SignInViewModel by activityViewModels { dashBoardViewModelFactory }


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