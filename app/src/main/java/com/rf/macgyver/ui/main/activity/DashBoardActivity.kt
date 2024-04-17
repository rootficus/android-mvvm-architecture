package com.rf.macgyver.ui.main.activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.rf.macgyver.R
import com.rf.macgyver.databinding.ActivityDashboardBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseActivity
import com.rf.macgyver.ui.base.BaseActivityModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerDashBoardActivityComponent
import com.rf.macgyver.ui.main.di.DashBoardActivityModule
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import javax.inject.Inject


class DashBoardActivity : BaseActivity<ActivityDashboardBinding, Any?>(R.layout.activity_dashboard),
    NavigationView.OnNavigationItemSelectedListener {


    private val drawerToggle: ActionBarDrawerToggle? = null
    private lateinit var drawerLayout: DrawerLayout

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewmodel: DashBoardViewModel by viewModels { dashBoardViewModelFactory }

    private lateinit var navController: NavController
    var doubleBackToExitPressedOnce = false
/*
    private val headerMessageTextView: TextView by lazy {
        findViewById(R.id.headerMessageTextView)
    }*/

    private var isShowing = false

    private val handler = Handler(Looper.getMainLooper())

    private val animationDuration: Long = 300 // Adjust the duration as needed

    private val hideDelay: Long = 2000 // Adjust the delay before hiding as needed

    // Interface to communicate with the fragment
    interface OnChangeFragmentButtonClickListener {
        fun onChangeFragmentButtonClicked()
    }


    private val mListener: OnChangeFragmentButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.getBundleExtra("bundle")
        if (bundle != null) {
            val uniqueToken = bundle.getString("uniqueId")
            // Do whatever you need with the data
        }
        //Utility.getPushToken(this@DashBoardActivity)
        initializationDagger()
        initializationView()

    }


    private fun initializationView() {
        navController = Navigation.findNavController(this, R.id.navHostOnDashBoardFragment)
        NavigationUI.setupWithNavController(viewDataBinding?.bottomNav!!, navController)

        viewDataBinding?.bottomNav!!.setOnItemSelectedListener {
            onNavigationItemSelected(it)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                val bundle = Bundle()
                jumpToAnotherFragment(R.id.navigation_dashboard, bundle)
            }

            R.id.navigation_account -> {
                val bundle = Bundle()
                jumpToAnotherFragment(R.id.navigation_account, bundle)
            }
            else-> return false
        }
        return true
    }
    private fun jumpToAnotherFragment(navigationTaskProfile: Int, bundle: Bundle) {
        navController.navigate(navigationTaskProfile, bundle)
    }

    fun toggleDrawer(view: View?) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }


    override fun onPostCreate(@Nullable savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
       // drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
      //  drawerToggle?.onConfigurationChanged(newConfig)
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

  /*  private fun initializationView() {
        // Sample data
        val items: MutableList<String> = ArrayList()
        items.add("Online")
        items.add("Offline")
        items.add("Pause")

        val icons = intArrayOf(R.drawable.online, R.drawable.offline, R.drawable.pause)*/

        /*// Create and set custom adapter

        // Create and set custom adapter
        val adapter = CustomSpinnerAdapter(this, items, icons)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding?.headerLayout?.spinner?.adapter = adapter


        viewDataBinding?.navigationRail?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    Toast.makeText(applicationContext, "Dashboard", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.navigation_notificationFragment -> {
                    Toast.makeText(applicationContext, "Notification", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
        sideNavClik()
        navController = Navigation.findNavController(this, R.id.navHostOnDashBoardFragment)
        NavigationUI.setupWithNavController(viewDataBinding?.navigationRail!!, navController)

        val headerNav = viewDataBinding?.headerLayout
        headerNav?.printerButton?.setOnClickListener{
            newOrderAlert()
        }*/


    }

    /*private fun sideNavClik() {
        val sideNav = viewDataBinding?.sideNavLayout
        if (sideNav != null) {
            changeSelectedNavUi("Home", sideNav)
            changeNonSelectedNavUi(
                arrayListOf(
                    "History",
                    "Order",
                    "Logout",
                    "Setting"
                ), sideNav
            )
        }
        sideNav?.layoutHome?.setOnClickListener {
            setHeaderText(getString(R.string.dashboard))

            val bundle = Bundle()
            jumpToAnotherFragment(R.id.navigation_dashboard, bundle)
            changeSelectedNavUi("Home", sideNav)
            changeNonSelectedNavUi(
                arrayListOf(
                    "History",
                    "Order",
                    "Logout",
                    "Setting"
                ), sideNav
            )
        }
        sideNav?.layoutHistory?.setOnClickListener {
            setHeaderText(getString(R.string.hisory))

            val bundle = Bundle()
            jumpToAnotherFragment(R.id.navigation_historyFragment, bundle)
            changeSelectedNavUi(
                "History", sideNav
            )
            changeNonSelectedNavUi(
                arrayListOf(
                    "Home",
                    "Order",
                    "Logout",
                    "Setting"
                ), sideNav
            )
        }
        sideNav?.layoutOrders?.setOnClickListener {
            viewDataBinding?.headerLayout?.headerLayout?.visibility = View.GONE


            val bundle = Bundle()
            jumpToAnotherFragment(R.id.navigation_orderFragment, bundle)
            changeSelectedNavUi("Order", sideNav)
            changeNonSelectedNavUi(
                arrayListOf(
                    "Home",
                    "History",
                    "Logout",
                    "Setting"
                ), sideNav
            )
        }
        sideNav?.layoutLogout?.setOnClickListener {
            verifyLogout()
        }

        sideNav?.layoutSetting?.setOnClickListener {
            setHeaderText(getString(R.string.settings))

            val bundle = Bundle()
            jumpToAnotherFragment(R.id.navigation_settingFragment, bundle)
            changeSelectedNavUi(
                "Setting", sideNav
            )
            changeNonSelectedNavUi(
                arrayListOf(
                    "Home",
                    "History",
                    "Order",
                    "Logout"
                ), sideNav
            )
        }
    }
*/
   /* private fun setHeaderText(headerText: String) {
        viewDataBinding?.headerLayout?.headerLayout?.visibility = View.VISIBLE
        viewDataBinding?.headerLayout?.txtHeader?.text = headerText
    }*/
/*
    private fun changeNonSelectedNavUi(stringsList: ArrayList<String>, sideNav: SideNavBinding) {
        stringsList.forEach { selected ->
            when (selected) {
                "Home" -> {
                    unSelectedUI(sideNav.layoutHome, sideNav.txtDashboard, sideNav.imgHome)
                }

                "History" -> {
                    unSelectedUI(sideNav.layoutHistory, sideNav.txtHistory, sideNav.imgHistory)
                }

                "Order" -> {
                    unSelectedUI(sideNav.layoutOrders, sideNav.txtOrder, sideNav.imgOrder)
                }

                "Logout" -> {
                    unSelectedUI(sideNav.layoutLogout, sideNav.txtLogout, sideNav.imgLogout)
                }

                "Setting" -> {
                    unSelectedUI(sideNav.layoutSetting, sideNav.txtSetting, sideNav.imgSetting)
                }
            }
        }
    }*/

  /*  private fun unSelectedUI(
        layout: LinearLayoutCompat,
        txtHead: AppCompatTextView,
        icon: AppCompatImageView
    ) {
        layout.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.primaryBlue
            )
        )
        txtHead.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        icon.imageTintList =
            ContextCompat.getColorStateList(applicationContext, R.color.white)
    }*/

    /*private fun changeSelectedNavUi(
        selected: String,
        sideNav: SideNavBinding,
    ) {
        when (selected) {
            "Home" -> {
                selectedUI(sideNav.layoutHome, sideNav.txtDashboard, sideNav.imgHome)
            }

            "History" -> {
                selectedUI(sideNav.layoutHistory, sideNav.txtHistory, sideNav.imgHistory)
            }

            "Order" -> {
                selectedUI(sideNav.layoutOrders, sideNav.txtOrder, sideNav.imgOrder)
            }

            "Logout" -> {
                selectedUI(sideNav.layoutLogout, sideNav.txtLogout, sideNav.imgLogout)
            }

            "Setting" -> {
                selectedUI(sideNav.layoutSetting, sideNav.txtSetting, sideNav.imgSetting)
            }

            else -> {
                selectedUI(sideNav.layoutHome, sideNav.txtDashboard, sideNav.imgHome)
            }
        }

    }*/

/*    private fun selectedUI(
        layout: LinearLayoutCompat,
        txtHead: AppCompatTextView,
        icon: AppCompatImageView
    ) {
        layout.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        txtHead.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.primaryBlue
            )
        )
        icon.imageTintList =
            ContextCompat.getColorStateList(applicationContext, R.color.primaryBlue)
    }*/

/*    private fun jumpToAnotherFragment(navigationTaskProfile: Int, bundle: Bundle) {
        navController.navigate(navigationTaskProfile, bundle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }*/

    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                val bundle = Bundle()
                //jumpToAnotherFragment(R.id.navigation_dashboard, bundle)
            }

            R.id.navigation_notificationFragment -> {
                val bundle = Bundle()
                //jumpToAnotherFragment(R.id.navigation_notificationFragment, bundle)
            }

            else -> return false
        }
        return true
    }*/

    /*override fun onBackPressed() {
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

*/
   /* private fun verifyLogout() {
        val mBuilder = android.app.AlertDialog.Builder(this)

        val view = LogoutAlertBinding.inflate(layoutInflater)
        mBuilder.setView(view.root);
        view.NoButtonLabel.setOnClickListener{

        }
        view.YesButtonLabel.setOnClickListener{

        }
        val dialog: android.app.AlertDialog? = mBuilder.create()
        dialog?.show()

    }*/

    /*private fun newOrderAlert() {
        val mBuilder = android.app.AlertDialog.Builder(this)

        val view = NewOrderAlertBinding.inflate(layoutInflater)
        mBuilder.setView(view.root);

        val dialog: android.app.AlertDialog? = mBuilder.create()
        dialog?.show()

    }*/

/*}*/

