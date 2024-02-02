package com.fionpay.agent.ui.main.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.AddModelSlot
import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.AddModemSlotsModel
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.CheckNumberAvailabilityRequest
import com.fionpay.agent.data.model.request.UpdateActiveInActiveRequest
import com.fionpay.agent.data.model.request.UpdateAvailabilityRequest
import com.fionpay.agent.data.model.request.UpdateLoginRequest
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.data.model.response.Slots
import com.fionpay.agent.databinding.BankListBottomSheetBinding
import com.fionpay.agent.databinding.FragmentModemBinding
import com.fionpay.agent.databinding.HoldBottomSheetBinding
import com.fionpay.agent.databinding.ItemPhoneBottomSheetBinding
import com.fionpay.agent.databinding.ModemBottomSheetBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.BankListAdapter
import com.fionpay.agent.ui.main.adapter.ModemsManagerListAdapter
import com.fionpay.agent.ui.main.di.DaggerModemFragmentComponent
import com.fionpay.agent.ui.main.di.ModemFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.Constant
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.util.Locale
import javax.inject.Inject


class ModemFragment : BaseFragment<FragmentModemBinding>(R.layout.fragment_modem) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }

    private var modemsManagerListAdapter: ModemsManagerListAdapter? = null
    private var listGetModemsByFilter: ArrayList<GetModemsListResponse> = arrayListOf()

    private lateinit var bankListAdapter: BankListAdapter

    private var apiCall: String = ""
    private var filter = 0
    lateinit var obj: DashBoardItemResponse
    lateinit var dialogNumberEditSheetDialog : BottomSheetDialog
    lateinit var holdBottomSheetDialog : BottomSheetDialog
    private val onFionModemStatusChangeRequestActionsReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.extras != null) {
                    getModemsListApi()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView()

        activity?.registerReceiver(
            onFionModemStatusChangeRequestActionsReceiver,
            IntentFilter(Constant.MODEM_STATUS_CHANGE_ACTIONS)
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        activity?.unregisterReceiver(onFionModemStatusChangeRequestActionsReceiver)
    }

    private fun initializeDagger() {
        DaggerModemFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .modemFragmentModule(ModemFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        dialogNumberEditSheetDialog = BottomSheetDialog(mActivity)
        getBundleData()
        val gson = Gson()
        val json: String? = viewModel.getDashBoardDataModel()
        obj =
            gson.fromJson(json, DashBoardItemResponse::class.java)
        mDataBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0) {
                    filter("", true)
                }

            }
        })

        mDataBinding.filterButton.setOnClickListener { showPopupMenu(it) }
        mDataBinding.addModemButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_addModemFragment)
        }
        mDataBinding.searchButton.setOnClickListener {
            filter(mDataBinding.searchView.text.toString(), true)
        }
        getModemsListApi()
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            apiCall = bundle.getString("Api").toString()
            filter = bundle.getInt("Filer")
        }
    }


    private fun getModemsListApi() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getModemsList()
            viewModel.getModemsListResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        listGetModemsByFilter.clear()
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        it.data?.let { it1 -> listGetModemsByFilter.addAll(it1) }
                        setModemsAdapter()
                        //bleManag erListAdapter?.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        // tartActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private fun setModemsAdapter() {
        modemsManagerListAdapter = ModemsManagerListAdapter(listGetModemsByFilter)
        mDataBinding.recentModemsList.layoutManager = LinearLayoutManager(context)
        modemsManagerListAdapter?.listener = cardListener
        mDataBinding.recentModemsList.adapter = modemsManagerListAdapter
        modemsManagerListAdapter?.notifyDataSetChanged()
    }

    private val cardListener = object : ModemsManagerListAdapter.ModemCardEvent {
        override fun onStatusClicked(updateActiveInActiveRequest: UpdateActiveInActiveRequest) {
            changeActiveRequest(updateActiveInActiveRequest)
        }

        override fun onAvailabilityClicked(updateAvailabilityRequest: UpdateAvailabilityRequest) {
            changeAvailabilityRequest(updateAvailabilityRequest)
        }

        override fun onLoginClicked(updateLoginRequest: UpdateLoginRequest) {
            if (updateLoginRequest.loginStatus == "Logout") {
                changeLoginRequest(updateLoginRequest)
            } else {
                showMessage("Device successfully logged out from the modem.")
            }
        }

        override fun onAddBalanceEdit(getModemsListResponse: GetModemsListResponse) {
            addBalanceBottomSheetDialog(getModemsListResponse)
        }

        override fun onBankClick(getModemsListResponse: GetModemsListResponse) {
            openBottomBankListDialog(getModemsListResponse)
        }

        override fun onHoldBalanceEdit(getModemsListResponse: GetModemsListResponse) {
            updateHoldBalanceBottomSheetDialog(getModemsListResponse)
        }
    }


    private fun changeLoginRequest(updateLoginRequest: UpdateLoginRequest) {
        val message = "Are you sure you want to Logout?"
        val alertDialogActive = AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                updateLoginApi(updateLoginRequest)
            }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }.create()
        alertDialogActive.show()
    }

    private fun changeAvailabilityRequest(updateAvailabilityRequest: UpdateAvailabilityRequest) {
        val message = if (updateAvailabilityRequest.availablity == "On") {
            "Are you sure you want to turn on modem service?"
        } else {
            "Are you sure you want to turn off modem service?"
        }
        val alertDialogActive = AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                updateAvailabilityApi(updateAvailabilityRequest)
            }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }.create()
        alertDialogActive.show()
    }

    private fun changeActiveRequest(updateActiveInActiveRequest: UpdateActiveInActiveRequest) {
        val message = if (updateActiveInActiveRequest.status == "Active") {
            "Are you sure you want to active modem service?"
        } else {
            "Are you sure you want to inactive modem service?"
        }
        val alertDialogActive = AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                updateActiveInActiveStatusApi(updateActiveInActiveRequest)
            }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }.create()
        alertDialogActive.show()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)

        // Set a click listener for menu item clicks
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.all -> {
                    // Handle Menu Item 1 click
                    filter(getString(R.string.all), false)
                    true
                }

                R.id.menu_active -> {
                    // Handle Menu Item 1 click
                    filter(getString(R.string.active), false)
                    true
                }

                R.id.menu_in_active -> {
                    // Handle Menu Item 1 click
                    filter(getString(R.string.in_active), false)
                    true
                }

                R.id.menu_unblocked -> {
                    // Handle Menu Item 2 click
                    filter(getString(R.string.on), false)
                    true
                }

                R.id.menu_blocked -> {
                    filter(getString(R.string.off), false)
                    // Handle Menu Item 3 click
                    true
                }

                R.id.log_in -> {
                    filter(getString(R.string.login), false)
                    // Handle Menu Item 3 click
                    true
                }

                R.id.log_out -> {
                    filter(getString(R.string.logout), false)
                    // Handle Menu Item 3 click
                    true
                }

                else -> false
            }
        }

        // Show the pop-up menu
        popupMenu.show()
    }

    private fun filter(text: String, isSearching: Boolean) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<GetModemsListResponse> = ArrayList()
        if (!isSearching) {
            for (item in listGetModemsByFilter) {
                // checking if the entered string matched with any item of our recycler view.
                if (text.lowercase(Locale.getDefault()).contains("active", true)) {
                    if (item.status?.lowercase().toString() == text.lowercase(Locale.getDefault())
                    ) {
                        // if the item is matched we are
                        filteredList.add(item)
                    }
                } else if (text.lowercase(Locale.getDefault()).contains("Log", true)) {
                    if (item.loginStatus?.lowercase()
                            .toString() == text.lowercase(Locale.getDefault())
                    ) {
                        // if the item is matched we are
                        filteredList.add(item)
                    }
                } else if (text.lowercase(Locale.getDefault())
                        .contains(getString(R.string.off), true) || text.lowercase(
                        Locale.getDefault()
                    ).contains(getString(R.string.on), true)
                ) {
                    if (item.availability?.lowercase()
                            .toString() == text.lowercase(Locale.getDefault())
                    ) {
                        // if the item is matched we are
                        filteredList.add(item)
                    }
                } else if (text.isEmpty() || text == getString(R.string.all)) {
                    filteredList.clear()
                    filteredList.addAll(listGetModemsByFilter)
                }
            }
        } else {
            for (item in listGetModemsByFilter) {
                if (text.isDigitsOnly()) {
                    if (item.pinCode?.toString()
                            ?.contains(text.lowercase(Locale.getDefault()), true) == true
                    ) {
                        // if the item is matched we are
                        filteredList.add(item)
                    }
                } else if (text.isEmpty()) {
                    filteredList.clear()
                    filteredList.addAll(listGetModemsByFilter)
                } else {
                    val name = "${item.firstName} ${item.lastName}"
                    if (name.contains(text.lowercase(Locale.getDefault()), true)) {
                        // if the item is matched we are
                        filteredList.add(item)
                    }
                }
            }

        }
        if (text.isEmpty()) {
            filteredList.clear()
            filteredList.addAll(listGetModemsByFilter)
        }

        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            Toast.makeText(context, getString(R.string.no_data_found), Toast.LENGTH_SHORT).show()
        } else {
            mDataBinding.txtListTitle.text = text
            // at last we are passing that filtered
            modemsManagerListAdapter?.updateListFilter(filteredList)
        }
    }

    private fun updateActiveInActiveStatusApi(updateActiveInActiveRequest: UpdateActiveInActiveRequest) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.updateActiveInActiveStatus(updateActiveInActiveRequest)
            viewModel.getUpdateActiveInActiveStatusResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        listGetModemsByFilter
                        getModemsListApi()
                    }

                    Status.ERROR -> {
                        // startActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private fun updateAvailabilityApi(updateAvailabilityRequest: UpdateAvailabilityRequest) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.updateAvailabilityStatus(updateAvailabilityRequest)
            viewModel.getUpdateAvailabilityStatusResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        getModemsListApi()
                    }

                    Status.ERROR -> {
                        // startActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private fun updateLoginApi(updateLoginRequest: UpdateLoginRequest) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.updateLoginStatus(updateLoginRequest)
            viewModel.getUpdateLoginStatusResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        getModemsListApi()
                    }

                    Status.ERROR -> {
                        // startActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addModemBalance(getModemsListResponse: GetModemsListResponse, amount: Double) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.addModemBalance(AddModemBalanceModel(getModemsListResponse.id, amount))
            viewModel.getAddModemBalanceResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        if (dialogNumberEditSheetDialog.isShowing) {
                            dialogNumberEditSheetDialog.dismiss()
                            getModemsListResponse.balance = it.data?.balance
                            getModemsListResponse.availableBalance = it.data?.availableBalance
                            getModemsListResponse.holdBalance = it.data?.holdBalance
                            it.data?.agentBalance?.let { it1 -> viewModel.setAvailableBalance(it1) }
                            modemsManagerListAdapter?.notifyDataSetChanged()
                            // getModemsListApi()
                        }
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(
                mActivity.getString(R.string.NO_INTERNET_CONNECTION)
            )
        }
    }

    private fun updateHoldBalanceBottomSheetDialog(modemsListResponse: GetModemsListResponse) {
        holdBottomSheetDialog = BottomSheetDialog(mActivity)
        val binding = HoldBottomSheetBinding.inflate(layoutInflater)
        holdBottomSheetDialog.setContentView(binding.root)
        holdBottomSheetDialog.setCanceledOnTouchOutside(true)
        holdBottomSheetDialog.setCancelable(true)
        holdBottomSheetDialog.show()
        binding.labelTotalBalance.text =
            getString(R.string.new_balance_will,modemsListResponse.availableBalance.toString())
        binding.etUpdateBalance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    val newVal = s.toString().toDouble()
                    val totalBalance = modemsListResponse.availableBalance?.minus(newVal)
                    if (totalBalance!! > 0.0) {
                        binding.labelTotalBalance.text =
                            getString(R.string.new_balance_will, totalBalance.toString())
                    } else {
                        Toast.makeText(
                            context,
                            "Entered amount should be less than modem available balance",
                            Toast.LENGTH_SHORT
                        ).show()
                        getString(R.string.new_balance_will, modemsListResponse.availableBalance.toString())
                        binding.etUpdateBalance.setText("")
                    }

                } else {
                    binding.labelTotalBalance.text =
                        getString(R.string.new_balance_will, modemsListResponse.availableBalance.toString())
                }

            }
        })

        binding.imageClose.setOnClickListener {
            holdBottomSheetDialog.dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            if (binding.etUpdateBalance.text.toString().isEmpty()) {
                binding.etUpdateBalance.error = "Please enter hold amount"
            } else {

                val amount = binding.etUpdateBalance.text.toString().toDouble()
                updateModemBalance(modemsListResponse, amount)
                //binding.etUpdateBalance.setText("")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateModemBalance(getModemsListResponse: GetModemsListResponse, amount: Double) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.holdModemBalance(AddModemBalanceModel(getModemsListResponse.id, amount))
            viewModel.getHoldModemBalanceResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        if (holdBottomSheetDialog.isShowing) {
                            holdBottomSheetDialog.dismiss()
                            getModemsListResponse.balance = it.data?.balance
                            getModemsListResponse.availableBalance = it.data?.availableBalance
                            getModemsListResponse.holdBalance = it.data?.holdBalance
                            modemsManagerListAdapter?.notifyDataSetChanged()
                        }
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(
                mActivity.getString(R.string.NO_INTERNET_CONNECTION)
            )
        }
    }

    private fun addBalanceBottomSheetDialog(getModemsListResponse: GetModemsListResponse) {
        val binding = ModemBottomSheetBinding.inflate(layoutInflater)
        dialogNumberEditSheetDialog.setContentView(binding.root)
        dialogNumberEditSheetDialog.setCanceledOnTouchOutside(true)
        dialogNumberEditSheetDialog.setCancelable(true)
        dialogNumberEditSheetDialog.show()

        val fullName = "${getModemsListResponse.firstName} ${getModemsListResponse.lastName}"
        val balance = "${Utility.currencySymbolBD}${getModemsListResponse.balance.toString()}"
        binding.labelTotalBalance.text =
            getString(R.string.new_balance_will,viewModel.getAvailableBalance().toString())
        binding.labelTitle.text = fullName
        binding.etCurrentBalance.text = balance

        Log.i("Current:", "${binding.etCurrentBalance.text}")

        binding.etUpdateBalance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    val newVal = s.toString().toDouble()
                    val totalBalance = viewModel.getAvailableBalance().minus(newVal)
                    if (totalBalance > 0.0) {
                        binding.labelTotalBalance.text =
                            getString(R.string.new_balance_will, totalBalance.toString())
                    } else {
                        Toast.makeText(
                            context,
                            "Entered amount should be less than agent available balance",
                            Toast.LENGTH_SHORT
                        ).show()
                        getString(R.string.new_balance_will, viewModel.getAvailableBalance().toString())
                        binding.etUpdateBalance.setText("")
                    }

                } else {
                    binding.labelTotalBalance.text =
                        getString(R.string.new_balance_will, viewModel.getAvailableBalance().toString())
                }

            }
        })

        binding.imageClose.setOnClickListener {
            dialogNumberEditSheetDialog.dismiss()
        }
        binding.btnUpdate.setOnClickListener {
            if (binding.etUpdateBalance.text.toString().isEmpty()) {
                binding.etUpdateBalance.error = "Please enter amount"
            } else {

                val amount = binding.etUpdateBalance.text.toString().toDouble()
                addModemBalance(getModemsListResponse, amount)
                //binding.etUpdateBalance.setText("")
            }
        }
    }

    private fun openBottomBankListDialog(getModemsListResponse: GetModemsListResponse) {
        val dialogBankListDialog = BottomSheetDialog(mActivity)
        val binding = BankListBottomSheetBinding.inflate(layoutInflater)
        val bankList = viewModel.getBanksListDao()
        val bankListTemp: List<Bank>? = bankList
        if (bankListTemp.isNullOrEmpty()) {
            Toast.makeText(context, "No more bank there", Toast.LENGTH_SHORT).show()
            return
        } else if (bankListTemp.isEmpty()) {
            Toast.makeText(context, "No more bank there", Toast.LENGTH_SHORT).show()
            return
        }
        bankListAdapter = BankListAdapter(bankListTemp)
        binding.bankList.layoutManager = GridLayoutManager(context, 2)
        bankListAdapter.listener = cardPhoneNumberListener
        binding.bankList.adapter = bankListAdapter
        dialogBankListDialog.setContentView(binding.root)
        dialogBankListDialog.setOnShowListener {
            Handler().postDelayed({
                dialogBankListDialog.let {
                    val sheet = it
                    sheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }, 0)
        }
        binding.sendBtn.text = context?.getString(R.string.add_Bank)
        binding.sendBtn.setOnClickListener {
            dialogBankListDialog.dismiss()
            val modelSlotsList: ArrayList<AddModelSlot> = arrayListOf()
            bankList.forEach {
                if (it.phoneNumber?.isNotEmpty() == true) {
                    modelSlotsList.add(AddModelSlot(it.phoneNumber, it.bankId))
                }
            }
            if (modelSlotsList.isNotEmpty()) {
                addModemSlots(getModemsListResponse.id, modelSlotsList)
            } else {
                showMessage("Please Select Bank")
            }
        }
        dialogBankListDialog.show()
    }

    fun addModemSlots(modemId: String?, listAddModeSlots: List<AddModelSlot>) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.addModemSlots(AddModemSlotsModel(modemId, listAddModeSlots))
            viewModel.getAddModemItemResponseModel.removeObservers(viewLifecycleOwner)
            viewModel.getModemSlotsResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        if (dialogNumberEditSheetDialog.isShowing) {
                            dialogNumberEditSheetDialog.dismiss()
                        }
                        getModemsListApi()
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(
                mActivity.getString(R.string.NO_INTERNET_CONNECTION)
            )
        }
    }

    private val cardPhoneNumberListener = object : BankListAdapter.BankCardEvent {
        override fun onCardClick(bank: Bank, position: Int) {
            //showMessage(selectedBankId.toString())
            getPhoneNumber(bank, position)
        }
    }

    private fun getPhoneNumber(bank: Bank, position: Int) {
        val builder: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val binding: ItemPhoneBottomSheetBinding =
            ItemPhoneBottomSheetBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        binding.etModemPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val maxLength: Int = if (s?.startsWith("0") == true) {
                    11
                } else {
                    10
                }
                val inputFilterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
                binding.etModemPhoneNumber.filters = inputFilterArray
            }
        })

        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
        dialog.show()
        binding.btnContinue.setOnClickListener {
            if (validationPhoneNumber(binding.etModemPhoneNumber)) {
                checkNumberBankAvailability(
                    CheckNumberAvailabilityRequest(
                        binding.etModemPhoneNumber.text.toString(),
                        bank.bankId
                    ),
                    dialog,
                    binding,
                    bank, position
                )
            } else {
                showMessage(getString(R.string.enter_valid_number))
            }
        }
    }


    private fun validationPhoneNumber(etModemPhoneNumber: AppCompatEditText): Boolean {
        val number = etModemPhoneNumber.text.toString()
        return if (number.startsWith("0") && number.length == 11) {
            true
        } else !number.startsWith("0") && number.length == 10
    }

    private fun checkNumberBankAvailability(
        checkNumberAvailabilityRequest: CheckNumberAvailabilityRequest,
        dialog: androidx.appcompat.app.AlertDialog,
        binding: ItemPhoneBottomSheetBinding,
        bank: Bank,
        position: Int
    ) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.checkNumberBankAvailability(checkNumberAvailabilityRequest)
            viewModel.checkNumberBankAvailabilityResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                        bank.phoneNumber = binding.etModemPhoneNumber.text.toString()
                        bankListAdapter.notifyItemChanged(position)
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            Snackbar.make(requireView(), getString(R.string.no_network), Snackbar.LENGTH_LONG)
                .show()
        }

    }
}