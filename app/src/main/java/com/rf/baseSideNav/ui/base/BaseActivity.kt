package com.rf.baseSideNav.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rf.baseSideNav.App

/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseActivity<VB : ViewDataBinding, Toolbar>(@LayoutRes val layoutRes: Int) :
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
}