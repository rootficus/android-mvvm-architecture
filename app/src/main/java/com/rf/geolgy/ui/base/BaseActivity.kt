package com.rf.geolgy.ui.base

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rf.geolgy.App
import com.rf.geolgy.R
import com.rf.geolgy.ui.main.activity.SignInActivity

/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseActivity<VB : ViewDataBinding>(@LayoutRes val layoutRes: Int) :
    AppCompatActivity() {
    protected var viewDataBinding: VB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutRes)
    }

    fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun <T> moveToNextScreenWithFinishActivity(nextMoveClass: Class<T>) {
        val intent = Intent(applicationContext, nextMoveClass)
        startActivity(intent)
        finish()
    }

    fun moveToNextScreen(nextMoveClass: Class<AppCompatActivity>) {
        val intent = Intent(applicationContext, nextMoveClass)
        startActivity(intent)
    }

    open fun getApp(): App? {
        return this.application as App?
    }

    fun showErrorMessage(message: String?) {
        if (message == getString(R.string.invalid_access_token)) {
            //Show Toast
            showMessage(
                getString(R.string.invalid_access_token)
            )
            sessionExpired()
        } else {
            showMessage(message.toString())
        }
    }

    fun sessionExpired() {
        val mBuilder = AlertDialog.Builder(this)
            .setTitle("Session Expired")
            .setMessage("your session has expired.\n\nYou will be redirected to login page.")
            .setPositiveButton("Ok", null)
            .show()
        val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            this.finishAffinity()
        }
    }
}