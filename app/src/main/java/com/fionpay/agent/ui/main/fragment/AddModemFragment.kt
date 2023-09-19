package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentAddModemBinding
import com.fionpay.agent.databinding.FragmentSettingBinding
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import javax.inject.Inject


class AddModemFragment : BaseFragment<FragmentAddModemBinding>(R.layout.fragment_add_modem) {

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
        initialization()
    }

    private fun initialization() {
        mDataBinding.topHeader.txtHeader.text = getString(R.string.add_modems)
        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }

        val otpBoxes = arrayOf(mDataBinding.otpLayout.otpBox1, mDataBinding.otpLayout.otpBox2, mDataBinding.otpLayout.otpBox3, mDataBinding.otpLayout.otpBox4,
            mDataBinding.otpLayout.otpBox5, mDataBinding.otpLayout.otpBox6)

        // Set up text change listeners for each OTP box
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        // When a digit is entered, move focus to the next OTP box
                        if (i < otpBoxes.size - 1) {
                            otpBoxes[i + 1].requestFocus()
                        } else {
                            // If this is the last OTP box, you can perform some action like submitting the OTP
                            // For example: validateOTP()
                        }
                    } else if (s?.isEmpty() == true && i > 0) {
                        // When the user clears the digit, move focus to the previous OTP box
                        otpBoxes[i - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        mDataBinding.btnNext.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_addModemBalanceFragment)
        }
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