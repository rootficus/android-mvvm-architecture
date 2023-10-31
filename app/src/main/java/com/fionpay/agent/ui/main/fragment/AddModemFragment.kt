package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.BankListBottomSheetBinding
import com.fionpay.agent.databinding.FragmentAddModemBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.BankListAdapter
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.AddModemFragmentModule
import com.fionpay.agent.ui.main.di.DaggerAddModemFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var dashBoardListAdapter: DashBoardListAdapter
    private var arrayList: ArrayList<TransactionModel> = arrayListOf()
    val otpStringBuilder = StringBuilder()
    private var bankList: List<Bank>? = listOf()
    private lateinit var bankListAdapter: BankListAdapter
    private var selectedBankId: Int? = 0
    var selectedBankList: ArrayList<Bank> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }


    private fun initializeDagger() {
        DaggerAddModemFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .addModemFragmentModule(AddModemFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {
        mDataBinding.topHeader.txtHeader.text = getString(R.string.add_modems)
        // Bank List
        bankList = viewModel.getBanksListDao()

        val otpBoxes = arrayOf(
            mDataBinding.otpLayout.otpBox1,
            mDataBinding.otpLayout.otpBox2,
            mDataBinding.otpLayout.otpBox3,
            mDataBinding.otpLayout.otpBox4,
            mDataBinding.otpLayout.otpBox5,
            mDataBinding.otpLayout.otpBox6
        )
        generatePinCode()
        disableOtpBoxes()
        mDataBinding.labelRefresh.setOnClickListener {
            generatePinCode()
        }

        mDataBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString()

                if (name.isNotEmpty()) {
                    mDataBinding.nameVerified.visibility = View.VISIBLE
                } else {
                    mDataBinding.nameVerified.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        mDataBinding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lastName = s.toString()

                if (lastName.isNotEmpty()) {
                    mDataBinding.lastNameVerified.visibility = View.VISIBLE
                } else {
                    mDataBinding.lastNameVerified.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        mDataBinding.etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val phoneNumber = s.toString()

                if (phoneNumber.isNotEmpty() && phoneNumber.length >= 8) {
                    mDataBinding.phoneNumberVerified.visibility = View.VISIBLE
                } else {
                    mDataBinding.phoneNumberVerified.visibility = View.GONE
                }
            }
        })

        mDataBinding.btnNext.setOnClickListener {
            if (mDataBinding.etName.text.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_NAME)
                )
            } else if (mDataBinding.etLastName.text.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_LAST_NAME)
                )
            } else if (mDataBinding.etPhoneNumber.text.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_NUMBER)
                )
            } else if (otpStringBuilder.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_PIN)
                )
            } else {
                openBottomBankListDialog()

                //addModemItem()
            }

        }

        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }


        // Set up text change listeners for each OTP box
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

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
                    s?.toString()?.let { otpStringBuilder.append(it) }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

    }

    private fun disableOtpBoxes() {
        mDataBinding.otpLayout.otpBox1.isFocusable = false
        mDataBinding.otpLayout.otpBox1.isClickable = false
        mDataBinding.otpLayout.otpBox2.isFocusable = false
        mDataBinding.otpLayout.otpBox2.isClickable = false
        mDataBinding.otpLayout.otpBox3.isFocusable = false
        mDataBinding.otpLayout.otpBox3.isClickable = false
        mDataBinding.otpLayout.otpBox4.isFocusable = false
        mDataBinding.otpLayout.otpBox4.isClickable = false
        mDataBinding.otpLayout.otpBox5.isFocusable = false
        mDataBinding.otpLayout.otpBox5.isClickable = false
        mDataBinding.otpLayout.otpBox6.isFocusable = false
        mDataBinding.otpLayout.otpBox6.isClickable = false
    }


    private fun generatePinCode() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.generatePinCode()
            viewModel.generatePinCodeResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        val pinCode = it.data.toString()
                        if (pinCode.length == 6) {
                            otpStringBuilder.clear()
                            mDataBinding.otpLayout.otpBox1.setText(pinCode[0].toString())
                            mDataBinding.otpLayout.otpBox2.setText(pinCode[1].toString())
                            mDataBinding.otpLayout.otpBox3.setText(pinCode[2].toString())
                            mDataBinding.otpLayout.otpBox4.setText(pinCode[3].toString())
                            mDataBinding.otpLayout.otpBox5.setText(pinCode[4].toString())
                            mDataBinding.otpLayout.otpBox6.setText(pinCode[5].toString())

                        }
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        if (it.message == "Invalid access token") {
                            sessionExpired()
                        } else {
                            showMessage(it.message.toString())
                        }
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

    private var dialogBankListDialog: BottomSheetDialog? = null
    private fun openBottomBankListDialog() {
        dialogBankListDialog = BottomSheetDialog(mActivity)
        val binding = BankListBottomSheetBinding.inflate(layoutInflater)
        bankListAdapter = BankListAdapter(bankList)
        binding.bankList.layoutManager = GridLayoutManager(context, 2)
        bankListAdapter.listener = cardListener
        binding.bankList.adapter = bankListAdapter
        dialogBankListDialog?.setContentView(binding.root)
        dialogBankListDialog?.setOnShowListener {
            Handler().postDelayed({
                dialogBankListDialog?.let {
                    val sheet = it
                    sheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }, 0)
        }
        binding.sendBtn.setOnClickListener {
            dialogBankListDialog?.dismiss()
            if (selectedBankId != 0) {
                val modemItemModel = ModemItemModel(
                    mDataBinding.etName.text.toString(),
                    mDataBinding.etLastName.text.toString(),
                    otpStringBuilder.toString().toLong(),
                    mDataBinding.etPhoneNumber.text.toString(),
                    selectedBankId
                )
                val bundle = Bundle().apply {
                    putSerializable("modemItemModel", modemItemModel)
                }
                Navigation.findNavController(requireView())
                    .navigate(
                        R.id.action_navigation_addModemFragment_to_navigation_addModemBalanceFragment,
                        bundle
                    )
            }
            /*Toast.makeText(
                  requireContext(),
                  "Please enter valid bank pincode.",
                  Toast.LENGTH_SHORT
              ).show()*/
        }
        dialogBankListDialog?.show()
    }

    private val cardListener = object : BankListAdapter.BankCardEvent {
        override fun onCardClick(bankId: Int?) {
            selectedBankId = bankId
            showMessage(selectedBankId.toString())
        }
    }

    private fun addModemItem() {

        if (networkHelper.isNetworkConnected()) {
            val modemItemModel = ModemItemModel(
                mDataBinding.etName.text.toString(),
                mDataBinding.etLastName.text.toString(),
                otpStringBuilder.toString().toLong()
            )
            viewModel.addModemItem(modemItemModel)
            viewModel.getAddModemItemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        val modemId = it.data?.id
                        val bundle = Bundle().apply {
                            putString("modem_id", modemId)
                            putSerializable("modemItemModel", modemItemModel)
                        }
                        Navigation.findNavController(requireView())
                            .navigate(
                                R.id.action_navigation_addModemFragment_to_navigation_addModemBalanceFragment,
                                bundle
                            )
                        // bleManagerListAdapter?.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        // startActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()
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
            progressBar.dismiss()
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }
}