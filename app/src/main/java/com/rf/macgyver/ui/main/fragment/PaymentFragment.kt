package com.rf.macgyver.ui.main.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.data.model.request.ImageData
import com.rf.macgyver.databinding.FragmentPaymentBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.main.adapter.PaymentItemAdapter

class PaymentFragment : BaseFragment<FragmentPaymentBinding>(R.layout.fragment_payment) {

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
    private fun initializeView(){
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
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        /* val docRef = FirebaseFirestore.getInstance()
         docRef.collection("macgyvers").document("BreakFast").get()
             .addOnSuccessListener { documentSnapshot ->
                 breakfastList = documentSnapshot.toObject(ImageData::class.java)
                 breakfastList?.let { setAdapter(it) }
             }.addOnFailureListener {
                 showMessage("${it.message}")
                 Log.i("FailureListener", "${it.message}")
             }
         docRef.collection("macgyvers").document("Lunch").get()
             .addOnSuccessListener { documentSnapshot ->
                 lunchList = documentSnapshot.toObject(ImageData::class.java)
                 lunchList?.let { setAdapter(it) }
             }
         docRef.collection("macgyvers").document("Dinner").get()
             .addOnSuccessListener { documentSnapshot ->
                 dinnerList = documentSnapshot.toObject(ImageData::class.java)
                 dinnerList?.let { setAdapter(it) }
             }*/
        mDataBinding.backgroundImg.background.alpha= 50

    }

    private fun setAdapter(itemList: ImageData) {

        itemList.let {
            it.data?.forEach { item ->
                dataList.add(item)
            }
        }
        if(dataList.any { it.count == 0 }) {
            dataList.removeIf{ it.count == 0 }
        }

        val itemAdapter = PaymentItemAdapter(dataList, requireActivity())
        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter

        var itemTotal = 0
        for(item in dataList){
            itemTotal += item.count?.times(item.price!!)!!
        }
        mDataBinding.ItemTotalId.text = itemTotal.toString()
        val tax : Double = (0.1*itemTotal)
        mDataBinding.taxesId.text = tax.toString()
        val totalBill =(itemTotal+ tax).toInt()
        mDataBinding.TotalBillId.text= totalBill.toString()
    }

}

