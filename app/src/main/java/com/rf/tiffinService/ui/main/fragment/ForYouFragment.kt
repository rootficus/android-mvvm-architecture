package com.rf.tiffinService.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.rf.tiffinService.R
import com.rf.tiffinService.data.model.request.Data
import com.rf.tiffinService.data.model.request.ImageData
import com.rf.tiffinService.databinding.FragmentForYouBinding
import com.rf.tiffinService.ui.base.BaseFragment
import com.rf.tiffinService.ui.main.adapter.TiffinItemAdapter

class ForYouFragment() : BaseFragment<FragmentForYouBinding>(R.layout.fragment_for_you) {


   /* var dataList = listOf(
        ImageData(R.drawable.food_icon,0),
        ImageData(R.drawable.recipes_svgrepo_com,0),
        ImageData(R.drawable.donnut_svgrepo_com,0),
        ImageData(R.drawable.dish_svgrepo_com,0),
        ImageData(R.drawable.food_icon,0),
        ImageData(R.drawable.recipes_svgrepo_com,0),
        ImageData(R.drawable.donnut_svgrepo_com,0),
        ImageData(R.drawable.dish_svgrepo_com,0)
    )*/

    private var dataList: ArrayList<Data> = arrayListOf()
    private var breakfastData: ImageData? = ImageData()
    private var lunchData: ImageData? = ImageData()
    private var dinnerData: ImageData? = ImageData()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)/*
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())*/
        initializeView()
    }

    private fun initializeView(){
        /*val sharedPreferenceData = sharedPreferenceHelper.retrieveImageDataList()
        if(sharedPreferenceData.isNotEmpty()){
            dataList= sharedPreferenceData
        }*/
        val docRef = FirebaseFirestore.getInstance()
        docRef.collection("TiffinServices").document("BreakFast").get()
            .addOnSuccessListener { documentSnapshot ->
                breakfastData= documentSnapshot.toObject(ImageData::class.java)
                breakfastData?.let { setAdapter(it) }
            }.addOnFailureListener {
                showMessage("${it.message}")
                Log.i("FailureListener", "${it.message}")
            }
        docRef.collection("TiffinServices").document("Lunch").get()
            .addOnSuccessListener { documentSnapshot ->
                lunchData = documentSnapshot.toObject(ImageData::class.java)
                lunchData?.let { setAdapter(it) }
            }
        docRef.collection("TiffinServices").document("Dinner").get()
            .addOnSuccessListener { documentSnapshot ->
                dinnerData = documentSnapshot.toObject(ImageData::class.java)
                dinnerData?.let { setAdapter(it) }
            }
        val db = Firebase.firestore
        val usersCollection = db.collection("TiffinServices")
        usersCollection.document("BreakFast").update("count",countUpdate )
        usersCollection.document("Lunch").update("count",countUpdate)
        usersCollection.document("Dinner").update("count",countUpdate)
    }

    private fun setAdapter(itemList: ImageData) {

        itemList?.let {
            it.data?.forEach { item ->
                dataList.add(item)
            }
        }

        val itemAdapter = TiffinItemAdapter(dataList, requireActivity())
        val layoutManager = GridLayoutManager(requireActivity(),2)
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter

    }
    /*private val cardListener = */object countUpdate : TiffinItemAdapter.CardEvent {
        override fun onCardClicked() {

        }
    }
}

