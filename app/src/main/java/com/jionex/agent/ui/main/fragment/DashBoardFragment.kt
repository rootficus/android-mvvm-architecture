package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.jionex.agent.R
import com.jionex.agent.databinding.FragmentDashboardBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.activity.DashBoardActivity
import com.jionex.agent.ui.main.di.DaggerDashBoardFragmentComponent
import com.jionex.agent.ui.main.di.DashBoardFragmentModule
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
import com.jionex.agent.utils.Utility
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView(view)
        (activity as DashBoardActivity).manageAppTitle("DashBoard","");
    }

    private fun initializeDagger() {
        DaggerDashBoardFragmentComponent.builder().appComponent(JionexSDK.appComponent)
            .dashBoardFragmentModule(DashBoardFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(view: View) {
        showLocalSaveValue()
        if (networkHelper.isNetworkConnected()) {
            viewModel.dashBoardData()
            viewModel.dashBoardItemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        it.data?.totalModem?.let { it1 ->
                            viewModel.setTotalModem(it1);
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
                        showLocalSaveValue()
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        }
    }

    private fun showLocalSaveValue() {
        viewModel.getTotalModem()?.let { it1 ->
            mDataBinding.totalModemValue.text = it1.toString()
            viewModel.setTotalModem(it1);
        }
        viewModel.getTodayTrxAmount()?.let { it1 ->
            mDataBinding.todayTrxAmountValue.text = Utility.convertCurrencyFormat(it1.toDouble())
        }
        viewModel.getTotalTrxAmount()?.let { it1 ->
            mDataBinding.totalTrxAmountValue.text = Utility.convertCurrencyFormat(it1.toDouble())
        }
        viewModel.getTodayTransactions()?.let { it1 ->
            mDataBinding.todayTransactionsValue.text = it1.toString()
        }
        viewModel.getTotalTransactions()?.let { it1 ->
            mDataBinding.totalTransactionsValue.text = Utility.convertCurrencyFormat(it1.toDouble())
        }
        viewModel.getTotalPending()?.let { it1 ->
            mDataBinding.totalPendingValue.text = it1.toString()
        }
    }

}