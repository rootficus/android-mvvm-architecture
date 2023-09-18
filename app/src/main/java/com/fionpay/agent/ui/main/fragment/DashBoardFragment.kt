package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentDashboardBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.BleManagerListAdapter
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.DaggerDashBoardFragmentComponent
import com.fionpay.agent.ui.main.di.DashBoardFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import javax.inject.Inject


class DashBoardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

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
        initializeView()
        setAdapter()
    }

    private fun setAdapter() {
        arrayList.add(TransactionModel("Used Balance","৳35,00.00"))
        arrayList.add(TransactionModel("Today’s Cash Out","৳100.00"))
        arrayList.add(TransactionModel("Total Cash In","৳2000.00"))
        arrayList.add(TransactionModel("Total Cash Out","৳15,00.00"))
        dashBoardListAdapter = DashBoardListAdapter(arrayList)
        mDataBinding.homeCardListView.layoutManager = GridLayoutManager(context, 2)
        mDataBinding.homeCardListView.adapter = dashBoardListAdapter
    }

    private fun initializeDagger() {
        DaggerDashBoardFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .dashBoardFragmentModule(DashBoardFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        val name =  sharedPreference.getFullName() ?: "Akash"
        mDataBinding.textAgentFullName.text = "Hello, $name"
        mDataBinding.notificationButton.setOnClickListener {
            val mBuilder = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.app_name))
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .show()
            val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
            mPositiveButton.setOnClickListener {
                startActivity(Intent(activity, SignInActivity::class.java))
                activity?.finishAffinity()
            }
        }
        if (networkHelper.isNetworkConnected()) {
            viewModel.dashBoardData()
            viewModel.dashBoardItemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        it.data?.totalModem?.let { it1 ->
                            viewModel.setTotalModem(it1)
                        }
                        it.data?.todayTrxAmount?.let { it1 ->
                            viewModel.setTodayTrxAmount(it1.toString())
                        }
                        it.data?.totalTrxAmount?.let { it1 ->
                            viewModel.setTotalTrxAmount(it1.toString())
                        }
                        it.data?.todayTransaction?.let { it1 ->
                            viewModel.setTodayTransactions(it1)
                        }
                        it.data?.totalTransaction?.let { it1 ->
                            viewModel.setTotalTransactions(it1.toString())
                        }
                        it.data?.totalPending?.let { it1 ->
                            viewModel.setTotalPending(it1)
                        }
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        if(it.message == "Invalid access token"){
                            sessionExpired()
                        }
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        }
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