package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.dailyReportData.QuestionData
import com.rf.macgyver.data.model.request.dailyReportData.Step1DrData
import com.rf.macgyver.data.model.request.dailyReportData.Step2DrData
import com.rf.macgyver.databinding.AlertCamDrBinding
import com.rf.macgyver.databinding.FragmentStep2DRBinding
import com.rf.macgyver.databinding.PopupStep2DrBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep2DRFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import java.io.Serializable
import javax.inject.Inject

class Step2DRFragment : BaseFragment<FragmentStep2DRBinding>(R.layout.fragment_step2_d_r) {

    private var step1Data :Serializable? = null

    private var dataList: ArrayList<Step2DrData> = arrayListOf()

    private  var card1Data : QuestionData = QuestionData()
    private  var card2Data : QuestionData = QuestionData()
    private  var card3Data : QuestionData = QuestionData()
    private  var card4Data : QuestionData = QuestionData()
    private  var card5Data : QuestionData = QuestionData()
    private  var card6Data : QuestionData = QuestionData()
    private  var card7Data : QuestionData = QuestionData()
    private  var card8Data : QuestionData = QuestionData()
    private  var card9Data : QuestionData = QuestionData()


    private var noteText :  String? = null
    private lateinit var text : String
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
        val uniqueToken: String? =
            bundle?.getString("uniqueToken")

        val step1Data: Step1DrData? =
            bundle?.getSerializable("step1DrData") as? Step1DrData

        //Q1
        mDataBinding.card1HotTxt.setOnClickListener {
         card1TextSelect(mDataBinding.card1HotTxt)
        }
        mDataBinding.card1ColdTxt.setOnClickListener{
            card1TextSelect(mDataBinding.card1ColdTxt)
        }
        mDataBinding.card1WindyTxt.setOnClickListener{
            card1TextSelect(mDataBinding.card1WindyTxt)
        }
        mDataBinding.card1DownpourTxt.setOnClickListener{
            card1TextSelect(mDataBinding.card1DownpourTxt)
        }
        mDataBinding.card1NATxt.setOnClickListener{
            card1TextSelect(mDataBinding.card1NATxt)
        }

        //Q2
        mDataBinding.card2Softtxt.setOnClickListener{
            card2TextSelected(mDataBinding.card2Softtxt)
        }
        mDataBinding.card2Firmtxt.setOnClickListener{
            card2TextSelected(mDataBinding.card2Firmtxt)
        }
        mDataBinding.card2LargeRoxkstxt.setOnClickListener{
            card2TextSelected(mDataBinding.card2LargeRoxkstxt)
        }
        mDataBinding.card2SlipperyTxt.setOnClickListener{
            card2TextSelected(mDataBinding.card2SlipperyTxt)
        }
        mDataBinding.card2NATxt.setOnClickListener{
            card2TextSelected(mDataBinding.card2NATxt)
        }

        //Q3
        mDataBinding.card3OKtxt.setOnClickListener{
            card3TextSelect(mDataBinding.card3OKtxt)
        }
        mDataBinding.card3FaultyTxt.setOnClickListener{
            card3TextSelect(mDataBinding.card3FaultyTxt)
        }
        mDataBinding.card3FaultyButOkTxt.setOnClickListener{
            card3TextSelect(mDataBinding.card3FaultyButOkTxt)
        }
        mDataBinding.card3NATxt.setOnClickListener{
            card3TextSelect(mDataBinding.card3NATxt)
        }

        //Q4
        mDataBinding.card4OKtxt.setOnClickListener{
            card4TextSelect(mDataBinding.card4OKtxt)
        }
        mDataBinding.card4FaultyTxt.setOnClickListener{
            card4TextSelect(mDataBinding.card4FaultyTxt)
        }
        mDataBinding.card4FaultyButOkTxt.setOnClickListener{
            card4TextSelect(mDataBinding.card4FaultyButOkTxt)
        }
        mDataBinding.card4NATxt.setOnClickListener{
            card4TextSelect(mDataBinding.card4NATxt)
        }

        //Q5
        mDataBinding.card5OKtxt.setOnClickListener{
            card5TextSelect(mDataBinding.card5OKtxt)
        }
        mDataBinding.card5FaultyTxt.setOnClickListener{
            card5TextSelect(mDataBinding.card5FaultyTxt)
        }
        mDataBinding.card5FaultyButOkTxt.setOnClickListener{
            card5TextSelect(mDataBinding.card5FaultyButOkTxt)
        }
        mDataBinding.card5NATxt.setOnClickListener{
            card5TextSelect(mDataBinding.card5NATxt)
        }

        //Q6
        mDataBinding.card6OKtxt.setOnClickListener{
            card6TextSelect(mDataBinding.card6OKtxt)
        }
        mDataBinding.card6FaultyTxt.setOnClickListener{
            card6TextSelect(mDataBinding.card6FaultyTxt)
        }
        mDataBinding.card6FaultyButOkTxt.setOnClickListener{
            card6TextSelect(mDataBinding.card6FaultyButOkTxt)
        }
        mDataBinding.card6NATxt.setOnClickListener{
            card6TextSelect(mDataBinding.card6NATxt)
        }

        //Q7
        mDataBinding.card7OKtxt.setOnClickListener{
            card7TextSelect(mDataBinding.card7OKtxt)
        }
        mDataBinding.card7FaultyTxt.setOnClickListener{
            card7TextSelect(mDataBinding.card7FaultyTxt)
        }
        mDataBinding.card7FaultyButOkTxt.setOnClickListener{
            card7TextSelect(mDataBinding.card7FaultyButOkTxt)
        }
        mDataBinding.card7NATxt.setOnClickListener{
            card7TextSelect(mDataBinding.card7NATxt)
        }

        //Q8
        mDataBinding.card8Softtxt.setOnClickListener{
            card8TextSelect(mDataBinding.card8Softtxt)
        }
        mDataBinding.card8Firmtxt.setOnClickListener{
            card8TextSelect(mDataBinding.card8Firmtxt)
        }
        mDataBinding.card8LargeRockstxt.setOnClickListener{
            card8TextSelect(mDataBinding.card8LargeRockstxt)
        }
        mDataBinding.card8SlipperyTxt.setOnClickListener{
            card8TextSelect(mDataBinding.card8SlipperyTxt)
        }
        mDataBinding.card8NATxt.setOnClickListener{
            card8TextSelect(mDataBinding.card8NATxt)
        }

        //Q9
        mDataBinding.card9OKtxt.setOnClickListener{
            card9TextSelect(mDataBinding.card9OKtxt)
        }
        mDataBinding.card9FaultyTxt.setOnClickListener{
            card9TextSelect(mDataBinding.card9FaultyTxt)
        }
        mDataBinding.card9FaultyButOkTxt.setOnClickListener{
            card9TextSelect(mDataBinding.card9FaultyButOkTxt)
        }
        mDataBinding.card9NATxt.setOnClickListener{
            card9TextSelect(mDataBinding.card9NATxt)
        }


        mDataBinding.airCompressorPlusBtn.setOnClickListener{
            text = mDataBinding.airCompressorHeading.text.toString()
            initializePlusButtonAlert(text)
            card1Data.note = noteText.toString()

        }

        mDataBinding.motorBearingPlusBtn.setOnClickListener{
            text = mDataBinding.motorBearingHeading.text.toString()
            initializePlusButtonAlert(text)
            card2Data.note = noteText.toString()
        }

        mDataBinding.coolerTempsPlusBtn.setOnClickListener{
            text = mDataBinding.coolerTempsHeading.text.toString()
            initializePlusButtonAlert(text)
            card3Data.note = noteText.toString()
        }

        mDataBinding.ispectCouplerPlusBtn.setOnClickListener{
            text = mDataBinding.inspectCouplerHeading.text.toString()
            initializePlusButtonAlert(text)
            card4Data.note = noteText.toString()
        }

        mDataBinding.engineTempsPlusBtn.setOnClickListener{
            text = mDataBinding.engineTempsHeading.text.toString()
            initializePlusButtonAlert(text)
            card5Data.note = noteText.toString()
        }

        mDataBinding.coolantTempsPlusBtn.setOnClickListener{
            text = mDataBinding.coolantTempsHeading.text.toString()
            initializePlusButtonAlert(text)
            card6Data.note = noteText.toString()
        }

        mDataBinding.engineStartUpPlusBtn.setOnClickListener{
            text = mDataBinding.engineHeading.text.toString()
            initializePlusButtonAlert(text)
            card7Data.note = noteText.toString()
        }

        mDataBinding.vibrationPlusBtn.setOnClickListener{
            text = mDataBinding.vibrationHeading.text.toString()
            initializePlusButtonAlert(text)
            card8Data.note = noteText.toString()
        }

        mDataBinding.smellPlusBtn.setOnClickListener{
            text = mDataBinding.smellHeading.text.toString()
            initializePlusButtonAlert(text)
            card9Data.note = noteText.toString()
        }

        mDataBinding.engineCamBtn.setOnClickListener{
            initializeCamAlert()
        }

        mDataBinding.vibrationCamBtn.setOnClickListener{
            initializeCamAlert()
        }

        mDataBinding.smellCamBtn.setOnClickListener{
            initializeCamAlert()
        }
        val navController = Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.completedTxt.setOnClickListener{

            if(
                card1Data.selectedAnswer.isNullOrEmpty() || card2Data.selectedAnswer.isNullOrEmpty() || card3Data.selectedAnswer.isNullOrEmpty() ||
                card4Data.selectedAnswer.isNullOrEmpty() || card5Data.selectedAnswer.isNullOrEmpty() || card6Data.selectedAnswer.isNullOrEmpty() ||
                card7Data.selectedAnswer.isNullOrEmpty() || card8Data.selectedAnswer.isNullOrEmpty() || card9Data.selectedAnswer.isNullOrEmpty()
            ){
                Toast.makeText(context, "Select an option for each", Toast.LENGTH_SHORT).show()
            }else {
/*
                val step2DrDataQ1 = Step2DrData(card1Data)
                val step2DrDataQ2 = Step2DrData(card2Data)
                val step2DrDataQ3 = Step2DrData(card3Data)
                val step2DrDataQ4 = Step2DrData(card4Data)
                val step2DrDataQ5 = Step2DrData(card5Data)
                val step2DrDataQ6 = Step2DrData(card6Data)
                val step2DrDataQ7 = Step2DrData(card7Data)
                val step2DrDataQ8 = Step2DrData(card8Data)
                val step2DrDataQ9 = Step2DrData(card9Data)

                step2DrDataQ1.let { it1 -> dataList.add(it1) }
                step2DrDataQ2.let { it1 -> dataList.add(it1) }
                step2DrDataQ3.let { it1 -> dataList.add(it1) }
                step2DrDataQ4.let { it1 -> dataList.add(it1) }
                step2DrDataQ5.let { it1 -> dataList.add(it1) }
                step2DrDataQ6.let { it1 -> dataList.add(it1) }
                step2DrDataQ7.let { it1 -> dataList.add(it1) }
                step2DrDataQ8.let { it1 -> dataList.add(it1) }
                step2DrDataQ9.let { it1 -> dataList.add(it1) }*/

                val bundle1 = Bundle().apply {
                    putSerializable("step2Q1Data", card1Data)
                    putSerializable("step2Q2Data", card2Data)
                    putSerializable("step2Q3Data", card3Data)
                    putSerializable("step2Q4Data", card4Data)
                    putSerializable("step2Q5Data", card5Data)
                    putSerializable("step2Q6Data", card6Data)
                    putSerializable("step2Q7Data", card7Data)
                    putSerializable("step2Q8Data", card8Data)
                    putSerializable("step2Q9Data", card9Data)
                    putSerializable("step1Data", step1Data)
                    putString("uniqueToken",uniqueToken)
                }
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_step2_report_to_navigation_step3_report,bundle1)
            }
        }
        mDataBinding.backTxt.setOnClickListener{
            navController.navigateUp()
        }

    }

    private fun initializeDagger() {
        DaggerStep2DRFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializePlusButtonAlert(text :String){

        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = PopupStep2DrBinding.inflate(layoutInflater)
        view.headingId.text = text
        mBuilder.setView(view.root)
        val dialog: AlertDialog = mBuilder.create()
        view.cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        view.DoneTxt.setOnClickListener {
            noteText = view.noteTxt.text.toString()
            if(noteText?.isEmpty() == true){
                Toast.makeText(context, "Please enter the note", Toast.LENGTH_SHORT).show()
            }else {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    private fun initializeCamAlert(){
        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = AlertCamDrBinding.inflate(layoutInflater)
        mBuilder.setView(view.root)
        val dialog: AlertDialog = mBuilder.create()
        view.cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        view.doneTxt.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun card1TextSelect(textview : TextView){
        setBgWhiteTextGray(
        mDataBinding.card1HotTxt,
        mDataBinding.card1ColdTxt,
        mDataBinding.card1WindyTxt,
        mDataBinding.card1DownpourTxt,
        mDataBinding.card1NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card1Data.title = mDataBinding.airCompressorHeading.text.toString()
        card1Data.selectedAnswer = textview.text.toString()
    }

    private fun card2TextSelected(textview : TextView){

        setBgWhiteTextGray(
        mDataBinding.card2Softtxt,
        mDataBinding.card2Firmtxt,
        mDataBinding.card2LargeRoxkstxt,
        mDataBinding.card2SlipperyTxt,
        mDataBinding.card2NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card2Data.title = mDataBinding.motorBearingHeading.text.toString()
        card2Data.selectedAnswer = textview.text.toString()
    }

    private fun card3TextSelect(textview : TextView){
        setBgWhiteTextGray(
        mDataBinding.card3OKtxt,
        mDataBinding.card3FaultyTxt,
        mDataBinding.card3FaultyButOkTxt,
        mDataBinding.card3NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card3Data.title = mDataBinding.inspectCouplerHeading.text.toString()
        card3Data.selectedAnswer = textview.text.toString()
    }

    private fun card4TextSelect(textview : TextView){
        setBgWhiteTextGray(
        mDataBinding.card4OKtxt,
        mDataBinding.card4FaultyTxt,
        mDataBinding.card4FaultyButOkTxt,
        mDataBinding.card4NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card4Data.title = mDataBinding.coolantTempsHeading.text.toString()
        card4Data.selectedAnswer = textview.text.toString()
    }

    private fun card5TextSelect(textview : TextView){
    setBgWhiteTextGray(
        mDataBinding.card5OKtxt,
        mDataBinding.card5FaultyTxt,
        mDataBinding.card5FaultyButOkTxt,
        mDataBinding.card5NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card5Data.title = mDataBinding.engineTempsHeading.text.toString()
        card5Data.selectedAnswer = textview.text.toString()
    }

    private fun card6TextSelect(textview : TextView){
       setBgWhiteTextGray(
        mDataBinding.card6OKtxt,
        mDataBinding.card6FaultyTxt,
        mDataBinding.card6FaultyButOkTxt,
        mDataBinding.card6NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card6Data.title = mDataBinding.coolantTempsHeading.text.toString()
        card6Data.selectedAnswer = textview.text.toString()
    }

    private fun card7TextSelect(textview : TextView){
        setBgWhiteTextGray(
        mDataBinding.card7OKtxt,
        mDataBinding.card7FaultyTxt,
        mDataBinding.card7FaultyButOkTxt,
        mDataBinding.card7NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card7Data.title = mDataBinding.engineHeading.text.toString()
        card7Data.selectedAnswer = textview.text.toString()

    }

    private fun card8TextSelect(textview : TextView){
        setBgWhiteTextGray(mDataBinding.card8Softtxt,
        mDataBinding.card8Firmtxt,
        mDataBinding.card8LargeRockstxt,
            mDataBinding.card8SlipperyTxt,
        mDataBinding.card8NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card8Data.title = mDataBinding.engineHeading.text.toString()
        card8Data.selectedAnswer = textview.text.toString()
    }

    private fun card9TextSelect(textview : TextView){
        setBgWhiteTextGray(
        mDataBinding.card9OKtxt,
        mDataBinding.card9FaultyTxt,
        mDataBinding.card9FaultyButOkTxt,
        mDataBinding.card9NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        card9Data.title = mDataBinding.engineHeading.text.toString()
        card9Data.selectedAnswer = textview.text.toString()
    }

    private fun setBgWhiteTextGray(textview1: TextView,textview2: TextView,textview3: TextView,textview4: TextView){

        val textviews = listOf<TextView>(textview1,textview2,textview3,textview4)
        for(textview in textviews){
        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.lightgray))}
    }

    private fun setBgWhiteTextGray(textview1: TextView,textview2: TextView,textview3: TextView,textview4: TextView,textView5: TextView){

        val textviews = listOf<TextView>(textview1,textview2,textview3,textview4,textView5)
        for(textview in textviews){
            textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
            textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.lightgray))}
    }
}