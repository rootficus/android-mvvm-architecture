package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.Modem
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
import com.fionpay.agent.utils.Constant
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.android.material.snackbar.Snackbar
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
    private var modemList: ArrayList<Modem> = arrayListOf()
    private var bankList: ArrayList<Bank> = arrayListOf()

    private val onFionModemStatusChangeActionsReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.extras != null) {
                    getDashBoardData()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView()
        activity?.registerReceiver(
            onFionModemStatusChangeActionsReceiver,
            IntentFilter(Constant.MODEM_STATUS_CHANGE_ACTIONS)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.unregisterReceiver(onFionModemStatusChangeActionsReceiver)
    }


    private fun initializeDagger() {
        DaggerDashBoardFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .dashBoardFragmentModule(DashBoardFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        val name = sharedPreference.getFullName() ?: "Agent"
        mDataBinding.textAgentFullName.text = getString(R.string.userName, name)
        mDataBinding.notificationButton.setOnClickListener {
            val mBuilder = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.app_name))
                .setMessage("Do you want to logout?")
                .setPositiveButton(getString(R.string.yes), null)
                .setNegativeButton(getString(R.string.no), null)
                .show()
            val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
            mPositiveButton.setOnClickListener {
                startActivity(Intent(activity, SignInActivity::class.java))
                activity?.finishAffinity()
            }
        }
        getTransactionFilters()
        mDataBinding.settingButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_settingFragment)
        }

        mDataBinding.notificationButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.navigation_notificationFragment)
        }
        getDashBoardData()
    }

    private fun getDashBoardData() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.dashBoardData()
            viewModel.dashBoardItemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        val gson = Gson()
                        val json = gson.toJson(it.data)
                        viewModel.setDashBoardDataModel(json)
                        it.data?.balance?.let { it1 -> viewModel.setBalance(it1) }
                        it.data?.availableBalance?.let { it1 -> viewModel.setAvailableBalance(it1) }
                        it.data?.holdBalance?.let { it1 -> viewModel.setHoldBalance(it1) }
                        setDashBalanceUI()
                        setAdapter(it.data)

                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        val gson = Gson()
                        val json: String? = viewModel.getDashBoardDataModel()
                        val obj: DashBoardItemResponse =
                            gson.fromJson(json, DashBoardItemResponse::class.java)
                        setAdapter(obj)
                        showErrorMessage(it.message)
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

    private fun setDashBalanceUI() {
        mDataBinding.availableBalanceEdit.text = viewModel.getAvailableBalance().toString()
        mDataBinding.totalBalanceEdit.text = viewModel.getBalance().toString()
        mDataBinding.holdBalanceEdit.text = viewModel.getHoldBalance().toString()
    }

    private fun setAdapter(responseData: DashBoardItemResponse?) {
        arrayList.add(
            TransactionModel(
                "Today’s Cash In",
                responseData?.todayCashIn,
                R.drawable.today_cash_in
            )
        )
        arrayList.add(
            TransactionModel(
                "Today’s Cash Out",
                responseData?.todayCashOut,
                R.drawable.today_cash_out
            )
        )
        arrayList.add(
            TransactionModel(
                "Monthly Cash In",
                responseData?.monthlyCashIn,
                R.drawable.total_cash_in
            )
        )
        arrayList.add(
            TransactionModel(
                "Monthly Cash Out",
                responseData?.monthlyCashOut,
                R.drawable.total_cash_out
            )
        )
        arrayList.add(
            TransactionModel(
                "Pending Request",
                responseData?.numberOfPendingRequest?.toFloat(),
                R.drawable.home_pending_logo
            )
        )
        arrayList.add(
            TransactionModel(
                "Transactions", responseData?.rejectedTransaction?.toFloat(),
                R.drawable.home_transaction_logo
            )
        )
        arrayList.add(
            TransactionModel(
                "Total Setup Modems",
                responseData?.totalSetupModems?.toFloat(),
                R.drawable.home_modem_logo
            )
        )
        arrayList.add(
            TransactionModel(
                "Active Modems",
                responseData?.activeModems?.toFloat(),
                R.drawable.home_modem_logo
            )
        )
        dashBoardListAdapter = DashBoardListAdapter(arrayList)
        dashBoardListAdapter.listener = cardListener
        mDataBinding.homeCardListView.layoutManager = GridLayoutManager(context, 2)
        mDataBinding.homeCardListView.adapter = dashBoardListAdapter
    }

    private val cardListener = object : DashBoardListAdapter.CardEvent {
        override fun onCardClicked(transactionModel: TransactionModel) {
            gotoNextFragment(transactionModel.title)
        }
    }

    private fun gotoNextFragment(title: String) {
        if (title.contains("Pending Request", true)) {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_pendingFragment)
        } else if (title.contains("Transactions", true)) {
            Navigation.findNavController(requireView())
                .navigate(R.id.navigation_transactionFragment)
        } else if (title.contains("Modems", true)) {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_modemFragment)
        }


    }

    private fun getTransactionFilters() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getTransactionFilters()
            viewModel.getTransactionFiltersResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        it.data?.modems?.let { it1 -> modemList.addAll(it1) }
                        it.data?.banks?.let { it1 -> bankList.addAll(it1) }
                        modemList.forEach { modem ->
                            viewModel.insertModems(modem)
                        }
                        bankList.forEach { bank ->
                            viewModel.insertBanks(bank)
                        }
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            Snackbar.make(requireView(), "No Internet", Snackbar.LENGTH_LONG).show()
        }

    }


}