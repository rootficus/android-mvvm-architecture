package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.dailyReportData.QuestionData
import com.rf.macgyver.data.model.request.dailyReportData.Step1DrData
import com.rf.macgyver.data.model.request.dailyReportData.Step2DrData
import com.rf.macgyver.data.model.request.dailyReportData.Step3DrData
import com.rf.macgyver.databinding.FragmentStep3DRBinding
import com.rf.macgyver.databinding.SuccessAlertDrBinding
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.roomDB.model.DailyReporting
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep3DRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.PdfGeneratorDR.createDrPdf
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step3DRFragment : BaseFragment<FragmentStep3DRBinding>(R.layout.fragment_step3_d_r) {

    private val database = context?.let { MagDatabase.getDatabase(it) }
    private val dao = database?.magDao()

    private var step3DrData : Step3DrData = Step3DrData()
    private lateinit var downtimeSpinnerVal : String
    private lateinit var runtimeSpinnerValue : String
    private lateinit var worklogSpinnerValue : String

    private val dataList : ArrayList<Step2DrData> = arrayListOf()

    private var downtimeData = arrayOf("hours", "minutes")
    private var runtimeData = arrayOf("hours", "minutes")
    private var worklogData = arrayOf("hours", "minutes")

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewmodel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private var isPasswordVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView()

    }

    private fun initializeView() {

        val bundle = arguments

        val step1Data: Step1DrData? =
            bundle?.getSerializable("step1Data") as? Step1DrData

        val uniqueToken: String? =
            bundle?.getString("uniqueToken")
        val step2Q1Data : QuestionData? = bundle?.getSerializable("step2Q1Data") as? QuestionData
        val step2Q2Data : QuestionData? = bundle?.getSerializable("step2Q2Data") as? QuestionData
        val step2Q3Data : QuestionData? = bundle?.getSerializable("step2Q3Data") as? QuestionData
        val step2Q4Data : QuestionData? = bundle?.getSerializable("step2Q4Data") as? QuestionData
        val step2Q5Data : QuestionData? = bundle?.getSerializable("step2Q5Data") as? QuestionData
        val step2Q6Data : QuestionData? = bundle?.getSerializable("step2Q6Data") as? QuestionData
        val step2Q7Data : QuestionData? = bundle?.getSerializable("step2Q7Data") as? QuestionData
        val step2Q8Data : QuestionData? = bundle?.getSerializable("step2Q8Data") as? QuestionData
        val step2Q9Data : QuestionData? = bundle?.getSerializable("step2Q9Data") as? QuestionData


        val step2DrDataQ1 = Step2DrData(step2Q1Data)
        val step2DrDataQ2 = Step2DrData(step2Q2Data)
        val step2DrDataQ3 = Step2DrData(step2Q3Data)
        val step2DrDataQ4 = Step2DrData(step2Q4Data)
        val step2DrDataQ5 = Step2DrData(step2Q5Data)
        val step2DrDataQ6 = Step2DrData(step2Q6Data)
        val step2DrDataQ7 = Step2DrData(step2Q7Data)
        val step2DrDataQ8 = Step2DrData(step2Q8Data)
        val step2DrDataQ9 = Step2DrData(step2Q9Data)

        step2DrDataQ1.let { it1 -> dataList.add(it1) }
        step2DrDataQ2.let { it1 -> dataList.add(it1) }
        step2DrDataQ3.let { it1 -> dataList.add(it1) }
        step2DrDataQ4.let { it1 -> dataList.add(it1) }
        step2DrDataQ5.let { it1 -> dataList.add(it1) }
        step2DrDataQ6.let { it1 -> dataList.add(it1) }
        step2DrDataQ7.let { it1 -> dataList.add(it1) }
        step2DrDataQ8.let { it1 -> dataList.add(it1) }
        step2DrDataQ9.let { it1 -> dataList.add(it1) }


        mDataBinding.generateReportTxt.setOnClickListener {

            downtimeSpinnerVal = mDataBinding.downtimeSpinner.selectedItem.toString()
            runtimeSpinnerValue = mDataBinding.runtimeSpinner.selectedItem.toString()
            worklogSpinnerValue = mDataBinding.worklohSpinner.selectedItem.toString()
            val string1 = mDataBinding.downtimeEdittext.text.toString()
            val string2 = mDataBinding.runtimeEdittext.text.toString() +""+ runtimeSpinnerValue
            val string3 = mDataBinding.worklogEdittext.text.toString() +""+ worklogSpinnerValue

            step3DrData.vehicleDowntime = string1
            step3DrData.vehicleRuntime = string2
            step3DrData.worklog = string3
            step3DrData.downtimeNote = mDataBinding.downtimeNoteEdittext.text.toString()

            if(mDataBinding.downtimeEdittext.text.isNullOrEmpty() || mDataBinding.runtimeEdittext.text.isNullOrEmpty() ||
                mDataBinding.worklogEdittext.text.isNullOrEmpty() || step3DrData.downtimeNote.isNullOrEmpty()){
                Toast.makeText(context, "please enter all the details", Toast.LENGTH_SHORT).show()
            }else {
                val bundle1 = Bundle().apply {
                    putString("uniqueId", uniqueToken)
                }

                val mBuilder = AlertDialog.Builder(requireActivity())
                val view = SuccessAlertDrBinding.inflate(layoutInflater)
                mBuilder.setView(view.root)
                val dialog: AlertDialog = mBuilder.create()

                view.backTxt.setOnClickListener {
                    dialog.dismiss()
                }
                view.viewReportTxt.setOnClickListener {
                    val entity = step1Data?.reportName?.let { it1 -> DailyReporting(uniqueToken = uniqueToken,
                        reportName = it1, vehicleNo = step1Data.vehicle?.vehicleNo, name = step1Data.name, shift = step1Data.shift,
                        vehicleName = step1Data.vehicle?.vehicleName, date = step1Data.date, day = step1Data.day , vehicleDowntime = step3DrData.vehicleDowntime ,
                        vehicleRuntime = step3DrData.vehicleRuntime , vehicleWorkLog = step3DrData.worklog , downtimeNote = step3DrData.downtimeNote ,
                        question1 = step2Q1Data, question2 = step2Q2Data, question3 = step2Q3Data, question4 = step2Q4Data, question5 = step2Q5Data, question6 = step2Q6Data,
                        question7 = step2Q7Data, question8 = step2Q8Data, question9 = step2Q9Data) }

                    if (entity != null) {
                        viewmodel.insertDailyReporting(entity)
                        createDrPdf(requireActivity(), entity)
                    }

                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_step3_report_to_navigation_daily_report_home_page,bundle1)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backTxt.setOnClickListener {
            navController.navigateUp()
        }

        val spinner1Adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, downtimeData)
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.downtimeSpinner.adapter = spinner1Adapter

        val spinner2Adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, runtimeData)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.runtimeSpinner.adapter = spinner2Adapter

        val spinner3Adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, worklogData)
        spinner3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.worklohSpinner.adapter = spinner3Adapter

    }

    private fun initializeDagger() {
        DaggerStep3DRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }
}