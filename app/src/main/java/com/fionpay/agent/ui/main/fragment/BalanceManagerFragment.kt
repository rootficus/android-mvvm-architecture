package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.BLTransactionModemResponse
import com.fionpay.agent.databinding.FragmentBalanceManagerBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.BleManagerListAdapter
import com.fionpay.agent.ui.main.di.BalanceManageFragmentModule
import com.fionpay.agent.ui.main.di.DaggerBalanceManagerFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.android.material.snackbar.Snackbar
import java.util.Locale
import javax.inject.Inject


class BalanceManagerFragment :
    BaseFragment<FragmentBalanceManagerBinding>(R.layout.fragment_balance_manager) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private lateinit var bleManagerListAdapter: BleManagerListAdapter
    private var arrayList: ArrayList<BLTransactionModemResponse> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {
        mDataBinding.refreshButton.setOnClickListener {
            getBlTransactionsData()
        }

        mDataBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0) {
                    filter("", true)
                }

            }
        })

        mDataBinding.filterButton.setOnClickListener { showPopupMenu(it) }

        mDataBinding.searchButton.setOnClickListener {
            filter(mDataBinding.searchView.text.toString(), true)
        }
        setAdapter()
        getBlTransactionsData()

    }

    private fun setAdapter() {
        bleManagerListAdapter = BleManagerListAdapter(arrayList)
        mDataBinding.recentModemsList.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentModemsList.adapter = bleManagerListAdapter
    }

    private fun initializeDagger() {

        DaggerBalanceManagerFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .balanceManageFragmentModule(BalanceManageFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }



    private fun getBlTransactionsData() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getBlTransactionsData()
            viewModel.blTransactionsDataResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
                        bleManagerListAdapter.notifyDataSetChanged()
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
            Snackbar.make(requireView(), getString(R.string.no_network), Snackbar.LENGTH_LONG).show()
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
                    filter("", false)
                    true
                }

                R.id.menu_success -> {
                    // Handle Menu Item 1 click
                    filter("success", false)
                    true
                }

                R.id.menu_pending -> {
                    // Handle Menu Item 1 click
                    filter("pending", false)
                    true
                }

                R.id.menu_reject -> {
                    // Handle Menu Item 1 click
                    filter("rejected", false)
                    true
                }

                R.id.menu_danger -> {
                    // Handle Menu Item 2 click
                    filter("danger", false)
                    true
                }

                else -> false
            }
        }

        // Show the pop-up menu
        popupMenu.show()
    }

    private fun filter(text: String, isSearching: Boolean) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<BLTransactionModemResponse> = ArrayList()

        if (!isSearching) {


        for (item in arrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.status?.lowercase().toString()
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                filteredList.add(item)
            }else if (text.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(arrayList)
            }
        }
        }else {
            for (item in arrayList) {
                if (text.isDigitsOnly()) {
                    if (item.customerNumber
                            ?.contains(text.lowercase(Locale.getDefault()), true) == true
                    ) {
                        // if the item is matched we are
                        filteredList.add(item)
                    }
                } else if (text.isEmpty()) {
                    filteredList.clear()
                    filteredList.addAll(arrayList)
                }
            }

        }

        if (text.isEmpty()) {
            filteredList.clear()
            filteredList.addAll(arrayList)
        }

        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            showMessage(getString(R.string.no_data_found))
        } else {
            // at last we are passing that filtered
            bleManagerListAdapter?.filterList(filteredList)
        }
    }

}