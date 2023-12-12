package com.fionpay.agent.ui.main.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.TextView
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.fionpay.agent.R
import com.fionpay.agent.databinding.ActivityDashboardBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseActivity
import com.fionpay.agent.ui.base.BaseActivityModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.di.DaggerDashBoardActivityComponent
import com.fionpay.agent.ui.main.di.DashBoardActivityModule
import com.fionpay.agent.ui.main.fragment.DashBoardFragment
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.Constant
import com.fionpay.agent.utils.IOnBackPressed
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Utility
import com.google.android.material.navigation.NavigationView
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
    var doubleBackToExitPressedOnce = false

    private val headerMessageTextView: TextView by lazy {
        findViewById(R.id.headerMessageTextView)
    }

    private var isShowing = false

    private val handler = Handler(Looper.getMainLooper())

    private val animationDuration: Long = 300 // Adjust the duration as needed

    private val hideDelay: Long = 2000 // Adjust the delay before hiding as needed

    fun showHeaderMessage(message: String) {
        if (isShowing) {
            // Message is already showing, let's hide it first
            hideHeaderMessage()
        }

        headerMessageTextView.text = message
        headerMessageTextView.visibility = View.VISIBLE

        val animator: ViewPropertyAnimator = headerMessageTextView.animate()
            .translationY(0f)
            .setDuration(animationDuration)

        animator.start()

        isShowing = true

        // Schedule a task to hide the message after a delay
        handler.postDelayed({
            hideHeaderMessage()
        }, hideDelay)
    }

    private fun hideHeaderMessage() {
        val animator: ViewPropertyAnimator = headerMessageTextView.animate()
            .translationY(-headerMessageTextView.height.toFloat())
            .setDuration(animationDuration)

        animator.withEndAction {
            headerMessageTextView.visibility = View.GONE
            isShowing = false
        }

        animator.start()
    }

    private val onDashBoardActivityActionsReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.extras != null) {
                    val message = intent.extras?.getString(Constant.FIONPAY_ACTION, "")
                    if (!message.isNullOrEmpty()) {
                        setNotificationCount(message)
                    }

                }
            }
        }

    private fun setNotificationCount(message: String) {
        showHeaderMessage(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utility.getPushToken(this@DashBoardActivity)
        initializationDagger()
        initializationView()
        registerReceiver(
            onDashBoardActivityActionsReceiver,
            IntentFilter(Constant.HOME_REQUEST_NOTIFICATION_COUNT)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDashBoardActivityActionsReceiver)
    }

    private fun initializationDagger() {
        DaggerDashBoardActivityComponent.builder().appComponent(FionSDK.appComponent)
            .dashBoardActivityModule(DashBoardActivityModule())
            .baseActivityModule(BaseActivityModule(this@DashBoardActivity)).build()
            .inject(this)
    }

    private fun initializationView() {
        navController = Navigation.findNavController(this, R.id.navHostOnDashBoardFragment)
        NavigationUI.setupWithNavController(viewDataBinding?.bottomNavigation!!, navController)

        viewDataBinding?.bottomNavigation!!.setOnItemSelectedListener {
            onNavigationItemSelected(it)
        }
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

            R.id.navigation_pendingFragment -> {
                val bundle = Bundle().apply {
                    putString("Api", "All Transactions")
                    putInt("Filer", -1)
                }
                jumpToAnotherFragment(R.id.navigation_pendingFragment, bundle)
            }

            R.id.navigation_modemFragment -> {
                val bundle = Bundle().apply {
                    putString("Api", "All Transactions")
                    putInt("Filer", -1)
                }
                jumpToAnotherFragment(R.id.navigation_modemFragment, bundle)
            }

            R.id.navigation_transactionFragment -> {
                val bundle = Bundle().apply {
                    putString("Api", "B2B")
                    putInt("Filer", 2)
                }
                jumpToAnotherFragment(R.id.navigation_transactionFragment, bundle)
            }

            R.id.navigation_balanceFragment -> {
                val bundle = Bundle().apply {
                    putString("Api", "Modem List")
                }
                jumpToAnotherFragment(R.id.navigation_balanceFragment, bundle)
            }

            R.id.navigation_b2bFragment -> {
                val bundle = Bundle().apply {
                    putString("Api", "All Transactions")
                    putInt("Filer", -1)
                }
                jumpToAnotherFragment(R.id.navigation_b2bFragment, bundle)
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

