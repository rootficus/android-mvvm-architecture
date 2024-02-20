package com.rf.utellRestaurant.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.FragmentNotificationBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.di.DaggerNotificationFragmentComponent
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModuleDi
import com.rf.utellRestaurant.ui.main.di.NotificationFragmentModule
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import javax.inject.Inject


class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

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

    private fun initialization() {
        mDataBinding.topHeader.txtHeader.text = getString(R.string.notifications)
        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun initializeDagger() {
        DaggerNotificationFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

}