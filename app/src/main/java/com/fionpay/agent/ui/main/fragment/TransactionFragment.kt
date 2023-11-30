package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.Modem
import com.fionpay.agent.data.model.request.TransactionFilterRequest
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.databinding.AlterPendingDialogBinding
import com.fionpay.agent.databinding.FragmentTransactionsBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.TransactionListAdapter
import com.fionpay.agent.ui.main.di.DaggerTransactionFragmentComponent
import com.fionpay.agent.ui.main.di.TransactionFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class TransactionFragment :
    BaseFragment<FragmentTransactionsBinding>(R.layout.fragment_transactions) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private lateinit var transactionListAdapter: TransactionListAdapter
    private var arrayList: ArrayList<TransactionModemResponse> = arrayListOf()
    var filterLayoutVisible = true
    var modemList: ArrayList<Modem> = arrayListOf()
    var bankList: ArrayList<Bank> = arrayListOf()
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var paymentType: String = "All"
    private var bankType: String = "All"
    private var modemSlotId: Any = "All"
    private var calendar: Calendar? = null
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()

        initialization()

    }

    private fun initializeDagger() {
        DaggerTransactionFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .transactionFragmentModule(TransactionFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    override fun onResume() {
        super.onResume()
        setSpinnerData()
    }

    private fun initialization() {
        setSpinnerData()
        val gson = Gson()
        val json: String? = viewModel.getDashBoardDataModel()
        val obj: DashBoardItemResponse =
            gson.fromJson(json, DashBoardItemResponse::class.java)
        mDataBinding.txtStartDate.text = Utility.dateBeforeOneMonth()
        mDataBinding.txtEndDate.text = Utility.currentDate()
        startDate = dateFormat.parse(mDataBinding.txtStartDate.text.toString())
        endDate = dateFormat.parse(mDataBinding.txtEndDate.text.toString())
        mDataBinding.txtStartDate.setOnClickListener { showDatePickerDialog(mDataBinding.txtStartDate) }
        mDataBinding.txtEndDate.setOnClickListener { showDatePickerDialog(mDataBinding.txtEndDate) }
        //mDataBinding.txtTotalBalance.text = "৳${obj.totalBalance.toString()}"
        mDataBinding.refreshButton.setOnClickListener {
            getTransactionRecord()
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
            getTransactionRecord()
        }

        mDataBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0) {
                    filter("")
                }

            }
        })

        mDataBinding.searchButton.setOnClickListener {
            filter(mDataBinding.searchView.text.toString())
        }
        setAdapter()
        getTransactionRecord()

    }

    private fun setSpinnerData() {
        val modemList = viewModel.getModemsListDao()
        val bankList = viewModel.getBanksListDao()
        val phoneNumbers = arrayListOf<String>()
        val bankNames = arrayListOf<String>()
        val transactionType = arrayListOf("All Types", "Cash In", "Cash Out")

        //Set Modem Adapter
        phoneNumbers.add(0, "All Modems")
        modemList?.forEach {
            phoneNumbers.add(it.phoneNumber.toString())
        }
        val modemAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            phoneNumbers
        )
        modemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.spnModem.adapter = modemAdapter

        mDataBinding.spnModem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                modemSlotId = if (i == 0) {
                    "All"
                } else {
                    modemList?.get(i - 1)?.modemSlotId.toString()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }


        //Set Bank Adapter
        bankNames.add(0, "All Banks")
        bankList?.forEach {
            bankNames.add(it.bankName.toString())
        }

        val bankAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            bankNames
        )
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.spBank.adapter = bankAdapter

        mDataBinding.spBank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                bankType = if (i == 0) {
                    "All"
                } else {
                    bankList?.get(i - 1)?.bankName.toString()
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        //Set Transaction Type Adapter

        val transactionTypeAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            transactionType
        )
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.spTransactionType.adapter = transactionTypeAdapter

        mDataBinding.spTransactionType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    paymentType = if (i == 0) {
                        "All"
                    } else {
                        transactionType?.get(i).toString()
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
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

    private fun setAdapter() {
        transactionListAdapter = TransactionListAdapter(arrayList)
        transactionListAdapter?.listener = cardListener
        mDataBinding.recentModemsList.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentModemsList.adapter = transactionListAdapter
    }

    private val cardListener = object : TransactionListAdapter.CardEvent {
        override fun onCardClicked(transactionModemResponse: TransactionModemResponse) {
            showAlertDialog(transactionModemResponse)
        }
    }




    private fun getTransactionRecord() {
        if (networkHelper.isNetworkConnected()) {
            val transactionFilterRequest = TransactionFilterRequest(
                startDate = mDataBinding.txtStartDate.text.toString(),
                endDate = mDataBinding.txtEndDate.text.toString(),
                paymentType = paymentType,
                bankType = bankType,
                modemSlotId = modemSlotId
            )
            viewModel.getTransactionsData(transactionFilterRequest)
            viewModel.transactionsDataResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
                        totalCalculation(arrayList)
                        transactionListAdapter.notifyDataSetChanged()
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

    private fun totalCalculation(arrayList: java.util.ArrayList<TransactionModemResponse>) {
        var totalAmount = 0.0;
        arrayList.forEach {
            if(it.status == "Approved"){
                totalAmount = totalAmount.plus(it.amount.toString().toDouble())
            }
        }
        mDataBinding.txtTotalBalance.text = "৳$totalAmount"
    }

    private fun showAlertDialog(item: TransactionModemResponse) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val binding = AlterPendingDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)
        val alertDialog: AlertDialog = dialogBuilder.create()
        if (!item.bankImage.isNullOrEmpty()) {
            Glide.with(requireContext())
                .asBitmap()
                .centerInside()
                .load(item.bankImage)
                .error(R.drawable.bank_icon)
                .into(binding.imageBank)
        }
        setPaymentStatusView(item, binding)
        setStatusView(item, binding)
        if(item.transactionId.isNullOrEmpty())
        {
            binding.txtTransactionId.visibility = View.GONE
            binding.labelTransactionId.visibility = View.GONE
            binding.iconCopy.visibility = View.GONE
        }else
        {
            binding.txtTransactionId.visibility = View.VISIBLE
            binding.labelTransactionId.visibility = View.VISIBLE
            binding.iconCopy.visibility = View.VISIBLE
        }
        binding.txtTransactionId.text = "#${item.transactionId.toString()}"
        binding.txtPayeeNumber.text = "☎ ${item.customer.toString()}"

        binding.iconCopy.setOnClickListener {
            val clipboard: ClipboardManager? =
                requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("TxnId", "${binding.txtTransactionId.text.toString()}")
            clipboard?.setPrimaryClip(clip)
            showMessage("Text Copied Successfully")
        }
        binding.txtAmount.text = "৳${item.amount}"

        binding.txtDate.text = Utility.convertTransactionDate(item.date)
        binding.txtBankType.text = item.bankType

        alertDialog.show()
    }

    private fun setStatusView(item: TransactionModemResponse, binding: AlterPendingDialogBinding) {
        binding.txtSuccess.visibility = View.VISIBLE
        binding.txtSuccess.text = item.status
        when (item.status) {
            "Approved" -> {
                binding.txtSuccess.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.activeGreen
                    )
                )
                binding.txtSuccess.backgroundTintList =
                    (ContextCompat.getColorStateList(requireContext(), R.color.activeGreenBg))
            }

            "Rejected" -> {
                binding.txtSuccess.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.reject
                    )
                )
                binding.txtSuccess.backgroundTintList =
                    (ContextCompat.getColorStateList(requireContext(), R.color.activeDangerBg))
            }
        }
    }

    private fun setPaymentStatusView(
        item: TransactionModemResponse,
        binding: AlterPendingDialogBinding
    ) {
        when (item.paymentType) {
            "Cash In" -> {
                binding.labelPaymentType.text = item.paymentType.toString()
                binding.labelPaymentType.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.reject
                    )
                )
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.reject
                    )
                )
            }

            "Cash Out" -> {
                binding.labelPaymentType.text = item.paymentType.toString()
                binding.labelPaymentType.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.greenColor
                    )
                )
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.greenColor
                    )
                )
            }
        }
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<TransactionModemResponse> = ArrayList()

        for (item in arrayList) {
            // checking if the entered string matched with any item of our recycler view.

            if (text.isDigitsOnly()) {
                if (item.customer?.contains(text) == true) {
                    // if the item is matched we are
                    filteredList.add(item)
                }
            } else if (text.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(arrayList)
            } else {
                if (item.transactionId?.contains(text) == true) {
                    // if the item is matched we are
                    filteredList.add(item)
                }
            }
        }
        if (text.isEmpty()) {
            filteredList.clear()
            filteredList.addAll(arrayList)
        }

        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            transactionListAdapter?.filterList(filteredList)
        }
    }
}