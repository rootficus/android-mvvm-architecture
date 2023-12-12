package com.fionpay.agent.ui.main.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.GetAgentB2BRequest
import com.fionpay.agent.data.model.request.ReturnBalanceRequest
import com.fionpay.agent.data.model.response.B2BResponse
import com.fionpay.agent.data.model.response.ModemPinCodeResponse
import com.fionpay.agent.databinding.FragmentB2bBinding
import com.fionpay.agent.databinding.ModemBottomSheetBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.B2BListAdapter
import com.fionpay.agent.ui.main.di.B2BFragmentModule
import com.fionpay.agent.ui.main.di.DaggerB2BFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class B2BFragment :
    BaseFragment<FragmentB2bBinding>(R.layout.fragment_b2b) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private lateinit var b2BListAdapter: B2BListAdapter
    private var arrayList: ArrayList<B2BResponse> = arrayListOf()
    private var modemPinCodeList: ArrayList<ModemPinCodeResponse> = arrayListOf()
    var currentBalance: Double? = 0.0
    var totalBalance: Double? = 0.0
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var filterLayoutVisible = true
    private var modemId = " "
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()

    }

    private fun initializeDagger() {
        DaggerB2BFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .b2BFragmentModule(B2BFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {
        mDataBinding.txtStartDate.text = Utility.dateBeforeOneMonth()
        mDataBinding.txtEndDate.text = Utility.currentDate()
        startDate = dateFormat.parse(mDataBinding.txtStartDate.text.toString())
        endDate = dateFormat.parse(mDataBinding.txtEndDate.text.toString())

        mDataBinding.txtStartDate.setOnClickListener { showDatePickerDialog(mDataBinding.txtStartDate) }
        mDataBinding.txtEndDate.setOnClickListener { showDatePickerDialog(mDataBinding.txtEndDate) }
        getModemPinCodes()
        setAdapter()

        //API Call
        getRefundRecord()

        mDataBinding.refreshButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireActivity())
            val refundBottomSheetBinding = ModemBottomSheetBinding.inflate(layoutInflater)
            setBottomSheetUI(refundBottomSheetBinding, dialog)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(refundBottomSheetBinding.root)
            dialog.show()
        }


        mDataBinding.filterButton.setOnClickListener {
            if (filterLayoutVisible) {
                filterLayoutVisible = false
                mDataBinding.layoutFilter.visibility = View.VISIBLE
            } else {
                filterLayoutVisible = true
                mDataBinding.layoutFilter.visibility = View.GONE
            }
            //showPopupMenu(it)
        }


        mDataBinding.btnApply.setOnClickListener {
            mDataBinding.layoutFilter.visibility = View.GONE
            getRefundRecord()
        }
    }

    private fun showDatePickerDialog(textView: AppCompatTextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, day ->
            val selectedDate = Date(year - 1900, month, day) // Note: Year is 1900-based
            if (textView == mDataBinding.txtEndDate) {
                endDate = selectedDate

                textView.text = dateFormat.format(selectedDate)
                if (startDate != null && endDate != null && startDate!!.after(selectedDate)) {
                    mDataBinding.txtStartDate.text = ""
                }

            } else if (textView == mDataBinding.txtStartDate) {
                startDate = selectedDate
                if (startDate != null && endDate != null && startDate!!.before(selectedDate)) {
                    // Invalid selection: End date is earlier than or the same as the start date
                    // You can display an error message or handle this case as needed
                    // For now, I'll just clear the end date text view
                    endDate = null
                    mDataBinding.txtStartDate.text = ""
                    Snackbar.make(requireView(), "Please Select Valid Date", Snackbar.LENGTH_LONG)
                        .show()
                    // You can also show an error message here.
                } else {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    textView.text = dateFormat.format(selectedDate)
                }
            }

            // Update the TextView with the selected date

        }, year, month, day)
        if (textView == mDataBinding.txtStartDate) {
            try {
                val maxDate: Date = dateFormat.parse(mDataBinding.txtEndDate.text.toString())
                if (maxDate != null) {
                    datePickerDialog.datePicker.maxDate = maxDate.time
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else {
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        }
        datePickerDialog.show()
    }

    private fun setSpinnerData(modemPinCodeList: ArrayList<ModemPinCodeResponse>?) {
        val modemIds = arrayListOf<String>()
        //Set Modem Adapter
        modemIds.add(0, "All")
        modemPinCodeList?.forEach {
            modemIds.add(it.pincode.toString())
        }

        val modemAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            modemIds
        )
        modemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.spnModem.adapter = modemAdapter

        mDataBinding.spnModem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                modemId = if (i >= 0 && modemPinCodeList != null && i < modemPinCodeList.size) {
                    if (i == 0) {
                        " "
                    } else {
                        modemPinCodeList[i - 1].id.toString()
                    }
                } else {
                    // Handle the null case or out-of-bounds index, maybe log an error or return early
                    // You may also want to set a default value for modemId in case of an issue
                    " "
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }


    private fun setAdapter() {
        b2BListAdapter = B2BListAdapter(arrayList)
        mDataBinding.recentModemsList.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentModemsList.adapter = b2BListAdapter
    }

    private fun getModemPinCodes() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getModemPinCodes()
            viewModel.getModemPinCodesResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        modemPinCodeList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> modemPinCodeList.addAll(it1) }
                        setSpinnerData(modemPinCodeList)
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
            setSpinnerData(modemPinCodeList)
            Snackbar.make(
                requireView(),
                getString(R.string.NO_INTERNET_CONNECTION),
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    private fun getRefundRecord() {
        if (networkHelper.isNetworkConnected()) {
            val getAgentB2BRequest = GetAgentB2BRequest(
                id = viewModel.getUserId().toString(),
                modemId = modemId,
                startDate = mDataBinding.txtStartDate.text.toString(),
                endDate = mDataBinding.txtEndDate.text.toString(),
            )
            viewModel.getB2BRecord(getAgentB2BRequest)
            viewModel.getB2BRecordResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
                        b2BListAdapter.notifyDataSetChanged()
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
            Snackbar.make(
                requireView(),
                getString(R.string.NO_INTERNET_CONNECTION),
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    private fun setBottomSheetUI(
        refundBottomSheetBinding: ModemBottomSheetBinding,
        dialog: BottomSheetDialog
    ) {
        //View Note Visibility
        refundBottomSheetBinding.labelNotes.visibility = View.VISIBLE
        refundBottomSheetBinding.etNotes.visibility = View.VISIBLE


        currentBalance = viewModel.getCurrentAgentBalance()?.toDouble()
        val currentBal = "৳${currentBalance.toString()}"
        refundBottomSheetBinding.etCurrentBalance.text = currentBal
        refundBottomSheetBinding.labelTotalBalance.text =
            getString(R.string.new_balance_will, "$currentBalance")
        Log.i("Current:", "${refundBottomSheetBinding.etCurrentBalance.text}")

        refundBottomSheetBinding.etUpdateBalance.hint = getText(R.string.refund_amount)
        refundBottomSheetBinding.etUpdateBalance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    val newVal = s.toString().toDouble()
                    totalBalance = currentBalance?.minus(newVal)
                    if (totalBalance != null && totalBalance!! >= 0.0) {
                        refundBottomSheetBinding.labelTotalBalance.text =
                            getString(R.string.new_balance_will, "$totalBalance")
                    } else {
                        showMessage("Entered amount should be less than agent balance")
                        getString(R.string.new_balance_will, "$currentBalance")
                        refundBottomSheetBinding.etUpdateBalance.setText("")
                    }

                } else {
                    refundBottomSheetBinding.labelTotalBalance.text =
                        getString(R.string.new_balance_will, "$currentBalance")
                }

            }
        })

        refundBottomSheetBinding.imageClose.setOnClickListener {
            dialog.dismiss()
        }

        refundBottomSheetBinding.btnUpdate.setOnClickListener {
            if (refundBottomSheetBinding.etUpdateBalance.text.toString().isEmpty()) {
                refundBottomSheetBinding.etUpdateBalance.error = getString(R.string.amount_error)
            } else {

                val amount = refundBottomSheetBinding.etUpdateBalance.text.toString().toDouble()
                refundBottomSheetBinding.etUpdateBalance.setText("")
                refundBottomSheetBinding.etNotes.setText("")
                //Api Call
                returnBalanceToDistributor(
                    amount = amount,
                    notes = refundBottomSheetBinding.etNotes.text.toString(),
                    dialog
                )
            }
        }
    }

    private fun returnBalanceToDistributor(
        amount: Double,
        notes: String,
        dialog: BottomSheetDialog
    ) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.returnBalanceToDistributor(ReturnBalanceRequest(amount, notes))
            viewModel.returnBalanceToDistributorResponseModem.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        if (dialog.isShowing) {
                            dialog.dismiss()
                            viewModel.setCurrentAgentBalance(it.data?.currentBalance.toString())
                            getRefundRecord()
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
            progressBar.dismiss()
            showMessage(
                mActivity.getString(R.string.NO_INTERNET_CONNECTION)
            )
        }
    }
}