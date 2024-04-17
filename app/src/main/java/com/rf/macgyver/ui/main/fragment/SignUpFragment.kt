package com.rf.macgyver.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentSignUpBinding
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerDailyReportingFragmentComponent
import com.rf.macgyver.ui.main.di.DaggerSignUpFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.di.SignInFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel_Factory
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.Utility
import java.util.UUID
import javax.inject.Inject

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val database = context?.let { MagDatabase.getDatabase(it) }
    val dao = database?.magDao()


    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by activityViewModels { signInViewModelFactory }
    private var isPasswordVisible = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        initializeDagger()
    }

    private fun initializeDagger() {
        DaggerSignUpFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .signInFragmentModuleDi(SignInFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(){
        mDataBinding.registerBtn.setOnClickListener{
            val name = mDataBinding.etUsername.text.toString()
            val password = mDataBinding.etPassword.text.toString()
            val email = mDataBinding.etEmail.text.toString()
            if (name.isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_NAME)
                )
            } else if (password.isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_PASSWORD)
                )
            }else if(email.isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_EMAIL))
            }else
            {
                val uniqueId  = UUID.randomUUID().toString().replace("-", "")
                val entity = LoginDetails(username = name , emailId = email , password = password , uniqueToken = uniqueId)
                viewmodel.insertLoginDetails(entity)
                val bundle = Bundle().apply {
                    putString("email",email)
                    putString("uniqueId", uniqueId)
                }

                findNavController().navigate(R.id.action_fragment_signup_to_fragment_company_info,bundle)
            }
        }
    }
}