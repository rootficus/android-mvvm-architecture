package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {
        mDataBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s?.isEmpty() == true)
            {
                filter("")
            }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        mDataBinding.filterButton.setOnClickListener { showPopupMenu(it) }

        mDataBinding.searchButton.setOnClickListener {
            filter(mDataBinding.searchView.text.toString())
        }
        setAdapter()
        getTransactionRecord()

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



    private fun getTransactionRecord() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getBlTransactionsData()
            viewModel.blTransactionsDataResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
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
                R.id.menu_success -> {
                    // Handle Menu Item 1 click
                    filter("Success",)
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