package com.fionpay.agent.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fionpay.agent.App

/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseFragment<DB : ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {

    protected lateinit var mActivity: AppCompatActivity

    protected lateinit var mDataBinding: DB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as AppCompatActivity).let {
            this.mActivity = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        mDataBinding.root.isClickable = true
        return mDataBinding.root
    }


    fun removeFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = mActivity.supportFragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()
        fragment?.let { fragmentTransaction.remove(it) }
        fragmentTransaction.commit()
    }

    open fun getApp(): App? {
        return mActivity.application as App?
    }

    fun showMessage(message: String){
        Toast.makeText(mActivity,message, Toast.LENGTH_SHORT).show()
    }

}