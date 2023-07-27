package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jionex.agent.R
import com.jionex.agent.data.model.request.GetBalanceByFilter
import com.jionex.agent.databinding.FragmentBlManagerBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.adapter.ItemListAdapter
import com.jionex.agent.ui.main.di.BLManagerFragmentModule
import com.jionex.agent.ui.main.di.DaggerBLManagerFragmentComponent
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
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
    private val itemsPerPage = 10 // Number of items to display per page

    private var currentPage = 1
    private var totalPages = 0


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

        // Initialize your allData list here (for demonstration purposes, I'm generating some dummy data)
        allData = ArrayList()
        for (i in 1..1000) {
            allData.add("Item $i")
        }
        getBalanceByFilterApi()
        // Calculate total number of pages

        // Calculate total number of pages
        totalPages = Math.ceil(allData.size.toDouble() / itemsPerPage).toInt()

        mDataBinding.recentTrendingView.layoutManager = LinearLayoutManager(context)

        updateAdapterDataForPage(currentPage)
    }

    private fun clickListener() {
        mDataBinding.btnNext.setOnClickListener {
            //Next Page
            /*if (currentPage < totalPages) {
                    currentPage++;
                    updateAdapterDataForPage(currentPage);
                    updatePageNumbers();
             }*/
            //Last Page
            if (currentPage != totalPages) {
                currentPage = totalPages;
                updateAdapterDataForPage(currentPage);
                updatePageNumbers();
            }
        }
        mDataBinding.btnPrev.setOnClickListener {
            //Previous Page
            /*if (currentPage > 1) {
                currentPage--;
                updateAdapterDataForPage(currentPage);
                updatePageNumbers();
            }*/
            //First Page
            if (currentPage != 1) {
                currentPage = 1;
                updateAdapterDataForPage(currentPage);
                updatePageNumbers();
            }
        }

    }

    private fun updateAdapterDataForPage(page: Int) {
        val start = (page - 1) * itemsPerPage
        val end = min(start + itemsPerPage, allData.size)
        val pageData: List<String> = allData.subList(start, end)
        adapter = ItemListAdapter(pageData)
        mDataBinding.recentTrendingView.adapter = adapter

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
            val getBalanceByFilter = GetBalanceByFilter(
                page_size = 50,
                page_number = 1,
               /* sender = "",
                agent_account_no = 0,
                transaction_id = "",
                type = "",
                from = "",
                to = "",*/
                balance_manager_filter = -1

            )
            viewModel.getBalanceByFilter(
                getBalanceByFilter
            )
            viewModel.getBalanceByFilterResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data","::${it.data}")
                    }

                    Status.ERROR -> {
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