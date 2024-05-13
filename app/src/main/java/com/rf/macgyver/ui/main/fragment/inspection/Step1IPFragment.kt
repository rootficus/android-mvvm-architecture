package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.dailyReportData.QuestionData
import com.rf.macgyver.data.model.request.inspectionFormData.IPQuestionData
import com.rf.macgyver.data.model.request.inspectionFormData.InspectionFormData
import com.rf.macgyver.databinding.AlertCamDrBinding
import com.rf.macgyver.databinding.AlertCamIpBinding
import com.rf.macgyver.databinding.FragmentStep1IpBinding
import com.rf.macgyver.databinding.PopupStep2IpBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerStep1IPFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.fragment.dailyReporting.Step1DRFragment
import com.rf.macgyver.ui.main.fragment.dailyReporting.Step2DRFragment
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class Step1IPFragment : BaseFragment<FragmentStep1IpBinding>(R.layout.fragment_step1_ip) {

    @Inject
    lateinit var sharedPreference: SharedPreference
    private var inspectionFormData= InspectionFormData()
    private var uniqueNo : Int = 0
    private var uniqueToken : String? = null

    private val REQUEST_IMAGE_CAPTURE = 101

    private var imgUri : ArrayList<Uri>? = arrayListOf()
    var imageBitmap : Bitmap? = null
    var imageUri : Uri? = null
    private  var ques1Data : IPQuestionData = IPQuestionData()
    private  var ques2Data : IPQuestionData = IPQuestionData()
    private  var ques3Data : IPQuestionData = IPQuestionData()
    private  var ques4Data : IPQuestionData = IPQuestionData()
    private  var ques5Data : IPQuestionData = IPQuestionData()
    private  var ques6Data : IPQuestionData = IPQuestionData()
    private  var ques7Data : IPQuestionData = IPQuestionData()
    private  var ques8Data : IPQuestionData = IPQuestionData()
    private  var ques9Data : IPQuestionData = IPQuestionData()
    private  var ques10Data : IPQuestionData = IPQuestionData()


    private var noteText :  String? = null
    private lateinit var text : String

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

        mDataBinding.nextTxt.setOnClickListener {
            ques1Data.title = mDataBinding.radiatorHeading.text.toString()
            ques4Data.title = mDataBinding.beltsHeading.text.toString()

            if(
                ques2Data.selectedAnswer.isNullOrEmpty() || ques3Data.selectedAnswer.isNullOrEmpty() || ques5Data.selectedAnswer.isNullOrEmpty() ||
                ques6Data.selectedAnswer.isNullOrEmpty() || ques7Data.selectedAnswer.isNullOrEmpty() || ques8Data.selectedAnswer.isNullOrEmpty() ||
                ques9Data.selectedAnswer.isNullOrEmpty()){
                Toast.makeText(context, "Select an option for each", Toast.LENGTH_SHORT).show()
            }else {

                val bundle1 = Bundle().apply {
                    putSerializable("step1Q1Data", ques1Data)
                    putSerializable("step1Q2Data", ques2Data)
                    putSerializable("step1Q3Data", ques3Data)
                    putSerializable("step1Q4Data", ques4Data)
                    putSerializable("step1Q5Data", ques5Data)
                    putSerializable("step1Q6Data", ques6Data)
                    putSerializable("step1Q7Data", ques7Data)
                    putSerializable("step1Q8Data", ques8Data)
                    putSerializable("step1Q9Data", ques9Data)
                    putSerializable("step1Q10Data", ques10Data)
                    putSerializable("inspectionFormData", inspectionFormData)
                    putInt("UniqueNo", uniqueNo)
                    putString("uniqueToken", uniqueToken)
                }
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_step1_inspection_to_navigation_step3_inspection, bundle1)
            }

        }
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.cancelTxt.setOnClickListener {
            navController.navigateUp()
        }

        //q2
        mDataBinding.card1OKtxt.setOnClickListener {
            card1TextSelect(mDataBinding.card1OKtxt)
        }
        mDataBinding.card1FaultyTxt.setOnClickListener {
            card1TextSelect(mDataBinding.card1FaultyTxt)
        }
        mDataBinding.card1FaultyButOkTxt.setOnClickListener {
            card1TextSelect(mDataBinding.card1FaultyButOkTxt)
        }
        mDataBinding.card1NATxt.setOnClickListener {
            card1TextSelect(mDataBinding.card1NATxt)
        }

        //q3
        mDataBinding.card2SoftTxt.setOnClickListener {
            card2TextSelected(mDataBinding.card2SoftTxt)
        }
        mDataBinding.card2FirmTxt.setOnClickListener {
            card2TextSelected(mDataBinding.card2FirmTxt)
        }
        mDataBinding.card2LargeRocksTxt.setOnClickListener {
            card2TextSelected(mDataBinding.card2LargeRocksTxt)
        }
        mDataBinding.card8SlipperyTxt.setOnClickListener {
            card2TextSelected(mDataBinding.card8SlipperyTxt)
        }
        mDataBinding.card8NATxt.setOnClickListener {
            card2TextSelected(mDataBinding.card8NATxt)
        }

        //q5
        mDataBinding.card9OKtxt.setOnClickListener {
            card3TextSelect(mDataBinding.card9OKtxt)
        }
        mDataBinding.card9FaultyTxt.setOnClickListener {
            card3TextSelect(mDataBinding.card9FaultyTxt)
        }
        mDataBinding.card9FaultyButOkTxt.setOnClickListener {
            card3TextSelect(mDataBinding.card9FaultyButOkTxt)
        }
        mDataBinding.card9NATxt.setOnClickListener {
            card3TextSelect(mDataBinding.card9NATxt)
        }

        //q6
        mDataBinding.card10OKtxt.setOnClickListener {
            card4TextSelect(mDataBinding.card10OKtxt)
        }
        mDataBinding.card10FaultyTxt.setOnClickListener {
            card4TextSelect(mDataBinding.card10FaultyTxt)
        }
        mDataBinding.card10FaultyButOkTxt.setOnClickListener {
            card4TextSelect(mDataBinding.card10FaultyButOkTxt)
        }
        mDataBinding.card10NATxt.setOnClickListener {
            card4TextSelect(mDataBinding.card10NATxt)
        }

        //q7
        mDataBinding.card11OKtxt.setOnClickListener {
            card5TextSelect(mDataBinding.card11OKtxt)
        }
        mDataBinding.card11FaultyTxt.setOnClickListener {
            card5TextSelect(mDataBinding.card11FaultyTxt)
        }
        mDataBinding.card11FaultyButOkTxt.setOnClickListener {
            card5TextSelect(mDataBinding.card11FaultyButOkTxt)
        }
        mDataBinding.card11NATxt.setOnClickListener {
            card5TextSelect(mDataBinding.card11NATxt)
        }

        //q8
        mDataBinding.card12SoftTxt.setOnClickListener {
            card6TextSelect(mDataBinding.card12SoftTxt)
        }
        mDataBinding.card12FirmTxt.setOnClickListener {
            card6TextSelect(mDataBinding.card12FirmTxt)
        }
        mDataBinding.card12SlipperyTxt.setOnClickListener {
            card6TextSelect(mDataBinding.card12SlipperyTxt)
        }
        mDataBinding.card12LargeRocksTxt.setOnClickListener {
            card6TextSelect(mDataBinding.card12LargeRocksTxt)
        }
        mDataBinding.card12NATxt.setOnClickListener {
            card6TextSelect(mDataBinding.card12NATxt)
        }

        //q9
        mDataBinding.card13OKtxt.setOnClickListener {
            card7TextSelect(mDataBinding.card13OKtxt)
        }
        mDataBinding.card13FaultyTxt.setOnClickListener {
            card7TextSelect(mDataBinding.card13FaultyTxt)
        }
        mDataBinding.card13FaultyButOkTxt.setOnClickListener {
            card7TextSelect(mDataBinding.card13FaultyButOkTxt)
        }
        mDataBinding.card13NATxt.setOnClickListener {
            card7TextSelect(mDataBinding.card13NATxt)
        }

        //q10
        mDataBinding.card14OKtxt.setOnClickListener {
            card8TextSelect(mDataBinding.card14OKtxt)
        }
        mDataBinding.card14FaultyTxt.setOnClickListener {
            card8TextSelect(mDataBinding.card14FaultyTxt)
        }
        mDataBinding.card14FaultyButOkTxt.setOnClickListener {
            card8TextSelect(mDataBinding.card14FaultyButOkTxt)
        }
        mDataBinding.card14NATxt.setOnClickListener {
            card8TextSelect(mDataBinding.card14NATxt)
        }





        mDataBinding.bearingsPlusBtn.setOnClickListener{
            val text = mDataBinding.bearingsHeading.text.toString()
            initializePlusButtonAlert(text, ques6Data)
        }

        mDataBinding.smellPlusBtn.setOnClickListener{
            val text = mDataBinding.smellHeading.text.toString()
            initializePlusButtonAlert(text,ques5Data)
        }

        mDataBinding.engineStartUpPlusBtn.setOnClickListener{
            val text = mDataBinding.engineHeading.text.toString()
            initializePlusButtonAlert(text,ques2Data)
        }

        mDataBinding.vibrationPlusBtn.setOnClickListener{
            val text = mDataBinding.vibrationHeading.text.toString()
            initializePlusButtonAlert(text, ques3Data)
        }

        mDataBinding.hatchStartUpPlusBtn.setOnClickListener{
            val text = mDataBinding.hatchSHeading.text.toString()
            initializePlusButtonAlert(text, ques7Data)
        }

        mDataBinding.soundsPlusBtn.setOnClickListener{
            val text = mDataBinding.soundsHeading.text.toString()
            initializePlusButtonAlert(text, ques8Data)
        }

        mDataBinding.spinningPlusBtn.setOnClickListener{
            val text = mDataBinding.spinningHeading.text.toString()
            initializePlusButtonAlert(text,ques9Data)
        }

        mDataBinding.coolantPlusBtn.setOnClickListener{
            val text = mDataBinding.coolantHeading.text.toString()
            initializePlusButtonAlert(text,ques10Data)
        }
        mDataBinding.radiatorCamBtn.setOnClickListener{
            initializeCamAlert(ques1Data)
        }

        mDataBinding.beltsCamBtn.setOnClickListener{
            initializeCamAlert(ques4Data)
        }
    }

    private fun initializeDagger() {
        DaggerStep1IPFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializePlusButtonAlert(text :String, card: IPQuestionData){

        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = PopupStep2IpBinding.inflate(layoutInflater)
        view.headingId.text = text
        mBuilder.setView(view.root)
        val dialog: AlertDialog = mBuilder.create()
        view.cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        view.DoneTxt.setOnClickListener {
            noteText = view.etNoteId.text.toString()
            dialog.dismiss()
            card.note = noteText
        }
        dialog.show()
    }

    private fun initializeCamAlert(card: IPQuestionData){
        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = AlertCamDrBinding.inflate(layoutInflater)
        mBuilder.setView(view.root)
        view.camPlus1.setOnClickListener {
            imageUri = null
            dispatchTakePictureIntent()
            view.imageview1.visibility = View.VISIBLE
            view.camPlus1.visibility = View.GONE
            view.imageview1.setImageURI(imageUri)
        }
        view.camPlus2.setOnClickListener {
            imageBitmap = null
            dispatchTakePictureIntent()
            view.imageview2.visibility = View.VISIBLE
            view.imageview2.setImageBitmap(imageBitmap)

        }
        val dialog: AlertDialog = mBuilder.create()
        view.cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        view.doneTxt.setOnClickListener {
            dialog.dismiss()
            card.uri = imgUri
            imgUri = null
        }
        dialog.show()

    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            // Request camera permission
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val view = AlertCamDrBinding.inflate(layoutInflater)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                view.camPlus1.setImageBitmap(imageBitmap)
                imageUri = getImageUriFromBitmap(requireContext(), imageBitmap!!)
                imgUri?.add(imageUri!!)
            } else {
                Log.e("onActivityResult", "Failed to retrieve image bitmap from data extras.")
            }
        } else {
            Log.e("onActivityResult", "Unexpected request code or result code.")
        }
    }
    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                dispatchTakePictureIntent()
            } else {
                // Permission denied
                // Handle permission denied case
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001

        fun newInstance(): Step1IPFragment {
            return Step1IPFragment()
        }
    }

    private fun card1TextSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.card1OKtxt,
            mDataBinding.card1FaultyTxt,
            mDataBinding.card1FaultyButOkTxt,
            mDataBinding.card1NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques2Data.title = mDataBinding.engineHeading.text.toString()
        ques2Data.selectedAnswer = textview.text.toString()
    }

    private fun card2TextSelected(textview : TextView){

        setBgWhiteTextGray(
            mDataBinding.card2SoftTxt,
            mDataBinding.card2FirmTxt,
            mDataBinding.card2LargeRocksTxt,
            mDataBinding.card8SlipperyTxt,
            mDataBinding.card8NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques3Data.title = mDataBinding.vibrationHeading.text.toString()
        ques3Data.selectedAnswer = textview.text.toString()
    }

    private fun card3TextSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.card9OKtxt,
            mDataBinding.card9FaultyTxt,
            mDataBinding.card9FaultyButOkTxt,
            mDataBinding.card9NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques5Data.title = mDataBinding.smellHeading.text.toString()
        ques5Data.selectedAnswer = textview.text.toString()
    }

    private fun card4TextSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.card10OKtxt,
            mDataBinding.card10FaultyTxt,
            mDataBinding.card10FaultyButOkTxt,
            mDataBinding.card10NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques6Data.title = mDataBinding.bearingsHeading.text.toString()
        ques6Data.selectedAnswer = textview.text.toString()
    }

    private fun card5TextSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.card11OKtxt,
            mDataBinding.card11FaultyTxt,
            mDataBinding.card11FaultyButOkTxt,
            mDataBinding.card11NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques7Data.title = mDataBinding.hatchSHeading.text.toString()
        ques7Data.selectedAnswer = textview.text.toString()
    }

    private fun card6TextSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.card12SoftTxt,
            mDataBinding.card12FirmTxt,
            mDataBinding.card12SlipperyTxt,
            mDataBinding.card12LargeRocksTxt,
            mDataBinding.card12NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques8Data.title = mDataBinding.soundsHeading.text.toString()
        ques8Data.selectedAnswer = textview.text.toString()
    }

    private fun card7TextSelect(textview : TextView){
        setBgWhiteTextGray(
            mDataBinding.card13OKtxt,
            mDataBinding.card13FaultyTxt,
            mDataBinding.card13FaultyButOkTxt,
            mDataBinding.card13NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques9Data.title = mDataBinding.spinningHeading.text.toString()
        ques9Data.selectedAnswer = textview.text.toString()

    }

    private fun card8TextSelect(textview : TextView){
        setBgWhiteTextGray(mDataBinding.card14OKtxt,
            mDataBinding.card14FaultyTxt,
            mDataBinding.card14FaultyButOkTxt,
            mDataBinding.card14NATxt)

        textview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lightBlue))
        textview.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        ques10Data.title = mDataBinding.coolantHeading.text.toString()
        ques10Data.selectedAnswer = textview.text.toString()
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