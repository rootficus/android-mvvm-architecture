package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.GetPendingModemRequest
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.databinding.FragmentPendingBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.adapter.PendingListAdapter
import com.fionpay.agent.ui.main.di.DaggerPendingFragmentComponent
import com.fionpay.agent.ui.main.di.PendingFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class PendingFragment : BaseFragment<FragmentPendingBinding>(R.layout.fragment_pending) {

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
    private lateinit var pendingListAdapter: PendingListAdapter
    private var arrayList: ArrayList<PendingModemResponse> = arrayListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()

    }

    private fun initializeDagger() {
        DaggerPendingFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .pendingFragmentModule(PendingFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {

        mDataBinding.searchButton.setOnClickListener {
            filter(mDataBinding.searchView.text.toString())
        }
        mDataBinding.refreshButton.setOnClickListener {
            getPendingModemRecord()
        }
        setAdapter()
        getPendingModemRecord()
    }

    private fun setAdapter() {
        pendingListAdapter = PendingListAdapter(arrayList)
        mDataBinding.pendingModemsList.layoutManager = LinearLayoutManager(context)
        mDataBinding.pendingModemsList.adapter = pendingListAdapter
    }


    private fun getPendingModemRecord() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getPendingRequest(GetPendingModemRequest(10, 1))
            viewModel.getPendingModemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
                        pendingListAdapter.notifyDataSetChanged()
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

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<PendingModemResponse> = ArrayList()

        for (item in arrayList) {
            // checking if the entered string matched with any item of our recycler view.

            if (item.customer?.contains(text) == true) {
                // if the item is matched we are
                filteredList.add(item)
            } else if (text.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(arrayList)
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            Snackbar.make(requireView(), "No Data Found..", Snackbar.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            pendingListAdapter?.filterList(filteredList)
        }
    }

}