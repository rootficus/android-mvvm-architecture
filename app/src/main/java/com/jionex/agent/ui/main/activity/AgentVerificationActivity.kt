package com.jionex.agent.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.jionex.agent.R
import com.jionex.agent.databinding.ActivityAgentVerificationBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseActivity
import com.jionex.agent.ui.base.BaseActivityModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.di.AgentVerificationActivityModule
import com.jionex.agent.ui.main.di.DaggerAgentVerificationActivityComponent
import com.jionex.agent.ui.main.viewmodel.AgentVerificationViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
import javax.inject.Inject

class AgentVerificationActivity : BaseActivity<ActivityAgentVerificationBinding>(R.layout.activity_agent_verification) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var agentVerificationViewModelFactory: BaseViewModelFactory<AgentVerificationViewModel>
    private val agentVerificationViewModel: AgentVerificationViewModel by viewModels { agentVerificationViewModelFactory }

    var passwordString = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializationDagger()

        initializationView()

    }

    private fun initializationDagger() {
       DaggerAgentVerificationActivityComponent.builder().appComponent(JionexSDK.appComponent)
            .agentVerificationActivityModule(AgentVerificationActivityModule())
            .baseActivityModule(BaseActivityModule(this@AgentVerificationActivity)).build()
            .inject(this)
    }

    private fun initializationView() {
        enableLockScreen()

    }

    private fun enableLockScreen() {
        viewDataBinding?.lockLayout?.visibility = View.VISIBLE
        val view = this.currentFocus
        viewDataBinding?.btnZero?.setOnClickListener {
            passwordString.append("0")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnOne?.setOnClickListener {
            passwordString.append("1")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnTwo?.setOnClickListener {
            passwordString.append("2")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnThree?.setOnClickListener {
            passwordString.append("3")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnFour?.setOnClickListener {
            passwordString.append("4")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnFive?.setOnClickListener {
            passwordString.append("5")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnSix?.setOnClickListener {
            passwordString.append("6")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnSeven?.setOnClickListener {
            passwordString.append("7")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnEight?.setOnClickListener {
            passwordString.append("8")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnNine?.setOnClickListener {
            passwordString.append("9")
            viewDataBinding?.txtInput!!.text = passwordString.toString()
        }
        viewDataBinding?.btnClear?.setOnClickListener {
            passwordString = StringBuilder()
            viewDataBinding?.txtInput?.text = ""
        }
        viewDataBinding?.btnDecimal?.setOnClickListener {
            if (viewDataBinding?.txtInput?.text!!.isNotEmpty() && viewDataBinding?.txtInput?.text!!.length == 6) {
                var pinCode = viewDataBinding?.txtInput?.text.toString().toInt()
                if (sharedPreference.getPinCode() == 0) {
                    checkOnVerificationAPI(pinCode)
                } else if (pinCode == sharedPreference.getPinCode()) {
                    goToApplication()
                }
            } else {
                Toast.makeText(
                    this@AgentVerificationActivity, getString(R.string.error_messages_verfication_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

        }
    }

    private fun goToApplication() {
        if (sharedPreference.getModemSetupConfirmation()) {
           // startActivity(Intent(this@AgentVerificationActivity, MainActivity::class.java))
        } else {
            /*startActivity(
                Intent(
                    this@AgentVerificationActivity,
                    SimVerificationActivity::class.java
                )
            )*/
        }
       // finish()
    }


    private fun checkOnVerificationAPI(pincode: Int) {
        agentVerificationViewModel.checkOnVerificationAPI("$pincode")
        agentVerificationViewModel.agentVerificationResponseModel.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    Log.d("Data", "::${it.data}")
                    var userInfo = it.data
                    agentVerificationViewModel.setUserId(userInfo?.id);
                    agentVerificationViewModel.setUserName(userInfo?.user_name);
                    agentVerificationViewModel.setEmail(userInfo?.email)
                    agentVerificationViewModel.setPhoneNumber(userInfo?.phone)
                    agentVerificationViewModel.setFullName(userInfo?.full_name)
                    agentVerificationViewModel.setPinCode(userInfo?.pin_code)
                    agentVerificationViewModel.setCountry(userInfo?.country)
                    agentVerificationViewModel.setParentId(userInfo?.parent_id)
                    agentVerificationViewModel.setUserRole(userInfo?.role_id)
                    goToApplication()
                }

                Status.ERROR -> {
                    hideProgressBar()
                }

                Status.LOADING -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showProgressBar() {
        viewDataBinding?.progressView?.llProgressView?.visibility = View.VISIBLE

    }

    private fun hideProgressBar() {
        viewDataBinding?.progressView?.llProgressView?.visibility = View.GONE
    }
}