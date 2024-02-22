package com.rf.utellRestaurant.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.data.model.Order
import com.rf.utellRestaurant.databinding.FragmentActiveOderBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.adapter.OrderListAdapter
import com.rf.utellRestaurant.ui.main.di.DaggerActiveOrderFragmentComponent
import com.rf.utellRestaurant.ui.main.di.DaggerOrderFragmentComponent
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModuleDi
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import com.rf.utellRestaurant.utils.Utility
import javax.inject.Inject


class ActiveOrderFragment : BaseFragment<FragmentActiveOderBinding>(R.layout.fragment_active_oder){

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    lateinit var childFragment: OrdetDescFragment
    lateinit var childUpcomingFragment: UpcomingOrderFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initializeDagger() {
        DaggerActiveOrderFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {

        val transaction = childFragmentManager.beginTransaction()
        childFragment = OrdetDescFragment()
        transaction.replace(R.id.detail_container, childFragment)
        transaction.commit()
        loadBothFragments()

    }

    private fun loadBothFragments() {
        val orders: ArrayList<Order> = Utility.generateSampleOrders()
        val orderItem: ArrayList<String> = arrayListOf()
        // Populate ListView with orders
        val adapter = OrderListAdapter(orders)
        mDataBinding.listView?.layoutManager = LinearLayoutManager(context)
        adapter.listener = cardListener
        mDataBinding.listView?.adapter = adapter

        // Automatically select the first item
        if (orders.isNotEmpty()) {
            val firstItem = orders[0]// Get the first item from your list
            childFragment =
                OrdetDescFragment.newInstance(firstItem) // Pass the selected item to the child fragment
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.detail_container, childFragment)
            transaction.commit()
        }

    }

    private val cardListener = object : OrderListAdapter.CardEvent {
        override fun onCardClicked(order: Order) {
            if (childFragment != null) {
                childFragment.updateDetails(order);
            }
        }
    }

}