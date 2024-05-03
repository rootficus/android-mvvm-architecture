package com.rf.accessAli.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.rf.accessAli.R
import com.rf.accessAli.data.model.User
import com.rf.accessAli.data.model.request.SignInRequest
import com.rf.accessAli.databinding.FragmentSignInBinding
import com.rf.accessAli.sdkInit.AccessAliSDK
import com.rf.accessAli.ui.base.BaseFragment
import com.rf.accessAli.ui.base.BaseFragmentModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.activity.DashBoardActivity
import com.rf.accessAli.ui.main.adapter.CustomSpinnerAdapter
import com.rf.accessAli.ui.main.di.DaggerSignInFragmentComponent
import com.rf.accessAli.ui.main.di.SignInFragmentModuleDi
import com.rf.accessAli.ui.main.viewmodel.SignInViewModel
import com.rf.accessAli.utils.NetworkHelper
import com.rf.accessAli.utils.SharedPreference
import com.rf.accessAli.utils.Status
import com.rf.accessAli.utils.Utility
import com.rf.accessAli.utils.Utility.isEmailValid
import javax.inject.Inject


class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private var name: String = ""
    private var password: String = ""

    @Inject
    lateinit var sharedPreference: SharedPreference
    /*
        @Inject
        lateinit var progressBar: Dialog*/

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by activityViewModels { signInViewModelFactory }
    private var isPasswordVisible = false
    val items: MutableList<User> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView(view)

    }

    private fun initializeDagger() {

        DaggerSignInFragmentComponent.builder().appComponent(AccessAliSDK.appComponent)
            .signInFragmentModuleDi(SignInFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(view: View) {

        val storedSignList = getLoginList()
        items.addAll(storedSignList)

        // Create and set custom adapter
        if (items.isEmpty()) {
            mDataBinding.spinner.visibility = View.GONE
        } else {
            mDataBinding.spinner.visibility = View.VISIBLE
        }
        // Create and set custom adapter
        val adapter = CustomSpinnerAdapter(requireActivity(), items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDataBinding.spinner.adapter = adapter
        mDataBinding.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mDataBinding.etName.setText(items[position].name)
                    mDataBinding.etPassword.setText(items[position].password)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

        mDataBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()

                if (isEmailValid(email)) {
                    mDataBinding.emailVerified.visibility = View.VISIBLE
                } else {
                    mDataBinding.emailVerified.visibility = View.GONE
                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString()
                        .isNotEmpty() && mDataBinding.etPassword.text!!.isNotEmpty() && mDataBinding.etPassword.text!!.length > 8
                ) {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.btn_active_back)
                    mDataBinding.btnSignIn.isEnabled = true
                } else {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.btn_inactive_back
                        )
                    mDataBinding.btnSignIn.isEnabled = true
                }
            }
        })

        mDataBinding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        mDataBinding.passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        mDataBinding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString()
                        .isNotEmpty() && s.toString().length > 8 && mDataBinding.etName.text!!.toString()
                        .isNotEmpty()
                ) {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.btn_active_back)
                    mDataBinding.btnSignIn.isEnabled = true
                } else {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.btn_inactive_back
                        )
                    mDataBinding.btnSignIn.isEnabled = true
                }

            }
        })

        mDataBinding.btnSignIn.setOnClickListener {
            name = mDataBinding.etName.text.toString()
            password = mDataBinding.etPassword.text.toString()
            if (name.isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_NAME)
                )
            } else if (password.isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_PASSWORD)
                )
            } else {
                callSignInAPI()
            }
        }
    }

    private fun callSignInAPI() {

        if (networkHelper.isNetworkConnected()) {
            viewmodel.signInNow(
                SignInRequest(
                    email = name,
                    password = password,
                )
            )
            viewmodel.signInResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        mDataBinding.progressBar.visibility = View.GONE
                        val gson = Gson()
                        addLoginList(gson)
                        val json = gson.toJson(it.data)
                        viewmodel.setSignInDataModel(json)
                        viewmodel.setPassword(password)
                        viewmodel.setIsLogin(true)
                        viewmodel.setToken(it.data?.token.toString())
                        viewmodel.setFullName("${it.data?.company}")
                        startActivity(Intent(requireContext(), DashBoardActivity::class.java))
                    }

                    Status.ERROR -> {
                        mDataBinding.progressBar.visibility = View.GONE
                        showErrorMessage(it.message)

                    }

                    Status.LOADING -> {
                        mDataBinding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            mDataBinding.progressBar.visibility = View.GONE
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    // Function to check if the new object already exists in the list
    private fun isDuplicateObject(existingList: List<User>, newObject: User): Boolean {
        for (item in existingList) {
            if (item == newObject) {
                // If objects are equal, it means newObject already exists in the list
                return true
            }
        }
        // If no duplicate found, return false
        return false
    }

    private fun addLoginList(gson: Gson) {
        if (!isDuplicateObject(items, User(name, password))) {
            items.add(User(name, password))
            val jsonString = gson.toJson(items)
            sharedPreference.setSignInList(jsonString)
        }
    }

    private fun getLoginList(): List<User> {
        val gson = Gson()
        val jsonString = sharedPreference.getSignInList()
        return if (jsonString != null) {
            val array = gson.fromJson(jsonString, Array<User>::class.java)
            array?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Password is currently visible, so hide it
            mDataBinding.etPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
            mDataBinding.passwordToggle.setImageResource(R.drawable.show_password)
        } else {
            // Password is currently hidden, so make it visible
            mDataBinding.etPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            mDataBinding.passwordToggle.setImageResource(R.drawable.hide_password)
        }

        // Move the cursor to the end of the text
        mDataBinding.etPassword.setSelection(mDataBinding.etPassword.text!!.length)
        // Toggle the visibility flag
        isPasswordVisible = !isPasswordVisible
    }
}