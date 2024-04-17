package com.rf.macgyver.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentStartBinding
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.activity.DashBoardActivity
import com.rf.macgyver.ui.main.di.DaggerStartFragmentComponent
import com.rf.macgyver.ui.main.di.SignInFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject


class StartFragment : BaseFragment<FragmentStartBinding>(R.layout.fragment_start) {


    private val database = context?.let { MagDatabase.getDatabase(it) }
    val dao = database?.magDao()

    private lateinit var text :String

    private val checkBoxList = arrayListOf<String>()

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by activityViewModels { signInViewModelFactory }
    private var isPasswordVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView()

    }

    private fun initializeDagger() {
        DaggerStartFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .signInFragmentModuleDi(SignInFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {

        val bundle = arguments
        val uniqueToken :String? =
            bundle?.getString("uniqueId")
        val email: String? =
            bundle?.getString("email")

        if (uniqueToken != null) {
            Log.d("uniqueId", uniqueToken)
        }else{
            Log.d("uniqueId","null")
        }

        mDataBinding.inspectionCheckbox.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.inspectionCheckbox.text.toString()
            updateList(text, isChecked)
        }
        mDataBinding.fuelCheckbox.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.fuelCheckbox.text.toString()
            updateList(text, isChecked)
        }

        mDataBinding.maintainanceCheckbox.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.maintainanceCheckbox.text.toString()
            updateList(text, isChecked)
        }

        mDataBinding.safetyCheckbox.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.safetyCheckbox.text.toString()
            updateList(text, isChecked)
        }
        mDataBinding.purchasingCheckbox.setOnCheckedChangeListener { _, isChecked ->
            text = mDataBinding.purchasingCheckbox.text.toString()
            updateList(text, isChecked)
        }

        mDataBinding.letsStartBtn.setOnClickListener {
            if (checkBoxList.size >0) {

                    viewmodel.updateMotiveHVI(email, checkBoxList)


                val intent = Intent(requireActivity(), DashBoardActivity::class.java)
                if (bundle != null) {
                    intent.putExtra("bundle",bundle)
                }
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(context, "Select one or more options", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun updateList(text: String, isChecked: Boolean) {
        if (isChecked) {
            checkBoxList.add(text)
        } else {
            checkBoxList.remove(text)
        }
    }
}