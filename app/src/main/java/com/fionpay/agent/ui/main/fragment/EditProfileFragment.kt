package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentEditProfileBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.DaggerEditProfileFragmentComponent
import com.fionpay.agent.ui.main.di.EditProfileFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import javax.inject.Inject


class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

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

    private fun initialization() {

        mDataBinding.topHeader.txtHeader.text = getString(R.string.edit_profile)
        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        val fullName = viewModel.getFullName()
        val firstName = fullName?.split(' ')?.get(0) ?: " "
        val lastName = fullName?.split(' ')?.get(1) ?: " "
        mDataBinding.etFirstName.setText(firstName)
        mDataBinding.etLastName.setText(lastName)
        mDataBinding.btnUpdate.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_editProfileFragment_to_navigation_settingFragment)
        }
    }

    private fun initializeDagger() {
          DaggerEditProfileFragmentComponent.builder().appComponent(FionSDK.appComponent)
              .editProfileFragmentModule(EditProfileFragmentModule())
              .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}