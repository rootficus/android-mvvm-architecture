package com.rf.accessAli.ui.main.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import com.rf.accessAli.R
import com.rf.accessAli.data.model.request.CreateChallanRequest
import com.rf.accessAli.data.model.response.CreateChallanResponse
import com.rf.accessAli.data.model.response.SignInResponse
import com.rf.accessAli.databinding.ActivityDashboardBinding
import com.rf.accessAli.sdkInit.AccessAliSDK
import com.rf.accessAli.ui.base.BaseActivity
import com.rf.accessAli.ui.base.BaseActivityModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.di.DaggerDashBoardActivityComponent
import com.rf.accessAli.ui.main.di.DashBoardActivityModule
import com.rf.accessAli.ui.main.viewmodel.DashBoardViewModel
import com.rf.accessAli.utils.NetworkHelper
import com.rf.accessAli.utils.SharedPreference
import com.rf.accessAli.utils.Status
import com.rf.accessAli.utils.Utility
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class DashBoardActivity : BaseActivity<ActivityDashboardBinding>(R.layout.activity_dashboard) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by viewModels { dashBoardViewModelFactory }
    private lateinit var challanNumber: String
    var validFrom: String = ""
    var validTo: String = ""
    var rateOfMineral: String = ""
    var rateOfMineralTotal: String = ""
    var expireInHours: Int = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initializationDagger()
        val gson = Gson()
        val json: String? = viewModel.getSignInDataModel()
        val signInResponse: SignInResponse = gson.fromJson(json, SignInResponse::class.java)
        generateQRCode()
        initialization(signInResponse)
    }

    private fun initialization(signInResponse: SignInResponse) {
        viewModel.getExpireHour()?.let {
            expireInHours = it
        }
        viewDataBinding?.spinnerExpire?.text = expireInHours.toString()
        addPopupMenu()
        val permitStartDate: String = signInResponse.company?.permitStartDate.toString()
        val permitEndDate: String = signInResponse.company?.permitEndDate.toString()
        rateOfMineral = signInResponse.company?.rateOfMineral.toString()
        rateOfMineralTotal = signInResponse.company?.rateOfMineralTotal.toString()
        //expireInHours = signInResponse.company?.expireInHours.toString()
        val qualityPercentage: String = signInResponse.company?.quantityPercentage.toString()
        val qualityAmount: String = signInResponse.company?.quantityAmount.toString()
        viewDataBinding?.txtValidity?.text = getString(R.string.validity_from)
        viewDataBinding?.edtPoint1?.text = signInResponse.company?.licenceType

        // Check if the device supports autofill
        val gstNumber = Utility.generateRandomGSTString(11)
        //GetShared Pref Edit Text Value
        getEditTextValues()
        val point2Text1 = "Issuing date <b>$permitStartDate</b> Valid upto <b>$permitEndDate</b>"
        val point11Text =
            "Rate of Mineral <b>Rs.$rateOfMineral</b> Total Amount (Excluding GST and Transportation charges) <b>Rs.$rateOfMineralTotal</b>"
        val point12Text =
            "GST Bill/No. <b>$gstNumber</b> Quantity <b>$qualityPercentage%</b> Amount <b>Rs.$qualityAmount</b> (Enclose copy of GST Invoice)"
        viewDataBinding?.txt2Point1?.text = makeTextBold(point2Text1)
        viewDataBinding?.txtPoint11?.text = makeTextBold(point11Text)
        viewDataBinding?.txtPoint12?.text = makeTextBold(point12Text)

        viewDataBinding?.btnSubmit?.setOnClickListener {
            val nameAndLocation = viewDataBinding?.edtPoint6?.text.toString()
            val product = viewDataBinding?.edtPoint7?.text.toString()
            val quantityDispatched = viewDataBinding?.edtPoint8?.text.toString()
            val routeSource = viewDataBinding?.edtPoint10?.text.toString()
            val routeDesignation = viewDataBinding?.edt2Point10?.text.toString()
            val vehicleNo = viewDataBinding?.edtPoint13?.text.toString()
            val nameAndAddress = viewDataBinding?.edtPoint14?.text.toString()
            val driverName = viewDataBinding?.edtPoint15?.text.toString()
            val driverPhone = viewDataBinding?.edt2Point15?.text.toString()
            if (nameAndLocation.isNotEmpty() && product.isNotEmpty() && quantityDispatched.isNotEmpty()
                && routeSource.isNotEmpty() && routeDesignation.isNotEmpty() && vehicleNo.isNotEmpty()
                && nameAndAddress.isNotEmpty() && driverName.isNotEmpty() && driverPhone.isNotEmpty()
            ) {
                val createChallanRequest = CreateChallanRequest(
                    nameAndLocation = nameAndLocation,
                    product = product,
                    quantityDispatched = quantityDispatched,
                    routeSource = routeSource,
                    routeDesignation = routeDesignation,
                    vehicleNo = vehicleNo,
                    nameAndAddress = nameAndAddress,
                    driverName = driverName,
                    driverPhone = driverPhone,
                    challanNumber = challanNumber,
                    validFrom = validFrom,
                    validTo = validTo,
                    gstNumber = gstNumber,
                    expireInHours = viewDataBinding?.spinnerExpire?.text.toString().toInt()
                )
                createChallanAPI(createChallanRequest, gstNumber)
            } else {
                showMessage("Please fill all details")
            }
        }


        viewDataBinding?.btnLogout?.setOnClickListener {
            verifyLogout()
        }
    }

    private fun addPopupMenu() {
        // Create the ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, viewModel.items)
        // Set up the PopupMenu

        val popupMenu = PopupMenu(
            ContextThemeWrapper(this, R.style.PopupMenuStyle),
            viewDataBinding!!.spinnerExpire
        )
        popupMenu.menu.add("Dynamic Item") // You can also add dynamic items to the menu

        popupMenu.setOnMenuItemClickListener { item -> // Handle menu item click
            expireInHours = item.title.toString().toInt()
            viewModel.setExpireHour(expireInHours)
            updateValidityTime(expireInHours)
            viewDataBinding?.spinnerExpire?.text = item.title
            true
        }

        // Set up the ArrayAdapter as the menu for the PopupMenu

        // Set up the ArrayAdapter as the menu for the PopupMenu
        popupMenu.menu.clear()
        for (i in 0 until adapter.count) {
            adapter.getItem(i)?.let { popupMenu.menu.add(0, i, i, it) }
        }

        // Show the popup menu when the anchor view is clicked

        // Show the popup menu when the anchor view is clicked
        viewDataBinding?.spinnerExpire?.setOnClickListener { popupMenu.show() }
    }

    private fun makeTextBold(text: String): Spanned {
        return HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun initializationDagger() {
        DaggerDashBoardActivityComponent.builder().appComponent(AccessAliSDK.appComponent)
            .dashBoardActivityModule(DashBoardActivityModule())
            .baseActivityModule(BaseActivityModule(this@DashBoardActivity)).build()
            .inject(this)
    }

    private fun generateQRCode() {
        val bitmap: Bitmap? =
            Utility.generateQRCode(
                "NEW",
                ContextCompat.getColor(applicationContext, R.color.qrCode)
            )
        // Set QR Code to ImageView
        bitmap?.let {
            viewDataBinding?.imgQRCode?.setImageBitmap(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getExpireHour()?.let { updateValidityTime(it) }
        challanNumber = Utility.generateRandomChallanString(12)
        viewDataBinding?.txtEchallanNumber?.text = getString(R.string.challan_number, challanNumber)
    }

    private fun updateValidityTime(validHours: Int) {
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        validFrom = sdf.format(currentTime)
        calendar.add(Calendar.HOUR_OF_DAY, validHours)
        val futureTime = calendar.time
        validTo = sdf.format(futureTime)
        viewDataBinding?.txtValidity?.text =
            "Validity from $validFrom to $validTo"
        viewDataBinding?.txtPoint9?.text =
            "DATE & TIME of dispatch $validFrom to $validTo (Valid upto $validHours Hours)"
        challanNumber = Utility.generateRandomChallanString(12)
    }

    private fun verifyLogout() {
        val mBuilder = android.app.AlertDialog.Builder(this@DashBoardActivity)
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
            startActivity(Intent(this@DashBoardActivity, SignInActivity::class.java))
            finishAffinity()
        }
    }

    private fun createChallanAPI(createChallanRequest: CreateChallanRequest, gstNumber: String) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.createChallan(
                createChallanRequest
            )
            viewModel.createChallanResponseModel.observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        viewDataBinding?.progressBar?.visibility = View.GONE
                        setEditTextValue(it.data)
                        val intent = Intent(this@DashBoardActivity, FinalFormActivity::class.java)
                        intent.putExtra("point6", viewDataBinding?.edtPoint6?.text.toString())
                        intent.putExtra("point7", viewDataBinding?.edtPoint7?.text.toString())
                        intent.putExtra("point8", viewDataBinding?.edtPoint8?.text.toString())
                        intent.putExtra("point10", viewDataBinding?.edtPoint10?.text.toString())
                        intent.putExtra("2point10", viewDataBinding?.edt2Point10?.text.toString())
                        intent.putExtra("point13", viewDataBinding?.edtPoint13?.text.toString())
                        intent.putExtra("point14", viewDataBinding?.edtPoint14?.text.toString())
                        intent.putExtra("point15", viewDataBinding?.edtPoint15?.text.toString())
                        intent.putExtra("2point15", viewDataBinding?.edt2Point15?.text.toString())
                        intent.putExtra("challanNumber", challanNumber)
                        intent.putExtra("gstNumber", gstNumber)
                        intent.putExtra("rateOfMineral", rateOfMineral)
                        intent.putExtra("rateOfMineralTotal", rateOfMineralTotal)
                        intent.putExtra(
                            "expireInHours",
                            viewDataBinding?.spinnerExpire?.text.toString()
                        )
                        intent.putExtra("validTo", validTo)
                        intent.putExtra("validFrom", validFrom)
                        startActivity(intent)
                    }

                    Status.ERROR -> {
                        viewDataBinding?.progressBar?.visibility = View.GONE
                        showErrorMessage(it.message)
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


    private fun getEditTextValues() {
        if (viewModel.getEditText6()?.isNotEmpty() == true) {
            viewDataBinding?.edtPoint6?.setText(
                viewModel.getEditText6()
            )
        }
        if (viewModel.getEditText7()?.isNotEmpty() == true) {
            viewDataBinding?.edtPoint7?.setText(
                viewModel.getEditText7()
            )
        }
        if (viewModel.getEditText8() != 0) {
            viewDataBinding?.edtPoint8?.setText(
                viewModel.getEditText8().toString()
            )
        }
        if (viewModel.getEditText10()?.isNotEmpty() == true) {
            viewDataBinding?.edtPoint10?.setText(
                viewModel.getEditText10()
            )
        }
        if (viewModel.getEdit2Text10()?.isNotEmpty() == true) {
            viewDataBinding?.edt2Point10?.setText(
                viewModel.getEdit2Text10()
            )
        }
        if (viewModel.getEditText14()?.isNotEmpty() == true) {
            viewDataBinding?.edtPoint14?.setText(
                viewModel.getEditText14()
            )
        }
    }

    private fun setEditTextValue(data: CreateChallanResponse?) {
        viewModel.setEditText6(data?.nameAndLocation)
        viewModel.setEditText7(data?.product)
        val floatValue = data?.quantityDispatched?.toFloatOrNull()
        if (floatValue != null) {
            val quantityDispatched = floatValue.toInt()
            viewModel.setEditText8(quantityDispatched)
        }
        viewModel.setEditText10(data?.routeSource)
        viewModel.setEdit2Text10(data?.routeDesignation)
        viewModel.setEditText14(data?.nameAndAddress)
    }
}

