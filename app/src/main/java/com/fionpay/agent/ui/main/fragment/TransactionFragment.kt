package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentAddModemBinding
import com.fionpay.agent.databinding.FragmentSettingBinding
import com.fionpay.agent.databinding.FragmentTransactionsBinding
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import javax.inject.Inject


class TransactionFragment : BaseFragment<FragmentTransactionsBinding>(R.layout.fragment_transactions) {

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
    }

    private fun initializeDagger() {
      /*  DaggerNotificationFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .dashBoardFragmentModule(NotificationFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)*/
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