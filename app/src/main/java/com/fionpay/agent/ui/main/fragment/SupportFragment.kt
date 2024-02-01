package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.CreateSupportRequest
import com.fionpay.agent.data.model.request.GetSupportRequest
import com.fionpay.agent.data.model.response.SupportResponse
import com.fionpay.agent.databinding.FragmentSupportBinding
import com.fionpay.agent.databinding.SupportBottomSheetBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.SupportListAdapter
import com.fionpay.agent.ui.main.di.DaggerSupportFragmentComponent
import com.fionpay.agent.ui.main.di.SupportFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class SupportFragment : BaseFragment<FragmentSupportBinding>(R.layout.fragment_support) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }

    private lateinit var supportListAdapter: SupportListAdapter
    private var arrayList: ArrayList<SupportResponse> = arrayListOf()
    lateinit var dialog: BottomSheetDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initializeDagger() {
        DaggerSupportFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .supportFragmentModule(SupportFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {
        dialog = BottomSheetDialog(requireActivity())
        //Api Call
        getSupportDataApi()
        setAdapter()
        mDataBinding.imgBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        mDataBinding.addSupport.setOnClickListener {
            openSupportBottomSheet(true, SupportResponse())
        }
    }

    private fun openSupportBottomSheet(isCreateNew: Boolean, supportResponse: SupportResponse) {
        val supportBottomSheetBinding = SupportBottomSheetBinding.inflate(layoutInflater)
        setBottomSheetUI(supportBottomSheetBinding, dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(supportBottomSheetBinding.root)
        dialog.show()
        if (isCreateNew) {
            supportBottomSheetBinding.btnUpdate.visibility = View.VISIBLE
            supportBottomSheetBinding.etSupport.isClickable = true
            supportBottomSheetBinding.etSupport.isFocusable = true
            supportBottomSheetBinding.etDescription.isClickable = true
            supportBottomSheetBinding.etDescription.isFocusable = true
            supportBottomSheetBinding.etSupport.setText("")
            supportBottomSheetBinding.etDescription.setText("")
            supportBottomSheetBinding.btnUpdate.setOnClickListener {
                val subject = supportBottomSheetBinding.etSupport.text.toString()
                val description = supportBottomSheetBinding.etDescription.text.toString()
                if (subject.isNotEmpty() && description.isNotEmpty()) {
                    val createSupportRequest = CreateSupportRequest(subject, description)
                    createSupportTicket(createSupportRequest)
                }
            }
        } else {
            supportBottomSheetBinding.btnUpdate.visibility = View.GONE
            supportBottomSheetBinding.etSupport.isClickable = false
            supportBottomSheetBinding.etSupport.isFocusable = false
            supportBottomSheetBinding.etDescription.isClickable = false
            supportBottomSheetBinding.etDescription.isFocusable = false
            supportBottomSheetBinding.etSupport.setText(supportResponse.subject)
            supportBottomSheetBinding.etDescription.setText(supportResponse.description)
        }

    }

    private fun setAdapter() {
        supportListAdapter = SupportListAdapter(arrayList)
        mDataBinding.supportList.layoutManager = LinearLayoutManager(context)
        supportListAdapter.listener = cardListener
        mDataBinding.supportList.adapter = supportListAdapter
    }

    private val cardListener = object : SupportListAdapter.CardEvent {
        override fun onSupportItemClick(supportResponse: SupportResponse) {
            openSupportBottomSheet(false, supportResponse)
        }
    }

    private fun setBottomSheetUI(
        refundBottomSheetBinding: SupportBottomSheetBinding,
        dialog: BottomSheetDialog
    ) {

    }

    private fun getSupportDataApi() {
        if (networkHelper.isNetworkConnected()) {
            val getSupportRequest = GetSupportRequest(

            )
            viewModel.getSupportData(getSupportRequest)
            viewModel.getSupportDataResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
                        if (arrayList.isEmpty()) {
                            mDataBinding.txtNoData.visibility = View.VISIBLE
                            mDataBinding.supportList.visibility = View.GONE
                        } else {
                            mDataBinding.txtNoData.visibility = View.GONE
                            mDataBinding.supportList.visibility = View.VISIBLE
                        }
                        supportListAdapter.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                        mDataBinding.txtNoData.visibility = View.VISIBLE
                        mDataBinding.supportList.visibility = View.GONE
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

    private fun createSupportTicket(createSupportRequest: CreateSupportRequest) {
        if (networkHelper.isNetworkConnected()) {

            viewModel.createSupportTicket(createSupportRequest)
            viewModel.createSupportDataResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                        it.data?.let { it1 -> arrayList.add(it1) }
                        supportListAdapter.notifyDataSetChanged()
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

    private fun memberSupportTicket(createSupportRequest: CreateSupportRequest) {
        if (networkHelper.isNetworkConnected()) {

            viewModel.memberSupportTicket(createSupportRequest)
            viewModel.memberSupportDataResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                        val supportResponse = it.data
                        if (supportResponse != null) {
                            openSupportBottomSheet(false, supportResponse)
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
            Snackbar.make(
                requireView(),
                getString(R.string.NO_INTERNET_CONNECTION),
                Snackbar.LENGTH_LONG
            ).show()
        }

    }
}