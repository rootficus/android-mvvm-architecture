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
import com.fionpay.agent.databinding.FragmentSettingBinding
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import javax.inject.Inject


class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private lateinit var dashBoardListAdapter : DashBoardListAdapter
    private var arrayList : ArrayList<TransactionModel> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {

        mDataBinding.topHeader.txtHeader.text = "Edit Profile"
        mDataBinding.btnUpdate.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_editProfileFragment_to_navigation_settingFragment)
        }
    }
    private fun initializeDagger() {
      /*  DaggerNotificationFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .dashBoardFragmentModule(NotificationFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)*/
    }

    fun sessionExpired(){
        val mBuilder = AlertDialog.Builder(activity)
            .setTitle("Session Expired")
            .setMessage("your session has expired.\n\nYou will be redirected to login page.")
            .setPositiveButton("Ok", null)
            .show()
        val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            startActivity(Intent(activity, SignInActivity::class.java))
            activity?.finishAffinity()
        }
    }

}