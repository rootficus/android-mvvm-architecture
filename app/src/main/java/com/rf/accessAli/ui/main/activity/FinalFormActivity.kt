package com.rf.accessAli.ui.main.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.rf.accessAli.R
import com.rf.accessAli.databinding.ActivityFinalFormBinding
import com.rf.accessAli.roomDB.model.UniversityData
import com.rf.accessAli.ui.base.BaseActivity
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.viewmodel.DashBoardViewModel
import com.rf.accessAli.utils.NetworkHelper
import com.rf.accessAli.utils.SharedPreference
import javax.inject.Inject


class
FinalFormActivity : BaseActivity<ActivityFinalFormBinding>(R.layout.activity_final_form) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by viewModels { dashBoardViewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val universityData: UniversityData = intent.getSerializableExtra("Data") as UniversityData


        viewDataBinding?.uniName?.text = universityData.name
        viewDataBinding?.uniState?.text = universityData.stateProvince
        viewDataBinding?.uniCountry?.text = universityData.country
    }

    override fun onResume() {
        super.onResume()
    }
}

