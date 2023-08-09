package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jionex.agent.R
import com.jionex.agent.data.model.request.GetBalanceByFilterRequest
import com.jionex.agent.data.model.response.GetBalanceByFilterResponse
import com.jionex.agent.databinding.CustomBleDialogBinding
import com.jionex.agent.databinding.FragmentBlManagerBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.activity.SignInActivity
import com.jionex.agent.ui.main.adapter.BleManagerListAdapter
import com.jionex.agent.ui.main.adapter.ItemListAdapter
import com.jionex.agent.ui.main.di.BLManagerFragmentModule
import com.jionex.agent.ui.main.di.DaggerBLManagerFragmentComponent
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
import com.jionex.agent.utils.Utility
import java.lang.Math.min
import javax.inject.Inject


class BLManagerFragment : BaseFragment<FragmentBlManagerBinding>(R.layout.fragment_bl_manager) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }

    private lateinit var allData: ArrayList<String>
    private var adapter: ItemListAdapter? = null
    private var bleManagerListAdapter: BleManagerListAdapter? = null
    private val itemsPerPage = 10 // Number of items to display per page
    private var listGetBalanceByFilter: ArrayList<GetBalanceByFilterResponse> = arrayListOf()

    private var currentPage = 1
    private var totalPages = 0
    private var apiCall: String = ""
    private var filter = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView()
        clickListener()
    }

    private fun initializeDagger() {
        DaggerBLManagerFragmentComponent.builder().appComponent(JionexSDK.appComponent)
            .bLManagerFragmentModule(BLManagerFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        // Initialize your allData list here (for demonstration purposes, I'm generating some dummy data)
        getBundleData()
        allData = ArrayList()
        for (i in 1..1000) {
            allData.add("Item $i")
        }
        getBalanceByFilterApi()
        // Calculate total number of pages
        totalPages = Math.ceil(allData.size.toDouble() / itemsPerPage).toInt()
        updateAdapterDataForPage(currentPage)
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            apiCall = bundle.getString("Api").toString()
            filter = bundle.getInt("Filer")
        }
    }

    private fun setBleAdapter() {
        bleManagerListAdapter = BleManagerListAdapter(listGetBalanceByFilter)
        bleManagerListAdapter?.listener = cardListener
        mDataBinding.recentTrendingView.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentTrendingView.adapter = bleManagerListAdapter
    }

    private val cardListener = object : BleManagerListAdapter.CardEvent {
        override fun onCardClicked(getBalanceByFilterResponse: GetBalanceByFilterResponse) {
            showCustomDialog(getBalanceByFilterResponse)
        }
    }

    private fun showCustomDialog(getBalanceByFilterResponse: GetBalanceByFilterResponse) {
        val builder = AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog).create()
        val binding = CustomBleDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        binding?.textAAccount?.text = getBalanceByFilterResponse.agentAccountNo.toString()
        binding?.textAmount?.text = getBalanceByFilterResponse.amount.toString()
        binding?.textCAccount?.text = getBalanceByFilterResponse.customerAccountNo.toString()
        binding?.textCommission?.text = getBalanceByFilterResponse.commision.toString()
        binding?.textDate?.text =
            Utility.convertUtc2Local(getBalanceByFilterResponse.date.toString())
        binding?.textSender?.text = getBalanceByFilterResponse.sender.toString()
        binding?.textLastBalance?.text = getBalanceByFilterResponse.lastBalance.toString()
        binding?.textOldBalance?.text = getBalanceByFilterResponse.oldBalance.toString()
        binding?.textType?.text = getBalanceByFilterResponse.bType.toString()
        binding?.textStatus?.text = getBalanceByFilterResponse.status.toString()
        binding?.textLastTransactionId?.text = getBalanceByFilterResponse.transactionId.toString()
        if (getBalanceByFilterResponse.status.equals("pending", true)) {
            binding.btnReject.visibility = View.GONE
            binding.btnAccept.visibility = View.GONE
        } else {
            binding.btnReject.visibility = View.VISIBLE
            binding.btnAccept.visibility = View.VISIBLE
        }
        binding.btnReject.setOnClickListener {
            builder.dismiss()
        }
        binding.imageClose.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun clickListener() {
        mDataBinding.btnNext.setOnClickListener {
            //Next Page
            if (currentPage < totalPages) {
                currentPage++;
                updateAdapterDataForPage(currentPage);
                updatePageNumbers();
            }
            //Last Page
            /*    if (currentPage != totalPages) {
                    currentPage = totalPages;
                    updateAdapterDataForPage(currentPage);
                    updatePageNumbers();
                }*/
        }
        mDataBinding.btnPrev.setOnClickListener {
            //Previous Page
            if (currentPage > 1) {
                currentPage--;
                updateAdapterDataForPage(currentPage);
                updatePageNumbers();
            }
            //First Page
            /*            if (currentPage != 1) {
                            currentPage = 1;
                            updateAdapterDataForPage(currentPage);
                            updatePageNumbers();
                        }*/
        }

    }

    private fun updateAdapterDataForPage(page: Int) {
        val start = (page - 1) * itemsPerPage
        val end = min(start + itemsPerPage, allData.size)
        val pageData: List<String> = allData.subList(start, end)
        adapter = ItemListAdapter(pageData)


        // Update the page numbers UI
        updatePageNumbers()
    }

    // Handle clicks on page numbers (assuming you have a TextView for each page number)
    fun onPageNumberClicked(view: View) {
        val clickedPage = (view as TextView).text.toString().toInt()
        if (clickedPage != currentPage) {
            currentPage = clickedPage
            updateAdapterDataForPage(currentPage)
        }
    }

    // Update the page numbers in the UI
    private fun updatePageNumbers() {
        mDataBinding.pageNumbersLayout.removeAllViews()

        val pageRange =
            3 // Adjust this value to change the number of page numbers displayed before and after the current page

        val startPage = (currentPage - pageRange).coerceAtLeast(1)
        val endPage = (currentPage + pageRange).coerceAtMost(totalPages)
        // Add the first page number
        // Add the first page number
        addPageNumber(mDataBinding.pageNumbersLayout, 1)

        // Add ellipsis if necessary at the beginning

        // Add ellipsis if necessary at the beginning
        if (startPage > 2) {
            addEllipsis(mDataBinding.pageNumbersLayout)
        }

        // Add the middle page numbers

        // Add the middle page numbers
        for (i in Math.max(2, startPage)..endPage) {
            addPageNumber(mDataBinding.pageNumbersLayout, i)
        }

        // Add ellipsis if necessary at the end

        // Add ellipsis if necessary at the end
        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                addEllipsis(mDataBinding.pageNumbersLayout)
            }
            addPageNumber(mDataBinding.pageNumbersLayout, totalPages)
        }

    }

    private fun addPageNumber(parent: ViewGroup, pageNumber: Int) {
        val pageNumberTextView = LayoutInflater.from(context)
            .inflate(R.layout.item_page_number, parent, false) as TextView
        pageNumberTextView.text = pageNumber.toString()
        if (pageNumber == currentPage) {
            pageNumberTextView.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
        } else {
            pageNumberTextView.setTextColor(resources.getColor(android.R.color.black))
        }
        parent.addView(pageNumberTextView)
        pageNumberTextView.setOnClickListener {
            onPageNumberClicked(pageNumberTextView)
        }
    }

    private fun addEllipsis(parent: ViewGroup) {
        val ellipsisTextView = LayoutInflater.from(context)
            .inflate(R.layout.item_ellipsis, parent, false) as TextView
        parent.addView(ellipsisTextView)
    }

    private fun getBalanceByFilterApi() {

        if (networkHelper.isNetworkConnected()) {
            val getBalanceByFilterRequest = GetBalanceByFilterRequest(
                page_size = 50,
                page_number = 1,
                /* sender = "",
                 agent_account_no = 0,
                 transaction_id = "",
                 type = "",
                 from = "",
                 to = "",*/
                balance_manager_filter = filter

            )
            viewModel.getBalanceByFilter(
                getBalanceByFilterRequest
            )
            viewModel.getBalanceByFilterResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        it.data?.let { it1 -> listGetBalanceByFilter.addAll(it1) }
                        setBleAdapter()
                        // bleManagerListAdapter?.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        //startActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()

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