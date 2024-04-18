package com.rf.macgyver.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentCompanyInfoBinding
import com.rf.macgyver.roomDB.MagDatabase
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerCompanyInfoFragmentComponent
import com.rf.macgyver.ui.main.di.SignInFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class CompanyInfoFragment : BaseFragment<FragmentCompanyInfoBinding>(R.layout.fragment_company_info) {



    private  var noOfVehicles :String? = null
    private  var userRole :String? = null
    private var companyIndustry : String? = null
    private val database = context?.let { MagDatabase.getDatabase(it) }
    val dao = database?.magDao()

    private var isAnyRadioButtonSelected = false
    private val card1List = mutableListOf<String>()
    private val card2List = mutableListOf<String>()
    private val card3List = mutableListOf<String>()

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
        initializeVew()

    }

    private fun initializeDagger() {
        DaggerCompanyInfoFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .signInFragmentModuleDi(SignInFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeVew(){
        val bundle = arguments

        val email: String? =
            bundle?.getString("email")

        val uniqueId: String? =
            bundle?.getString("uniqueId")


        mDataBinding.card1RadioGrp.setOnCheckedChangeListener {  group, checkedId ->
            val checkedRadioButton = group.findViewById<RadioButton>(checkedId)
            val radioButtonTitle = checkedRadioButton.text.toString()

            card1Update(radioButtonTitle)
            checkIfAnyRadioButtonSelected()
        }

        mDataBinding.card2RadioGroup.setOnCheckedChangeListener {  group, checkedId ->
            val checkedRadioButton2 = group.findViewById<RadioButton>(checkedId)
            val radioButtonTitle2 = checkedRadioButton2.text.toString()

            card2Update(radioButtonTitle2)
            checkIfAnyRadioButtonSelected()
        }

        mDataBinding.card3RadioGroup.setOnCheckedChangeListener {  group, checkedId ->
            val checkedRadioButton3 = group.findViewById<RadioButton>(checkedId)
            val radioButtonTitle3 = checkedRadioButton3.text.toString()

            card3Update(radioButtonTitle3)
            checkIfAnyRadioButtonSelected()
        }

        mDataBinding.nextBtn.setOnClickListener{
            if(isAnyRadioButtonSelected ) {

                val card1RadioButtonId = mDataBinding.card1RadioGrp.checkedRadioButtonId
                if (card1RadioButtonId != -1) {
                    val selectedRadioButton: RadioButton? = view?.findViewById(card1RadioButtonId)
                    noOfVehicles = selectedRadioButton?.text.toString()}

                val card2RadioButtonId = mDataBinding.card2RadioGroup.checkedRadioButtonId
                if (card2RadioButtonId != -1) {
                    val selectedRadioButton: RadioButton? = view?.findViewById(card2RadioButtonId)
                    userRole = selectedRadioButton?.text.toString()}

                val card3RadioButtonId = mDataBinding.card1RadioGrp.checkedRadioButtonId
                if (card3RadioButtonId != -1) {
                    val selectedRadioButton: RadioButton? = view?.findViewById(card3RadioButtonId)
                    companyIndustry = selectedRadioButton?.text.toString()}


                viewmodel.updateCompanyInfo(email,companyIndustry,noOfVehicles,userRole)

                findNavController().navigate(R.id.action_fragment_company_info_to_fragment_start, bundle)
            }else{

                Toast.makeText(context, "Select option for each", Toast.LENGTH_SHORT).show()
            }
         }
    }
    private fun card1Update(text : String){

        if (!card1List.contains(text)) {
            card1List.add(text)
        }
    }

    private fun card2Update(text : String){

        if (!card2List.contains(text)) {
            card2List.add(text)
        }
    }
    private fun card3Update(text : String){

        if (!card3List.contains(text)) {
            card3List.add(text)
        }
    }
    private fun checkIfAnyRadioButtonSelected() {
        isAnyRadioButtonSelected = mDataBinding.card1RadioGrp.checkedRadioButtonId != -1 ||
                mDataBinding.card2RadioGroup.checkedRadioButtonId != -1 ||
                mDataBinding.card3RadioGroup.checkedRadioButtonId != -1
    }
}