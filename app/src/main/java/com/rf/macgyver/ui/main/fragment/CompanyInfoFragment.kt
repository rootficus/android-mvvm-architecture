package com.rf.macgyver.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentCompanyInfoBinding
import com.rf.macgyver.databinding.FragmentSignUpBinding
import com.rf.macgyver.ui.base.BaseFragment

class CompanyInfoFragment : BaseFragment<FragmentCompanyInfoBinding>(R.layout.fragment_company_info) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeVew()
        }

    private fun initializeVew(){
        mDataBinding.nextBtn.setOnClickListener{
            findNavController().navigate(R.id.action_fragment_company_info_to_fragment_start)
        }
    }
}