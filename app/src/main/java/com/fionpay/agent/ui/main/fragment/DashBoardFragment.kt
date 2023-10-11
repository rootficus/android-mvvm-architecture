package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentDashboardBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.DaggerDashBoardFragmentComponent
import com.fionpay.agent.ui.main.di.DashBoardFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.gson.Gson
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
    private lateinit var dashBoardListAdapter: DashBoardListAdapter
    private var arrayList: ArrayList<TransactionModel> = arrayListOf()
    private var progress = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView()

    }

    private fun setAdapter(responseData: DashBoardItemResponse?) {
        arrayList.add(
            TransactionModel(
                "Today’s Cash In",
              //  "${responseData?.todayCashId}",
                "10,00,0000",
                R.drawable.today_cash_in
            )
        )
        arrayList.add(
            TransactionModel(
                "Today’s Cash Out",
                "${responseData?.todayCashOut}",
                R.drawable.today_cash_out
            )
        )
        arrayList.add(
            TransactionModel(
                "Monthly Cash In",
                "${responseData?.totalCashId}",
                R.drawable.total_cash_in
            )
        )
        arrayList.add(
            TransactionModel(
                "Monthly Cash Out",
                "${responseData?.totalCashOut}",
                R.drawable.total_cash_out
            )
        )
        arrayList.add(
            TransactionModel(
                "Pending Request",
                "${responseData?.totalCashId}",
                R.drawable.home_pending_logo
            )
        )
        arrayList.add(
            TransactionModel(
                "Transactions",
                "${responseData?.totalCashOut}",
                R.drawable.home_transaction_logo
            )
        )
        arrayList.add(
            TransactionModel(
                "Total Setup Modems",
                "${responseData?.totalCashOut}",
                R.drawable.home_modem_logo
            )
        )
        arrayList.add(
            TransactionModel(
                "Active Modems",
                "${responseData?.totalCashOut}",
                R.drawable.home_modem_logo
            )
        )
        dashBoardListAdapter = DashBoardListAdapter(arrayList)
        mDataBinding.homeCardListView.layoutManager = GridLayoutManager(context, 2)
        mDataBinding.homeCardListView.adapter = dashBoardListAdapter
    }

    private fun initializeDagger() {
        DaggerDashBoardFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .dashBoardFragmentModule(DashBoardFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun updateProgressBar(progressValue: Int) {
        mDataBinding.progressBar.progress = progressValue
        mDataBinding.textViewProgress.text = "$progressValue%"
    }

    private fun initializeView() {
        val name = sharedPreference.getFullName() ?: "Agent"
        mDataBinding.textAgentFullName.text = getString(R.string.userName, name)
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

        mDataBinding.settingButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_settingFragment)
        }

        mDataBinding.notificationButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.navigation_notificationFragment)
        }

        if (networkHelper.isNetworkConnected()) {
            viewModel.dashBoardData()
            viewModel.dashBoardItemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        mDataBinding.currentBalanceAmount.text =
                            "৳${it.data?.currentBalance.toString()}"
                        val gson = Gson()
                        val json = gson.toJson(it.data)
                        viewModel.setDashBoardDataModel(json)
                        updateProgressBar(45)
                        setAdapter(it.data)

                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        val gson = Gson()
                        val json: String? = viewModel.getDashBoardDataModel()
                        val obj: DashBoardItemResponse =
                            gson.fromJson(json, DashBoardItemResponse::class.java)
                        setAdapter(obj)
                        if (it.message == "Invalid access token") {
                            sessionExpired()
                        }
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            val gson = Gson()
            val json: String? = viewModel.getDashBoardDataModel()
            val obj: DashBoardItemResponse =
                gson.fromJson(json, DashBoardItemResponse::class.java)
            setAdapter(obj)
        }
    }

}