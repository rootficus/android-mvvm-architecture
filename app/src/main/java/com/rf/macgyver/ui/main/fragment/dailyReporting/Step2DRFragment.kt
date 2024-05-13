package com.rf.macgyver.ui.main.fragment.dailyReporting

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.macgyver.BuildConfig
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
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class Step2DRFragment : BaseFragment<FragmentStep2DRBinding>(R.layout.fragment_step2_d_r) {

    private var step1Data :Serializable? = null

    private var dataList: ArrayList<Step2DrData> = arrayListOf()

    lateinit var view : AlertCamDrBinding

//    private val REQUEST_IMAGE_CAPTURE = 101

    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    private var photoFile: File? = null

    private var imgUri : ArrayList<Uri>? = arrayListOf()
    var imageBitmap : Bitmap? = null
    var imageUri : Uri? = null

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
        view = AlertCamDrBinding.inflate(layoutInflater)

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
            initializePlusButtonAlert(text, card1Data)
        }

        mDataBinding.motorBearingPlusBtn.setOnClickListener{
            text = mDataBinding.motorBearingHeading.text.toString()
            initializePlusButtonAlert(text, card2Data)
        }

        mDataBinding.coolerTempsPlusBtn.setOnClickListener{
            text = mDataBinding.coolerTempsHeading.text.toString()
            initializePlusButtonAlert(text,card3Data)
        }

        mDataBinding.ispectCouplerPlusBtn.setOnClickListener{
            text = mDataBinding.inspectCouplerHeading.text.toString()
            initializePlusButtonAlert(text,card4Data)
        }

        mDataBinding.engineTempsPlusBtn.setOnClickListener{
            text = mDataBinding.engineTempsHeading.text.toString()
            initializePlusButtonAlert(text,card5Data)
        }

        mDataBinding.coolantTempsPlusBtn.setOnClickListener{
            text = mDataBinding.coolantTempsHeading.text.toString()
            initializePlusButtonAlert(text,card6Data)
        }

        mDataBinding.engineStartUpPlusBtn.setOnClickListener{
            text = mDataBinding.engineHeading.text.toString()
            initializePlusButtonAlert(text,card7Data)
        }

        mDataBinding.vibrationPlusBtn.setOnClickListener{
            text = mDataBinding.vibrationHeading.text.toString()
            initializePlusButtonAlert(text,card8Data)
        }

        mDataBinding.smellPlusBtn.setOnClickListener{
            text = mDataBinding.smellHeading.text.toString()
            initializePlusButtonAlert(text,card9Data)
        }

        mDataBinding.engineCamBtn.setOnClickListener{
            initializeCamAlert(card7Data)
        }

        mDataBinding.vibrationCamBtn.setOnClickListener{
            initializeCamAlert(card8Data)
        }

        mDataBinding.smellCamBtn.setOnClickListener{
            initializeCamAlert(card9Data)
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

    private fun initializePlusButtonAlert(text :String, card : QuestionData){
        noteText = null
        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = PopupStep2DrBinding.inflate(layoutInflater)
        view.headingId.text = text
        mBuilder.setView(view.root)
        val dialog: AlertDialog = mBuilder.create()
        view.cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        view.DoneTxt.setOnClickListener {
            if(view.etNoteId.text.isNullOrEmpty()){
                Toast.makeText(context, "Please enter the note", Toast.LENGTH_SHORT).show()
            }else {
                noteText = view.etNoteId.text.toString()
                card.note = noteText
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    private fun initializeCamAlert(card: QuestionData){
        val mBuilder = AlertDialog.Builder(requireActivity())
        val view = AlertCamDrBinding.inflate(layoutInflater)
        mBuilder.setView(view.root)
        view.cam1.setOnClickListener {
            imageUri = null
            checkCameraPermissionAndCapture()
            /*view.imageview1.visibility = View.VISIBLE
            view.camPlus1.visibility = View.GONE
            view.imageview1.setImageURI(imageUri)*/
        }
        view.camPlus2.setOnClickListener {
            imageBitmap = null
            //dispatchTakePictureIntent()
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

    private fun checkCameraPermissionAndCapture() {
        if (hasCameraPermission()) {
            dispatchTakePictureIntent()
        } else {
            requestCameraPermission()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                // Permission denied, handle this case if needed
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                photoFile = createImageFile()
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            photoFile = this
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val takenImage = photoFile?.absolutePath
            view.cam1.setImageBitmap(BitmapFactory.decodeFile(takenImage))
            // Do something with the URI like saving it in a database
            imageUri = photoFile?.let { Uri.fromFile(it) }
            imgUri?.add(imageUri!!)

            // Now you can use takenImageUri to store it in a database or wherever you want.
        }
    }


   /* private fun permission() {
        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            val permissions =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, 121)
        } else openCamera()
    }*/

    /*private fun captureImage() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            requireActivity(),
                            "com.example.captureimage.fileprovider",
                            photoFile!!
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(requireContext(), ex.message.toString())
                }

            } else {
                displayMessage(requireContext(), "Null")
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, *//* prefix *//*
            ".jpg", *//* suffix *//*
            storageDir      *//* directory *//*
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val view = AlertCamDrBinding.inflate(layoutInflater)

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            view.cam1.setImageBitmap(myBitmap)
        } else {
            displayMessage(requireContext(), "Request cancelled or something went wrong.")
        }
    }
*/
/*
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
                //view.camPlus1.setImageBitmap(imageBitmap)
                //view.camPlus1.visibility = View.GONE
                imageUri = getImageUriFromBitmap(requireContext(), imageBitmap!!)
                imgUri?.add(imageUri!!)
                //view.imageview1.visibility = View.VISIBLE


                view.cam1.setImageURI(imageUri)
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
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001

        fun newInstance(): Step2DRFragment {
            return Step2DRFragment()
        }
    }*/


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