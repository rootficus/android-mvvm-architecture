package com.rf.macgyver.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentSignUpBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.utils.Utility

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
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
                findNavController().navigate(R.id.action_fragment_signup_to_fragment_company_info)
            }
        }
    }
}