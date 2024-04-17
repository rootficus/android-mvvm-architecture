package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.inspectionFormData.IPQuestionData
import com.rf.macgyver.data.model.request.inspectionFormData.InspectionFormData
import com.rf.macgyver.databinding.FragmentStep3IpBinding
import com.rf.macgyver.databinding.PopupInspectionStep3Binding
import com.rf.macgyver.databinding.SuccessAlertIrBinding
import com.rf.macgyver.roomDB.model.InspectionForm
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep3IPFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class Step3IPFragment : BaseFragment<FragmentStep3IpBinding>(R.layout.fragment_step3_ip) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var inspectionFormData= InspectionFormData()
    private var uniqueNo : Int = 0
    private var isDeployed : String? = null
    private var isSafe : String? = null
    private var vehicleStatus : String? = null
    private var priority : String? = null
    private var overallCond : String? = null
    private var uniqueToken : String? = null

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

        uniqueToken =
            bundle?.getString("uniqueToken")

        inspectionFormData = (bundle?.getSerializable("inspectionFormData") as? InspectionFormData)!!

        uniqueNo  = bundle.getInt("UniqueNo")

        val step1Q1Data : IPQuestionData? = bundle.getSerializable("step1Q1Data") as? IPQuestionData
        val step1Q2Data : IPQuestionData? = bundle.getSerializable("step1Q2Data") as? IPQuestionData
        val step1Q3Data : IPQuestionData? = bundle.getSerializable("step1Q3Data") as? IPQuestionData
        val step1Q4Data : IPQuestionData? = bundle.getSerializable("step1Q4Data") as? IPQuestionData
        val step1Q5Data : IPQuestionData? = bundle.getSerializable("step1Q5Data") as? IPQuestionData
        val step1Q6Data : IPQuestionData? = bundle.getSerializable("step1Q6Data") as? IPQuestionData
        val step1Q7Data : IPQuestionData? = bundle.getSerializable("step1Q7Data") as? IPQuestionData
        val step1Q8Data : IPQuestionData? = bundle.getSerializable("step1Q8Data") as? IPQuestionData
        val step1Q9Data : IPQuestionData? = bundle.getSerializable("step1Q9Data") as? IPQuestionData
        val step1Q10Data : IPQuestionData? = bundle.getSerializable("step1Q10Data") as? IPQuestionData


        var item =0
        var step1DataList : ArrayList<IPQuestionData?> = arrayListOf(step1Q10Data,step1Q2Data,step1Q3Data,step1Q4Data,step1Q5Data,
            step1Q6Data,step1Q7Data,step1Q8Data,step1Q9Data,step1Q10Data)

        for(step1 in step1DataList){
            if(step1?.selectedAnswer == "Faulty"){
                item++
            }
        }
        mDataBinding.faultyItemsNo.setText(item.toString())

        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backTxt.setOnClickListener {
            navController.navigateUp()
        }

        mDataBinding.availableBtn.setOnClickListener {
            mDataBinding.priorityLayout.visibility = View.GONE
            vehicleStatusCardSelect(mDataBinding.availableBtn)
        }
        mDataBinding.maintenanceRequiredId.setOnClickListener {
            mDataBinding.priorityLayout.visibility = View.VISIBLE
            vehicleStatusCardSelect(mDataBinding.maintenanceRequiredId)
        }
        mDataBinding.breakdownBtn.setOnClickListener {
            mDataBinding.priorityLayout.visibility = View.VISIBLE
            vehicleStatusCardSelect(mDataBinding.breakdownBtn)
        }

        mDataBinding.deployedYesBtn.setOnClickListener{
            deployedCardSelect(mDataBinding.deployedYesBtn)
        }
        mDataBinding.deplpyedNoBtn.setOnClickListener{
            deployedCardSelect(mDataBinding.deplpyedNoBtn)
        }


        mDataBinding.safeVehicleYesBtn.setOnClickListener{
            safeCardSelect(mDataBinding.safeVehicleYesBtn)
        }
        mDataBinding.safeVehicleNoBtn.setOnClickListener{
            safeCardSelect(mDataBinding.safeVehicleNoBtn)
        }

        mDataBinding.lowId.setOnClickListener{
            priorityCardSelect(mDataBinding.lowId)
        }
        mDataBinding.mediumId.setOnClickListener{
            priorityCardSelect(mDataBinding.mediumId)
        }
        mDataBinding.highId.setOnClickListener{
            priorityCardSelect(mDataBinding.highId)
        }
        mDataBinding.emergencyId.setOnClickListener{
            priorityCardSelect(mDataBinding.emergencyId)
        }

        val binding = PopupInspectionStep3Binding.inflate(layoutInflater)

        val popupWindow = PopupWindow(
            binding.root,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, true
        )

        mDataBinding.overallCondSelectId.setOnClickListener {

            val location = IntArray(2)
            mDataBinding.overallCondSelectId.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            popupWindow.showAtLocation(binding.radioGrp1, Gravity.NO_GRAVITY, x, y)

            }

        binding.radioGrp1.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = binding.root.findViewById(checkedId)
                overallCond = radio.text.toString()
                mDataBinding.overallCondSelectId.setText(overallCond)
            })


        mDataBinding.backOrGenerateCard.setOnClickListener {

            if(overallCond.isNullOrEmpty() || vehicleStatus.isNullOrEmpty() || isSafe.isNullOrEmpty()|| isDeployed.isNullOrEmpty()){
                Toast.makeText(context, "Please enter all the required information", Toast.LENGTH_SHORT).show()
            }else {
                val faultyItems = item.toString()
                val additionalNote = mDataBinding.etAdditionalNote.text.toString()
                val mBuilder = AlertDialog.Builder(requireActivity())
                val view1 = SuccessAlertIrBinding.inflate(layoutInflater)
                mBuilder.setView(view1.root)
                val dialog: AlertDialog = mBuilder.create()
                view1.backTxt.setOnClickListener {
                    dialog.dismiss()
                }
                view1.viewReportTxt.setOnClickListener {

                    val entity = InspectionForm(uniqueToken = uniqueToken,
                        uniqueId = uniqueNo, heading = inspectionFormData.title, question1 = step1Q1Data, question2 = step1Q2Data, question3 = step1Q3Data,
                        question4 = step1Q4Data, question5 = step1Q5Data, question6 = step1Q6Data, question7 = step1Q7Data, question8 = step1Q8Data,
                        question9 = step1Q9Data, question10 = step1Q10Data, faultyItems = faultyItems, additionalNote = additionalNote, isDeployed = isDeployed,
                        isSafeToUse = isSafe, vehicleStatus = vehicleStatus, priority = priority, overallCond = overallCond)
                    viewmodel.insertInspectionForm(entity)
                    val bundle1 = Bundle().apply {
                        putString("uniqueId", uniqueToken)
                    }
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_step3_inspection_to_navigation_inspection,bundle1)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    private fun initializeDagger() {
        DaggerStep3IPFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun vehicleStatusCardSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.availableBtn,
            mDataBinding.maintenanceRequiredId,
            mDataBinding.breakdownBtn)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        vehicleStatus = textview.text.toString()
    }

    private fun safeCardSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.safeVehicleYesBtn,
            mDataBinding.safeVehicleNoBtn)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        isSafe = textview.text.toString()

    }

    private fun priorityCardSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.lowId,
            mDataBinding.mediumId,
            mDataBinding.highId,
            mDataBinding.emergencyId)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        priority = textview.text.toString()

    }

    private fun deployedCardSelect(textview : TextView){
        setBgWhiteTextGray(mDataBinding.deployedYesBtn,
            mDataBinding.deplpyedNoBtn)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
         isDeployed = textview.text.toString()
    }

    private fun setBgWhiteTextGray(textview1: TextView, textview2: TextView, textview3: TextView, textview4 : TextView){

        val textviews = listOf<TextView>(textview1,textview2,textview3, textview4)
        for(textview in textviews){
            textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
            textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.lightgray))}
    }
    private fun setBgWhiteTextGray(textview1: TextView, textview2: TextView, textview3: TextView){

        val textviews = listOf<TextView>(textview1,textview2,textview3)
        for(textview in textviews){
            textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
            textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.lightgray))}
    }
    private fun setBgWhiteTextGray(textview1: TextView, textview2: TextView){

        val textviews = listOf<TextView>(textview1,textview2)
        for(textview in textviews){
            textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
            textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.lightgray))}
    }

}