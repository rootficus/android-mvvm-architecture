package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.Step1DrData
import com.rf.macgyver.data.model.request.Step2DrData
import com.rf.macgyver.data.model.request.Vehicle
import com.rf.macgyver.databinding.AddVehicleAlertBinding
import com.rf.macgyver.databinding.FragmentStep1DRBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep1DRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Step1DRFragment : BaseFragment<FragmentStep1DRBinding>(R.layout.fragment_step1_d_r) {

    private var step1DrData : Step1DrData = Step1DrData()

    private var vehicleData : Vehicle = Vehicle()

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
        mDataBinding.vehicleSelectTxt.setOnClickListener {
            val mBuilder = android.app.AlertDialog.Builder(requireActivity())
            val view = AddVehicleAlertBinding.inflate(layoutInflater)
            mBuilder.setView(view.root)
            val dialog: android.app.AlertDialog? = mBuilder.create()
            dialog?.show()

            view.saveBtn.setOnClickListener{
                vehicleData.vehicleName = view.etVehicleName.text.toString()
                vehicleData.vehicleNo = view.etVehicleNo.text.toString()

                step1DrData.vehicle = vehicleData
                if(vehicleData.vehicleName?.isEmpty() == true){
                    Toast.makeText(context, "Please enter vehicle Name", Toast.LENGTH_SHORT).show()
                } else if(vehicleData.vehicleNo?.isEmpty() == true){
                    Toast.makeText(context, "Please enter vehicle number", Toast.LENGTH_SHORT).show()
                }else{
                    mDataBinding.vehicleContent.setText(vehicleData.vehicleNo)
                    dialog?.dismiss()
                }
            }
        }

        mDataBinding.nextTxt.setOnClickListener {

            step1DrData.name = mDataBinding.nameId.text.toString()
            step1DrData.reportName = mDataBinding.reportName.text.toString()
            if(step1DrData.name?.isEmpty() == true){
                Toast.makeText(context, "Please Enter Name", Toast.LENGTH_SHORT).show()
            }
            else if(step1DrData.shift.isNullOrEmpty()){
                Toast.makeText(context, "Please select the shift", Toast.LENGTH_SHORT).show()
            }else if(step1DrData.vehicle?.vehicleNo.isNullOrEmpty()  || step1DrData.vehicle?.vehicleName.isNullOrEmpty()){
                Toast.makeText(context, "Please enter vehicle details", Toast.LENGTH_SHORT).show()
            }else
                {
                    val bundle = Bundle().apply {
                        putSerializable("step1DrData", step1DrData)
                    }
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_step1_report_to_navigation_step2_report,bundle)
                }
            }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.cancelTxt.setOnClickListener {
            navController.navigateUp()
        }

        val currentDate = getCurrentDate()
        step1DrData.date = "Date : $currentDate"
        mDataBinding.dateHeading.text = step1DrData.date
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        when(dayOfWeek){
            1 ->  {mDataBinding.Sundaytxt.setBackgroundColor(Color.GRAY)
            step1DrData.day = "Sunday"}
            2 -> {
                mDataBinding.Mtxt.setBackgroundColor(Color.GRAY)
                step1DrData.day = "Monday"}
            3 ->  {mDataBinding.Ttxt.setBackgroundColor(Color.GRAY)
                step1DrData.day = "Tuesday"}
            4 ->  {mDataBinding.Wtxt.setBackgroundColor(Color.GRAY)
                step1DrData.day = "Wednesday"}
            5 ->  {mDataBinding.Thurstxt.setBackgroundColor(Color.GRAY)
            step1DrData.day = "Thursday"}
            6 ->  {mDataBinding.Ftxt.setBackgroundColor(Color.GRAY)
                step1DrData.day = "Friday"}
            7 ->  {mDataBinding.Sattxt.setBackgroundColor(Color.GRAY)
                step1DrData.day = "Saturday"}

        }

        mDataBinding.shift1Btn.setOnClickListener{
            selectButton(mDataBinding.shift1Btn)
        }

        mDataBinding.shift2Btn.setOnClickListener{
            selectButton(mDataBinding.shift2Btn)
        }

        mDataBinding.shift3Btn.setOnClickListener{
            selectButton(mDataBinding.shift3Btn)
        }
    }

    private fun initializeDagger() {
        DaggerStep1DRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun selectButton(selectedButton: Button) {
        val buttons = listOf<Button>(mDataBinding.shift1Btn,mDataBinding.shift2Btn, mDataBinding.shift3Btn)
        for (button in buttons) {
            button.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
        }
        selectedButton.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightgray))
        step1DrData.shift = selectedButton.text.toString()
    }

}