package com.jionex.agent.ui.main.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.jionex.agent.R
import com.jionex.agent.databinding.ActivityDashboardBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseActivity
import com.jionex.agent.ui.base.BaseActivityModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.di.DaggerDashBoardActivityComponent
import com.jionex.agent.ui.main.di.DashBoardActivityModule
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
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

        initializationView()

    }

    private fun initializationDagger() {
        DaggerDashBoardActivityComponent.builder().appComponent(JionexSDK.appComponent)
            .dashBoardActivityModule(DashBoardActivityModule())
            .baseActivityModule(BaseActivityModule(this@DashBoardActivity)).build()
            .inject(this)
    }

    private fun initializationView() {
        navController = Navigation.findNavController(this, R.id.navHostOnDashBoardFragment)
        NavigationUI.setupWithNavController(viewDataBinding?.navView!!, navController);
        val mNavigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val header = mNavigationView.getHeaderView(0)
        header.findViewById<AppCompatTextView>(R.id.textAgentValueFullName).text = "${dashBoardViewModel.getFullName()}"
        header.findViewById<AppCompatTextView>(R.id.textAgentValuePinCode).text = "${dashBoardViewModel.getPinCode()}"

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    private fun jumpToAnotherFragment(navigationTaskProfile: Int) {
        viewDataBinding?.drawerLayout?.close()
        navController.navigate(navigationTaskProfile)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.navigation_dashboard -> {
                jumpToAnotherFragment(R.id.navigation_dashboard)
            }
            R.id.navigation_blManager-> {
                jumpToAnotherFragment(R.id.navigation_blManager)
            }
            R.id.navigation_smsInboxFragment -> {
                jumpToAnotherFragment(R.id.navigation_smsInboxFragment)
            }
            R.id.navigation_modemFragment -> {
                jumpToAnotherFragment(R.id.navigation_modemFragment)
            }

            else -> return false
        }
        return true
    }
}

