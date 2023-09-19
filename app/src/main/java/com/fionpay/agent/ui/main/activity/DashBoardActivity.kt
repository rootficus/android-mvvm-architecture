package com.fionpay.agent.ui.main.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.fionpay.agent.R
import com.fionpay.agent.databinding.ActivityDashboardBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseActivity
import com.fionpay.agent.ui.base.BaseActivityModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.DrawerAdapter
import com.fionpay.agent.ui.main.di.DaggerDashBoardActivityComponent
import com.fionpay.agent.ui.main.di.DashBoardActivityModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import javax.inject.Inject


class DashBoardActivity : BaseActivity<ActivityDashboardBinding>(R.layout.activity_dashboard),
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val dashBoardViewModel: DashBoardViewModel by viewModels { dashBoardViewModelFactory }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializationDagger()
        //dashBoardViewModel.checkNightTheme(true)
        initializationView()


    }

    private fun initializationDagger() {
        DaggerDashBoardActivityComponent.builder().appComponent(FionSDK.appComponent)
            .dashBoardActivityModule(DashBoardActivityModule())
            .baseActivityModule(BaseActivityModule(this@DashBoardActivity)).build()
            .inject(this)
    }

    private fun initializationView() {
        navController = Navigation.findNavController(this, R.id.navHostOnDashBoardFragment)
        NavigationUI.setupWithNavController(viewDataBinding?.bottomNavigation!!, navController);

        viewDataBinding?.bottomNavigation!!.setOnItemSelectedListener {
            onNavigationItemSelected(it)
        }
    }

    private fun jumpToAnotherFragment(navigationTaskProfile: Int, bundle: Bundle) {
        navController.navigate(navigationTaskProfile, bundle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_dashboard-> {
                val bundle = Bundle()
                jumpToAnotherFragment(R.id.navigation_dashboard, bundle)
            }
            R.id.navigation_modemFragment-> {
                val bundle = Bundle().apply {
                    putString("Api", "All Transactions")
                    putInt("Filer", -1)
                }
                jumpToAnotherFragment(R.id.navigation_modemFragment, bundle)
            }
            R.id.navigation_transactionFragment-> {
                val bundle = Bundle().apply {
                    putString("Api", "B2B")
                    putInt("Filer", 2)
                }
                jumpToAnotherFragment(R.id.navigation_transactionFragment, bundle)
            }
            R.id.navigation_modemFragment-> {
                val bundle = Bundle().apply {
                    putString("Api", "Modem List")
                }
                jumpToAnotherFragment(R.id.navigation_modemFragment, bundle)
            }
            else -> return false
        }
        return true
    }
}

