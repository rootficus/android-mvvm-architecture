package com.rf.utellRestaurant.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rf.utellRestaurant.ui.main.di.DaggerSettingFragmentComponent
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.FragmentSettingBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.activity.SignInActivity
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModuleDi
import com.rf.utellRestaurant.ui.main.di.SettingFragmentModule
import javax.inject.Inject


class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

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
        DaggerSettingFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {

    }
}