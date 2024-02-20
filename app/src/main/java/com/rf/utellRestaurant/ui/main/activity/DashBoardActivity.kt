package com.rf.utellRestaurant.ui.main.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.ActivityDashboardBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseActivity
import com.rf.utellRestaurant.ui.base.BaseActivityModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.di.DaggerDashBoardActivityComponent
import com.rf.utellRestaurant.ui.main.di.DashBoardActivityModule
import com.rf.utellRestaurant.ui.main.fragment.DashBoardFragment
import com.rf.utellRestaurant.ui.main.viewmodel.DashBoardViewModel
import com.rf.utellRestaurant.utils.IOnBackPressed
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import com.rf.utellRestaurant.utils.Utility
import javax.inject.Inject


class DashBoardActivity : BaseActivity<ActivityDashboardBinding>(R.layout.activity_dashboard),
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewmodel: DashBoardViewModel by viewModels { dashBoardViewModelFactory }

    private lateinit var navController: NavController
    var doubleBackToExitPressedOnce = false

    private val headerMessageTextView: TextView by lazy {
        findViewById(R.id.headerMessageTextView)
    }

    private var isShowing = false

    private val handler = Handler(Looper.getMainLooper())

    private val animationDuration: Long = 300 // Adjust the duration as needed

    private val hideDelay: Long = 2000 // Adjust the delay before hiding as needed





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Utility.getPushToken(this@DashBoardActivity)
        initializationDagger()
        initializationView()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initializationDagger() {
        DaggerDashBoardActivityComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardActivityModule(DashBoardActivityModule())
            .baseActivityModule(BaseActivityModule(this@DashBoardActivity)).build()
            .inject(this)
    }

    private fun initializationView() {
        navController = Navigation.findNavController(this, R.id.navHostOnDashBoardFragment)
        NavigationUI.setupWithNavController(viewDataBinding?.bottomNavigation!!, navController)


    }

    private fun jumpToAnotherFragment(navigationTaskProfile: Int, bundle: Bundle) {
        navController.navigate(navigationTaskProfile, bundle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                val bundle = Bundle()
                jumpToAnotherFragment(R.id.navigation_dashboard, bundle)
            }

            R.id.navigation_notificationFragment -> {
                val bundle = Bundle()
                jumpToAnotherFragment(R.id.navigation_notificationFragment, bundle)
            }

            else -> return false
        }
        return true
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.primaryNavigationFragment as NavHostFragment?
        if (navHostFragment != null) {
            // Getting the current fragment in the navGraph
            val currentFragment = navHostFragment.childFragmentManager.fragments[0]
            val nameOfCurrentFragment = currentFragment.javaClass.simpleName
            val homeFragmentId = R.id.navigation_dashboard

            if (currentFragment is IOnBackPressed) {
                (currentFragment as IOnBackPressed).onBackPressed()
                return
            }
            if (currentFragment != null && nameOfCurrentFragment == DashBoardFragment::class.java.simpleName) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }

                this.doubleBackToExitPressedOnce = true

                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                    doubleBackToExitPressedOnce = false
                }, 500)
                return
            }

            if (currentFragment != null && currentFragment.id != homeFragmentId) {
                navController.popBackStack(homeFragmentId, false)
                navController.navigate(homeFragmentId)
                return
            }

        }
        super.onBackPressed()
    }

}

