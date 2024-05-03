package com.rf.accessAli.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.rf.accessAli.R
import com.rf.accessAli.databinding.ActivityDashboardBinding
import com.rf.accessAli.roomDB.model.UniversityData
import com.rf.accessAli.sdkInit.AccessAliSDK
import com.rf.accessAli.ui.base.BaseActivity
import com.rf.accessAli.ui.base.BaseActivityModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.adapter.OrderListAdapter
import com.rf.accessAli.ui.main.di.DaggerDashBoardActivityComponent
import com.rf.accessAli.ui.main.di.DashBoardActivityModule
import com.rf.accessAli.ui.main.viewmodel.DashBoardViewModel
import com.rf.accessAli.utils.NetworkHelper
import com.rf.accessAli.utils.SharedPreference
import com.rf.accessAli.utils.Status
import javax.inject.Inject


class DashBoardActivity : BaseActivity<ActivityDashboardBinding>(R.layout.activity_dashboard) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by viewModels { dashBoardViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initializationDagger()
        //generateQRCode()
        initialization()
    }

    private fun initialization() {
        universityDataAPI()

    }

    private fun initializationDagger() {
        DaggerDashBoardActivityComponent.builder().appComponent(AccessAliSDK.appComponent)
            .dashBoardActivityModule(DashBoardActivityModule())
            .baseActivityModule(BaseActivityModule(this@DashBoardActivity)).build()
            .inject(this)
    }

    override fun onResume() {
        super.onResume()
        /*viewModel.getExpireHour()?.let { updateValidityTime(it) }
        challanNumber = Utility.generateRandomChallanString(12)
        viewDataBinding?.txtEchallanNumber?.text = getString(R.string.challan_number, challanNumber)*/
    }


    private fun universityDataAPI() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getUniversityData()

            viewModel.universityResponseModel.observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        viewDataBinding?.progressBar?.visibility = View.GONE
                        val responseData : List<UniversityData>? = it.data
                        Log.i("jhvkyvknkjnilb", responseData?.get(0)?.name.toString())

                        if (responseData != null) {
                            viewModel.insertUniversityData(responseData)
                        }
                        val adapter = responseData?.let { it1 -> OrderListAdapter(this@DashBoardActivity,it1) }
                        viewDataBinding?.listView?.layoutManager = LinearLayoutManager(applicationContext)
                        adapter?.listener = cardListener
                        viewDataBinding?.listView?.adapter = adapter
                    }

                    Status.ERROR -> {
                        viewDataBinding?.progressBar?.visibility = View.GONE
                        viewModel.getUniversityDataDB()
                    }

                    Status.LOADING -> {
                        viewDataBinding?.progressBar?.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            viewDataBinding?.progressBar?.visibility = View.GONE
            showMessage(getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private val cardListener = object : OrderListAdapter.CardEvent {
        override fun onCardClicked(data: UniversityData) {
            if (data != null) {
                val intent = Intent(this@DashBoardActivity, FinalFormActivity::class.java)
                intent.putExtra("Data", data)
                startActivity(intent)

            }
        }
    }
}

