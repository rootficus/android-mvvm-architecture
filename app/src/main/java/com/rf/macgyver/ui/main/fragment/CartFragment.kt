package com.rf.macgyver.ui.main.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.data.model.request.ImageData
import com.rf.macgyver.databinding.FragmentCartBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.main.adapter.CartItemAdapter

class CartFragment : BaseFragment<FragmentCartBinding>(R.layout.fragment_cart) {

    private var dataList: ArrayList<Data> = arrayListOf()
    private var breakfastData: ImageData? = ImageData()
    private var lunchData: ImageData? = ImageData()
    private var dinnerData: ImageData? = ImageData()

    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        initializeView()
    }
    private fun initializeView() {
        val db = FirebaseFirestore.getInstance()

        val macgyversRef = db.collection("macgyvers")
        macgyversRef.get()
            .addOnSuccessListener { documents ->
                for (userDocument in documents) {
                    val userId = userDocument.id

                    val breakfastRef = macgyversRef.document(userId)
                        .collection("Categories")
                        .document("Breakfast")
                    breakfastRef.get().addOnSuccessListener { documentSnapshot ->
                        breakfastData= documentSnapshot.toObject(ImageData::class.java)
                        breakfastData?.let { setAdapter(it) }
                    }

                    val lunchRef = macgyversRef.document(userId)
                        .collection("Categories")
                        .document("Lunch")
                    lunchRef.get().addOnSuccessListener { documentSnapshot ->
                        lunchData= documentSnapshot.toObject(ImageData::class.java)
                        lunchData?.let { setAdapter(it) }
                    }

                    val dinnerRef = macgyversRef.document(userId)
                        .collection("Categories")
                        .document("Dinner")
                    dinnerRef.get().addOnSuccessListener { documentSnapshot ->
                        dinnerData= documentSnapshot.toObject(ImageData::class.java)
                        dinnerData?.let { setAdapter(it) }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        mDataBinding.ProceedBtn.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_cart_to_navigation_payment)
        }
    }
    private fun setAdapter(itemData: ImageData) {

        itemData.let {
            it.data?.forEach { item ->
                dataList.add(item)
            }
        }
        if(dataList.any { it.count == 0 }) {
            dataList.removeIf{ it.count == 0 }
        }

        val itemAdapter = CartItemAdapter(dataList, requireActivity())
        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter
    }
}