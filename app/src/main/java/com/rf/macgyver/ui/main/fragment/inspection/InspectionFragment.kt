package com.rf.macgyver.ui.main.fragment.inspection

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.dailyReportData.QuestionData
import com.rf.macgyver.data.model.request.inspectionFormData.IPQuestionData
import com.rf.macgyver.data.model.request.inspectionFormData.InspectionFormData
import com.rf.macgyver.databinding.FragmentInspectionBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.base.BaseFragmentModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.adapter.InspectionItemAdapter
import com.rf.macgyver.ui.main.di.DaggerInspectionFragmentComponent
import com.rf.macgyver.ui.main.di.DashBoardFragmentModuleDi
import com.rf.macgyver.ui.main.viewmodel.DashBoardViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import javax.inject.Inject

class InspectionFragment : BaseFragment<FragmentInspectionBinding>(R.layout.fragment_inspection) {

    private var dataList: ArrayList<InspectionFormData> = arrayListOf()

    var uniqueToken: String? = null

    //arrayListOf("Engine Overheatinng","Hydrolic Oil lekage")
    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewmodel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private var isPasswordVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initializeView()

    }

    private fun initializeView() {

        val bundle = arguments
        uniqueToken =
            bundle?.getString("uniqueId")
        mDataBinding.createReportButton.visibility = View.VISIBLE
        mDataBinding.recyclerViewId.visibility = View.GONE
        mDataBinding.createReportButton.setOnClickListener {
            mDataBinding.createReportButton.visibility = View.GONE
            mDataBinding.recyclerViewId.visibility = View.VISIBLE

            /*Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_inspection_to_navigation_step1_inspection)*/
        }

        val navController =
            Navigation.findNavController(requireActivity(), R.id.navHostOnDashBoardFragment)

        mDataBinding.backArrowBtn.setOnClickListener {
            navController.navigateUp()
        }
        dataList.clear()
        dataList.add(
            InspectionFormData(
                "Engine Overheatinng",
                concernLevel = "50%",
                circle1 = true,
                circle2 = true,
                circle3 = true,
                circle4 = false,
                circle5 = false
            )
        )
        dataList.add(
            InspectionFormData(
                "Hydrolic Oil lekage",
                concernLevel = "10%",
                circle1 = true,
                circle2 = false,
                circle3 = false,
                circle4 = false,
                circle5 = false
            )
        )
        val itemAdapter = InspectionItemAdapter(dataList, requireActivity())
        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        itemAdapter.listener = cardListener
        mDataBinding.recyclerViewId.adapter = itemAdapter
    }

    private fun initializeDagger() {
        DaggerInspectionFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .dashBoardFragmentModuleDi(DashBoardFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private val cardListener = object : InspectionItemAdapter.InspectionCardEvent {
        override fun onItemClicked(inspectionFormData: InspectionFormData) {
            val randomUniqueNumber = generateRandomUniqueNumber()
            val bundle = Bundle().apply {
                putSerializable("inspectionFormData", inspectionFormData)
                putString("uniqueToken",uniqueToken)
                putInt("UniqueNo", randomUniqueNumber)
            }
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_inspection_to_navigation_step1_inspection, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        mDataBinding.createReportButton.visibility = View.VISIBLE
        mDataBinding.recyclerViewId.visibility = View.GONE
    }
    private fun generateRandomUniqueNumber(): Int {
        return (0..Int.MAX_VALUE).random()
    }
}