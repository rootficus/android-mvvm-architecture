package com.rf.macgyver.ui.main.fragment.incidentReport

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentStep2IRBinding
import com.rf.macgyver.databinding.FragmentStep2IpBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep1IRFragmentComponent
import com.rf.macgyver.ui.main.di.DaggerStep2IRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step2IRFragment : BaseFragment<FragmentStep2IRBinding>(R.layout.fragment_step2_i_r) {

    private var incidentSeverityOptions = arrayOf<String>("Fatal Incident")
    private var weatherConditionOptions = arrayOf<String>("Clear")
    private var vehicleActivityOptions = arrayOf<String>("Digging")


    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewmodel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private var isPasswordVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView()

    }

    private fun initializeView() {
        val spinner1Adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, incidentSeverityOptions)
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.incidentSeveritySpinner.adapter = spinner1Adapter

        val spinner2Adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, weatherConditionOptions)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.weatherConditionSpinner.adapter = spinner2Adapter

        val spinner3Adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vehicleActivityOptions)
        spinner3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.vehicleActivitySpinner.adapter = spinner3Adapter
    }

    private fun initializeDagger() {
        DaggerStep2IRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
             .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
             .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}