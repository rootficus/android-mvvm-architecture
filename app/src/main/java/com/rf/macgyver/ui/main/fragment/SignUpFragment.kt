package com.rf.macgyver.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentDashboardBinding
import com.rf.macgyver.databinding.FragmentSignUpBinding
import com.rf.macgyver.ui.base.BaseFragment

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView(){
        mDataBinding.registerBtn.setOnClickListener{
            findNavController().navigate(R.id.action_fragment_signup_to_fragment_company_info)
        }
    }
}