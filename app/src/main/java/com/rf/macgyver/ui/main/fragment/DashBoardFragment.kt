package com.rf.macgyver.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentDashboardBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.activity.DashBoardActivity
import com.rf.macgyver.ui.main.di.DaggerDashBoardFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class DashBoardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    var uniqueId :String? = null
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
        initializeView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initializeDagger() {
        DaggerDashBoardFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {

       /* val bundle = arguments
        val uniqueId: String? =
            bundle?.getString("uniqueId")*/
        val receivedBundle = (activity as DashBoardActivity).intent.getBundleExtra("bundle")
        if (receivedBundle != null) {
            uniqueId = receivedBundle.getString("uniqueId")
            // Do whatever you need with the data
        }
        if (uniqueId != null) {
            Log.d("uniqueId", uniqueId!!)
        }else{
            Log.d("uniqueId","null")
        }

        val loginDetails = uniqueId?.let { viewModel.getLoginDetailsUsingToken(it) }
        mDataBinding.nameId.text = loginDetails?.username
        val bundle =  Bundle().apply {
            putString("uniqueId", uniqueId)
        }



        mDataBinding.dailyCheckupCard.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_dashboard_to_navigation_daily_report, bundle)
        }

        mDataBinding.inspectionCard.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_dashboard_to_navigation_inspection,bundle)
        }
        mDataBinding.IncidentReport.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_dashboard_to_navigation_incident_report,bundle)
        }
    }
}