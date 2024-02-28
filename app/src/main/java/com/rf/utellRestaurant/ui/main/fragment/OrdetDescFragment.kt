package com.rf.utellRestaurant.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.data.model.Order
import com.rf.utellRestaurant.data.model.request.PreparationTimeData
import com.rf.utellRestaurant.databinding.DialogPreparationTimeBinding
import com.rf.utellRestaurant.databinding.FragmentOrderDescBinding
import com.rf.utellRestaurant.databinding.OrderCancellationAlertBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.adapter.OrderMenusListAdapter
import com.rf.utellRestaurant.ui.main.adapter.PreparationTimeAdapter
import com.rf.utellRestaurant.ui.main.di.DaggerOrderDescFragmentComponent
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModuleDi
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import javax.inject.Inject


class OrdetDescFragment : BaseFragment<FragmentOrderDescBinding>(R.layout.fragment_order_desc) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }



    companion object {
        fun newInstance(selectedItem: Order): OrdetDescFragment {
            val fragment = OrdetDescFragment()
            val args = Bundle()
            args.putSerializable("selectedItem", selectedItem) // Pass the selected item as an argument
            fragment.arguments = args
            return fragment
        }
    }

    private var selectedItem: Order? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initializeDagger() {
        DaggerOrderDescFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {
        arguments?.let {
            selectedItem = it.getSerializable("selectedItem") as Order?
        }
        val adapter = selectedItem?.menus?.let { OrderMenusListAdapter(it) }
        mDataBinding.itemsRecycler?.layoutManager = LinearLayoutManager(context)
        mDataBinding.itemsRecycler?.adapter = adapter

        mDataBinding.txtUsername?.text = selectedItem?.name
        mDataBinding.txtEmail?.text = selectedItem?.email
        mDataBinding.txtAddress?.text = selectedItem?.address
        mDataBinding.txtMobile?.text = selectedItem?.mobile
        mDataBinding.txtType?.text = selectedItem?.type
        mDataBinding.txtStatus?.text = selectedItem?.status
        mDataBinding.txtRemark?.text = selectedItem?.comment
        mDataBinding.textOrderNum?.text = "Order number : ${selectedItem?.orderNumber}"
        mDataBinding.btnReject?.setOnClickListener{
            confirmReject()
        }
        mDataBinding.btnAccept?.setOnClickListener{
            confirmAccept()
        }




    }

    fun updateDetails(order: Order?) {
        if (order != null) {
            this.selectedItem = order
        }
        mDataBinding.txtUsername?.text = selectedItem?.name
        mDataBinding.txtEmail?.text = selectedItem?.email
        mDataBinding.txtAddress?.text = selectedItem?.address
        mDataBinding.txtMobile?.text = selectedItem?.mobile
        mDataBinding.txtType?.text = selectedItem?.type
        mDataBinding.txtStatus?.text = selectedItem?.status
        mDataBinding.textOrderNum?.text = "Order number : ${selectedItem?.orderNumber}"
        Log.i("Order", "${order?.name}")
    }

    private fun confirmReject() {
        val mBuilder = android.app.AlertDialog.Builder(requireActivity())

        val view = OrderCancellationAlertBinding.inflate(layoutInflater)
        mBuilder.setView(view.root);
        view.NoButtonLabel.setOnClickListener{

        }
        view.YesButtonLabel.setOnClickListener{

        }
        val dialog: android.app.AlertDialog? = mBuilder.create()
        dialog?.show()

    }

    private fun confirmAccept() {
        val mBuilder = AlertDialog.Builder(requireActivity())
        val dataList = listOf(
            PreparationTimeData("5 mins"),
            PreparationTimeData("10 mins"),
            PreparationTimeData("15 mins"),
            PreparationTimeData("20 mins"),
            PreparationTimeData("25 mins"),
            PreparationTimeData("30 mins"),
            PreparationTimeData("35 mins")
        )
        val view = DialogPreparationTimeBinding.inflate(layoutInflater)
        mBuilder.setView(view.root)
        val timeAdapter = PreparationTimeAdapter(dataList, requireActivity())
        val layoutManager = GridLayoutManager(requireActivity(),4)
        view.recyclerView.layoutManager = layoutManager
        view.recyclerView.adapter = timeAdapter
        val dialog: AlertDialog? = mBuilder.create()
        dialog?.show()

    }


}