package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentSettingBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.DaggerSettingFragmentComponent
import com.fionpay.agent.ui.main.di.SettingFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
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
    private lateinit var dashBoardListAdapter: DashBoardListAdapter
    private var arrayList: ArrayList<TransactionModel> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initializeDagger() {
        DaggerSettingFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .settingFragmentModule(SettingFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {

        mDataBinding.topHeader.txtHeader.text = getString(R.string.settings)
        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        mDataBinding.textName.text = viewModel.getFullName()
        mDataBinding.textEmail.text = viewModel.getEmail()

        val profileImage = viewModel.getProfileImage()

        Glide.with(this)
            .load(profileImage)
            .error(profileImage)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)) // Optional caching strategy
            .into(mDataBinding.userImage)

        mDataBinding.editProfileButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.navigation_editProfileFragment)
        }
        mDataBinding.logOutCard.setOnClickListener {
            verifyLogout()
        }
    }

    private fun verifyLogout() {
        val mBuilder = android.app.AlertDialog.Builder(activity)
            .setTitle("Log Out")
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton("Yes", null)
            .setNegativeButton("No", null)
            .show()
        val mPositiveButton = mBuilder.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            mBuilder.dismiss()
            sharedPreference.resetSharedPref()
            startActivity(Intent(activity, SignInActivity::class.java))
            activity?.finishAffinity()
        }
    }
}