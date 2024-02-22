package com.rf.utellRestaurant.ui.main.fragment

import SolatViewPagerAdapter
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.FragmentOderBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.adapter.CustomSpinnerAdapter
import com.rf.utellRestaurant.ui.main.di.DaggerOrderFragmentComponent
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModuleDi
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import javax.inject.Inject


class OrderFragment : BaseFragment<FragmentOderBinding>(R.layout.fragment_oder){

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private lateinit var solatViewPagerAdapter: SolatViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initializeDagger() {
        DaggerOrderFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {

        val items: MutableList<String> = ArrayList()
        items.add("Online")
        items.add("Offline")
        items.add("Pause")

        val icons = intArrayOf(R.drawable.online, R.drawable.offline, R.drawable.pause)

        // Create and set custom adapter
        val adapter = CustomSpinnerAdapter(requireActivity(), items, icons)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.spinner?.adapter = adapter


        solatViewPagerAdapter =
            SolatViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        solatViewPagerAdapter.addFragment(ActiveOrderFragment())
        solatViewPagerAdapter.addFragment(UpcomingOrderFragment())
        mDataBinding.viewPagerQuran?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mDataBinding.viewPagerQuran?.adapter = solatViewPagerAdapter
        mDataBinding.viewPagerQuran?.let { solatViewPagerAdapter.removeSwipeFunctionality(it) }
        //Tabs Layout
        mDataBinding?.tabLayoutQuran?.let {
            mDataBinding.viewPagerQuran?.let { it1 ->
                TabLayoutMediator(
                    it,
                    it1
                ) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = context?.getString(R.string.active_order)
                        }

                        1 -> {
                            tab.text = context?.getString(R.string.upcoming_order)
                        }
                    }
                }.attach()
            }
        }

    }
}