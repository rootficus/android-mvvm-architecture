package com.fionpay.agent.ui.main.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.GetPendingModemRequest
import com.fionpay.agent.data.model.request.Modem
import com.fionpay.agent.data.model.response.TransactionModemResponse
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
import com.google.android.material.snackbar.Snackbar
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        getTransactionFilters()
        initialization()

    }

    private fun setSpinnerData(modemList: ArrayList<Modem>, bankList: ArrayList<Bank>) {
        val phoneNumbers = arrayListOf<String>()
        val bankNames = arrayListOf<String>()
        val transactionType = arrayListOf("All Types", "Cash in", "Cash Out")

        //Set Modem Adapter
        phoneNumbers.add(0, "All Modems")
        modemList.forEach {
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
                if (i == 0) {
                    Toast.makeText(requireContext(), "Please Select Modem", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${modemList[i].phoneNumber}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }


        //Set Bank Adapter
        bankNames.add(0, "All Banks")
        bankList.forEach {
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
                if (i == 0) {
                    Toast.makeText(requireContext(), "Please Select Bank", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "${bankList[i].bankName}", Toast.LENGTH_SHORT)
                        .show()
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
                    if (i == 0) {
                        Toast.makeText(
                            requireContext(),
                            "Please Transaction Type",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${transactionType[i]}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

    }

    private fun initialization() {
        mDataBinding.txtStartDate.setOnClickListener { showDatePickerDialog(mDataBinding.txtStartDate) }
        mDataBinding.txtEndDate.setOnClickListener { showDatePickerDialog(mDataBinding.txtEndDate) }

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

        mDataBinding.searchButton.setOnClickListener {
            filter(mDataBinding.searchView.text.toString())
        }
        setAdapter()
        getTransactionRecord()

    }

    private fun showDatePickerDialog(textView: AppCompatTextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, day ->
            val selectedDate = Date(year - 1900, month, day) // Note: Year is 1900-based
            if (textView == mDataBinding.txtStartDate) {
                startDate = selectedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                textView.text = dateFormat.format(selectedDate)
            } else if (textView == mDataBinding.txtEndDate) {
                endDate = selectedDate
                if (startDate != null && endDate != null && endDate!!.before(startDate)) {
                    // Invalid selection: End date is earlier than or the same as the start date
                    // You can display an error message or handle this case as needed
                    // For now, I'll just clear the end date text view
                    endDate = null
                    mDataBinding.txtEndDate.text = ""
                    Snackbar.make(requireView(), "Please Select Valid Date", Snackbar.LENGTH_LONG).show()
                    // You can also show an error message here.
                }else{
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    textView.text = dateFormat.format(selectedDate)
                }
            }

            // Update the TextView with the selected date

        }, year, month, day)

        datePickerDialog.show()
    }

    private fun setAdapter() {
        transactionListAdapter = TransactionListAdapter(arrayList)
        mDataBinding.recentModemsList.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentModemsList.adapter = transactionListAdapter
    }

    private fun initializeDagger() {
        DaggerTransactionFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .transactionFragmentModule(TransactionFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
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
                        setSpinnerData(modemList, bankList)
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


    private fun getTransactionRecord() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getTransactionsData(GetPendingModemRequest(10, 1))
            viewModel.transactionsDataResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
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

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.tansaction_menu, popupMenu.menu)

        // Set a click listener for menu item clicks
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_all -> {
                    // Handle Menu Item 1 click
                    filter("")
                    true
                }

                R.id.menu_success -> {
                    // Handle Menu Item 1 click
                    filter("Success")
                    true
                }

                R.id.menu_pending -> {
                    // Handle Menu Item 1 click
                    filter("Pending")
                    true
                }

                R.id.menu_reject -> {
                    // Handle Menu Item 1 click
                    filter("Rejected")
                    true
                }

                R.id.menu_danger -> {
                    // Handle Menu Item 2 click
                    filter("Danger")
                    true
                }

                else -> false
            }
        }

        // Show the pop-up menu
        popupMenu.show()
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<TransactionModemResponse> = ArrayList()

        for (item in arrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.status?.lowercase().toString()
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                filteredList.add(item)
            } else if (text.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(arrayList)
            }
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