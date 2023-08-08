package com.jionex.agent.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.jionex.agent.R
import com.jionex.agent.data.model.request.GetModemsByFilterRequest
import com.jionex.agent.databinding.ActivityDashboardBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseActivity
import com.jionex.agent.ui.base.BaseActivityModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.adapter.DrawerAdapter
import com.jionex.agent.ui.main.di.DaggerDashBoardActivityComponent
import com.jionex.agent.ui.main.di.DashBoardActivityModule
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
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
    private var lastExpandedGroupPosition = -1
    private lateinit var toggle: ActionBarDrawerToggle
    var succes: Int? = 0
    var reject: Int? = 0
    var approve: Int? = 0
    var danger: Int? = 0
    var pending: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializationDagger()
        //dashBoardViewModel.checkNightTheme(true)
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
        header.findViewById<AppCompatTextView>(R.id.textAgentValueFullName).text =
            "${dashBoardViewModel.getFullName()}"
        header.findViewById<AppCompatTextView>(R.id.textAgentValuePinCode).text =
            "${dashBoardViewModel.getPinCode()}"
        getStatusCount()

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    private fun setDrawerData() {
        val expandableListDetail = HashMap<String, List<String>>()

        val bleManagerList: MutableList<String> = ArrayList()
        bleManagerList.add("All Transactions")
        bleManagerList.add("Success,$succes")
        bleManagerList.add("Pending,$pending")
        bleManagerList.add("Rejected,$reject")
        bleManagerList.add("Approved,$approve")
        bleManagerList.add("Danger,$danger")

        val modemsList: MutableList<String> = ArrayList()
        modemsList.add("Modem List")

        val smsList: MutableList<String> = ArrayList()
        smsList.add("All Sms")
        smsList.add("Cash In")
        smsList.add("B2B")

        expandableListDetail["Sms Inbox"] = smsList
        expandableListDetail["Balance Manager"] = bleManagerList
        expandableListDetail["Modems"] = modemsList

        val expandableListTitle = ArrayList<String>(expandableListDetail.keys.reversed())
        val drawerAdapter =
            DrawerAdapter(applicationContext, expandableListDetail, expandableListTitle)
        viewDataBinding?.sideNav?.evMenu?.setAdapter(drawerAdapter)
        drawerAdapter.listener = cardListener
        //Call Dash Board
        viewDataBinding?.sideNav?.textDash?.setOnClickListener {
            viewDataBinding?.drawerLayout?.close()
            dashBoardViewModel.checkNightTheme(false)
            //navController.navigate(R.id.navigation_dashboard)
        }
        viewDataBinding?.sideNav?.evMenu?.setOnGroupExpandListener { groupPosition ->
            if (lastExpandedGroupPosition != -1 && lastExpandedGroupPosition != groupPosition) {
                // Collapse the previously expanded group
                viewDataBinding?.sideNav?.evMenu?.collapseGroup(lastExpandedGroupPosition);
            }
            lastExpandedGroupPosition = groupPosition;
        }

        viewDataBinding?.sideNav?.evMenu?.setOnGroupCollapseListener { groupPosition ->

        }

        viewDataBinding?.sideNav?.evMenu?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            false
        }

    }

    private fun jumpToAnotherFragment(navigationTaskProfile: Int, bundle: Bundle) {
        viewDataBinding?.drawerLayout?.close()
        navController.navigate(navigationTaskProfile, bundle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {

            else -> return false
        }
        return true
    }

    private val cardListener = object : DrawerAdapter.OnClickListener {
        override fun click(text: String) {

            when (text) {
                "All Transactions" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "All Transactions")
                        putInt("Filer", -1)
                    }
                    jumpToAnotherFragment(R.id.navigation_blManager, bundle)
                }

                "Success" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "Success")
                        putInt("Filer", 0)
                    }
                    jumpToAnotherFragment(R.id.navigation_blManager, bundle)
                }

                "Pending" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "Pending")
                        putInt("Filer", 1)
                    }
                    jumpToAnotherFragment(R.id.navigation_blManager, bundle)
                }

                "Rejected" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "Rejected")
                        putInt("Filer", 3)
                    }
                    jumpToAnotherFragment(R.id.navigation_blManager, bundle)
                }

                "Approved" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "Approved")
                        putInt("Filer", 4)
                    }
                    jumpToAnotherFragment(R.id.navigation_blManager, bundle)
                }

                "Danger" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "Danger")
                        putInt("Filer", 5)
                    }
                    jumpToAnotherFragment(R.id.navigation_blManager, bundle)
                }

                "Modem List" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "Modem List")
                    }
                    jumpToAnotherFragment(R.id.navigation_modemFragment, bundle)
                }

                "All Sms" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "All Sms")
                        putInt("Filer", -1)
                    }
                    jumpToAnotherFragment(R.id.navigation_smsInboxFragment, bundle)
                }

                "Cash In" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "Cash In")
                        putInt("Filer", 0)
                    }
                    jumpToAnotherFragment(R.id.navigation_smsInboxFragment, bundle)
                }

                "B2B" -> {
                    val bundle = Bundle().apply {
                        putString("Api", "B2B")
                        putInt("Filer", 2)
                    }
                    jumpToAnotherFragment(R.id.navigation_smsInboxFragment, bundle)
                }
            }
        }

    }

    private fun getStatusCount() {

        if (networkHelper.isNetworkConnected()) {

            dashBoardViewModel.getStatusCount()
            dashBoardViewModel.getStatusCountResponseModel.observe(this@DashBoardActivity) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val getStatusCountResponse = it.data
                        Log.i("Status Count","::${getStatusCountResponse?.approved}")
                        succes= getStatusCountResponse?.success
                        approve= getStatusCountResponse?.approved
                        danger= getStatusCountResponse?.danger
                        pending= getStatusCountResponse?.pending
                        reject=   10///getStatusCountResponse?.rejected
                        setDrawerData()
                    }

                    Status.ERROR -> {
                        Log.i("Status Count","Error")
                        setDrawerData()
                    }

                    Status.LOADING -> {
                    }
                }
            }
        } else {
            showMessage(getString(R.string.NO_INTERNET_CONNECTION))
        }
    }
}

