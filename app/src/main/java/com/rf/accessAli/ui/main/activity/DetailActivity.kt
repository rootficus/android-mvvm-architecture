package com.rf.accessAli.ui.main.activity

import android.content.Intent
import android.os.Bundle
import com.rf.accessAli.R
import com.rf.accessAli.databinding.ActivityDetailBinding
import com.rf.accessAli.roomDB.model.UniversityData
import com.rf.accessAli.ui.base.BaseActivity
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.viewmodel.DashBoardViewModel
import com.rf.accessAli.utils.NetworkHelper
import com.rf.accessAli.utils.SharedPreference
import javax.inject.Inject

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val universityData: UniversityData = intent.getSerializableExtra("Data") as UniversityData
        viewDataBinding?.uniName?.text = universityData.name
        viewDataBinding?.uniState?.text = universityData.stateProvince
        viewDataBinding?.uniCountry?.text = universityData.country
        viewDataBinding?.webpage?.text = universityData.webpages?.get(0).toString()
        viewDataBinding?.countryCode?.text = universityData.alphaTwoCode
        viewDataBinding?.imgRefresh?.setOnClickListener{
            startActivity(Intent(this,DashBoardActivity::class.java))
        }
    }
}

