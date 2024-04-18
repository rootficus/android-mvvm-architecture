package com.rf.macgyver.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentAccountBinding
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.activity.DashBoardActivity
import com.rf.macgyver.ui.main.di.DaggerAccountFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) {

    var uniqueId :String? = null
    var email :String? = null


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
        DaggerAccountFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        val receivedBundle = (activity as DashBoardActivity).intent.getBundleExtra("bundle")
        if (receivedBundle != null) {
            uniqueId = receivedBundle.getString("uniqueId")

        }
        val loginDetails : LoginDetails? = uniqueId?.let { viewModel.getLoginDetailsUsingToken(it) }
        val name : String? = loginDetails?.username
        mDataBinding.adminName.setText(name)
    }
}