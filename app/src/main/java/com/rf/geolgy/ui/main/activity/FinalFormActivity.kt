package com.rf.geolgy.ui.main.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.rf.geolgy.R
import com.rf.geolgy.data.model.response.SignInResponse
import com.rf.geolgy.databinding.ActivityFinalFormBinding
import com.rf.geolgy.sdkInit.GeolgySDK
import com.rf.geolgy.ui.base.BaseActivity
import com.rf.geolgy.ui.base.BaseActivityModule
import com.rf.geolgy.ui.base.BaseViewModelFactory
import com.rf.geolgy.ui.main.di.DaggerFinalFormActivityComponent
import com.rf.geolgy.ui.main.di.FinalFormActivityModule
import com.rf.geolgy.ui.main.viewmodel.DashBoardViewModel
import com.rf.geolgy.utils.NetworkHelper
import com.rf.geolgy.utils.SharedPreference
import com.rf.geolgy.utils.Utility.makeTextBold
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class FinalFormActivity : BaseActivity<ActivityFinalFormBinding>(R.layout.activity_final_form) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by viewModels { dashBoardViewModelFactory }

    private lateinit var printWeb: WebView
    var printJob: PrintJob? = null
    var printBtnPressed = false
    var point6 = ""
    var point7 = ""
    var point8 = ""
    var point10 = ""
    var pointTwo10 = ""
    var point13 = ""
    var point14 = ""
    var point15 = ""
    var pointTwo15 = ""
    var challanNumber = ""
    var gstNumber = ""
    private var printManager: PrintManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        printManager = getSystemService(PRINT_SERVICE) as PrintManager?
        initializationDagger()
        val gson = Gson()
        val json: String? = viewModel.getSignInDataModel()
        val signInResponse: SignInResponse = gson.fromJson(json, SignInResponse::class.java)
        val intent = intent.extras
        point6 = intent?.getString("point6").toString()
        point7 = intent?.getString("point7").toString()
        point8 = intent?.getString("point8").toString()
        point10 = intent?.getString("point10").toString()
        pointTwo10 = intent?.getString("2point10").toString()
        point13 = intent?.getString("point13").toString()
        point14 = intent?.getString("point14").toString()
        point15 = intent?.getString("point15").toString()
        pointTwo15 = intent?.getString("2point15").toString()
        challanNumber = intent?.getString("challanNumber").toString()
        gstNumber = intent?.getString("gstNumber").toString()

        val permitStartDate: String = signInResponse.company?.permitStartDate.toString()
        val permitEndDate: String = signInResponse.company?.permitEndDate.toString()
        val rateOfMineral: String = signInResponse.company?.rateOfMineral.toString()
        val rateOfMineralTotal: String = signInResponse.company?.rateOfMineralTotal.toString()
        val qualityPercentage: String = signInResponse.company?.quantityPercentage.toString()
        val qualityAmount: String = signInResponse.company?.quantityAmount.toString()
        viewDataBinding?.txtValidity?.text = getString(R.string.validity_from)
        viewDataBinding?.edtPoint1?.text = signInResponse.company?.licenceType

        val point2Text1 = "Issuing date <b>$permitStartDate</b> Valid upto <b>$permitEndDate</b>"
        val point11Text =
            "Rate of Mineral GST <b>Rs.$rateOfMineral</b> Total Amount (Excluding GST and Transportation charges) <b>Rs.$rateOfMineralTotal</b>"
        val point12Text =
            "GST Bill/No. <b>$gstNumber</b> Quantity <b>$qualityPercentage%</b> Amount <b>Rs.$qualityAmount</b> (Enclose copy of GST Invoice)"
        viewDataBinding?.txt2Point1?.text = makeTextBold(point2Text1)
        viewDataBinding?.txtPoint11?.text = makeTextBold(point11Text)
        viewDataBinding?.txtPoint12?.text = makeTextBold(point12Text)


        viewDataBinding?.edtPoint6?.text = point6
        viewDataBinding?.edtPoint7?.text = point7
        viewDataBinding?.edtPoint8?.text = point8
        viewDataBinding?.edtPoint10?.text = point10
        viewDataBinding?.edt2Point10?.text = pointTwo10
        viewDataBinding?.edtPoint13?.text = point13
        viewDataBinding?.edtPoint14?.text = point14
        viewDataBinding?.edtPoint15?.text = point15
        viewDataBinding?.edt2Point15?.text = pointTwo15
        viewDataBinding?.txtEchallanNumber?.text = getString(R.string.challan_number, challanNumber)

        //Generate QR Code
        generateQRCode()
        // Check storage permission and generate PDF
        viewDataBinding?.webview?.settings?.javaScriptEnabled = true  // Enable JavaScript if needed
        // Setting we View Client
        viewDataBinding?.webview?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // initializing the printWeb Object
                printWeb = view
            }
        }
        // loading the URL
        viewDataBinding?.webview?.loadUrl("https://geolgyminingjk.in/challan/$challanNumber")
        viewDataBinding?.webview?.visibility = View.GONE
        viewDataBinding?.btnPrint?.setOnClickListener {
            viewDataBinding?.progressBar?.visibility = View.VISIBLE
            if (printWeb != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Calling createWebPrintJob()
                    printTheWebPage(printWeb);
                }
            } else {
                // Showing Toast message to user
                Toast.makeText(this, "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
            }
        }

        viewDataBinding?.btnLogout?.setOnClickListener {
            verifyLogout()
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun printTheWebPage(webView: WebView) {

        // set printBtnPressed true
        printBtnPressed = true

        // Creating  PrintManager instance
        val printManager = this
            .getSystemService(PRINT_SERVICE) as PrintManager

        // setting the name of job
        val jobName = "echallan_jk08y-$challanNumber"
        viewDataBinding?.progressBar?.visibility = View.GONE
        // Creating  PrintDocumentAdapter instance
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        val printAttributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .build()


        assert(printManager != null)
        printJob = printManager.print(
            jobName, printAdapter,
            printAttributes
        )
    }

    override fun onResume() {
        super.onResume()
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        calendar.add(Calendar.HOUR_OF_DAY, 3)
        val futureTime = calendar.time
        viewDataBinding?.txtValidity?.text =
            "Validity from ${sdf.format(currentTime)} to ${sdf.format(futureTime)}"
        viewDataBinding?.txtPoint9?.text =
            "DATE & TIME of dispatch ${sdf.format(currentTime)} to ${sdf.format(futureTime)} (Valid upto 3 Hours)"

        if (printJob != null && printBtnPressed) {
            if (printJob!!.isCompleted) {
                // Showing Toast Message
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show()
                //sharePDFViaWhatsApp(printJob)
            } else if (printJob!!.isStarted) {
                // Showing Toast Message
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isBlocked) {
                // Showing Toast Message
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isCancelled) {
                // Showing Toast Message
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isFailed) {
                // Showing Toast Message
                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isQueued) {
                // Showing Toast Message
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show()
            }
            // set printBtnPressed false
            printBtnPressed = false
        }
    }


    private fun generateQRCode() {
        val bitmap: Bitmap? =
            generateQRCode(
                "https://geolgyminingjk.in/challan/$challanNumber",
                ContextCompat.getColor(applicationContext, R.color.qrCode)
            )
        // Set QR Code to ImageView
        bitmap?.let {
            viewDataBinding?.imgQRCode?.setImageBitmap(it)
        }

    }

    private fun generateQRCode(text: String, qrCodeColor: Int): Bitmap? {
        val writer = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 1000, 1000)
            val width: Int = bitMatrix.width
            val height: Int = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) qrCodeColor else Color.TRANSPARENT)

                }
            }
            return bmp
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    private fun initializationDagger() {
        DaggerFinalFormActivityComponent.builder().appComponent(GeolgySDK.appComponent)
            .finalFormActivityModule(FinalFormActivityModule())
            .baseActivityModule(BaseActivityModule(this@FinalFormActivity)).build()
            .inject(this)
    }

    private fun verifyLogout() {
        val mBuilder = android.app.AlertDialog.Builder(this@FinalFormActivity)
            .setTitle(getString(R.string.log_out))
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton(getString(R.string.yes), null)
            .setNegativeButton(getString(R.string.no), null)
            .show()
        val mPositiveButton = mBuilder.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            mBuilder.dismiss()
            viewModel.setSignInDataModel("")
            viewModel.setPassword("")
            viewModel.setIsLogin(false)
            viewModel.setToken("")
            viewModel.setFullName("")
            startActivity(Intent(this@FinalFormActivity, SignInActivity::class.java))
            finishAffinity()
        }
    }
}

