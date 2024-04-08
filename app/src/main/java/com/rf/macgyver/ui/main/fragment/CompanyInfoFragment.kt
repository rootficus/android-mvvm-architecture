package com.rf.macgyver.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentCompanyInfoBinding
import com.rf.macgyver.databinding.FragmentSignUpBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerCompanyInfoFragmentComponent
import com.rf.macgyver.ui.main.di.DaggerSignInFragmentComponent
import com.rf.macgyver.ui.main.di.SignInFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class CompanyInfoFragment : BaseFragment<FragmentCompanyInfoBinding>(R.layout.fragment_company_info) {


    @Inject
    lateinit var sharedPreference: SharedPreference

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
        initializeDagger()
        initializeVew()

    }

    private fun initializeDagger() {
        DaggerCompanyInfoFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .signInFragmentModuleDi(SignInFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeVew(){
        mDataBinding.nextBtn.setOnClickListener{
            findNavController().navigate(R.id.action_fragment_company_info_to_fragment_start)
        }
    }
}