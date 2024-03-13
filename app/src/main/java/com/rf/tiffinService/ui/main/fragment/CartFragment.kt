package com.rf.tiffinService.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rf.tiffinService.R
import com.rf.tiffinService.data.model.request.Data
import com.rf.tiffinService.data.model.request.ImageData
import com.rf.tiffinService.databinding.FragmentCartBinding
import com.rf.tiffinService.ui.base.BaseFragment
import com.rf.tiffinService.ui.main.adapter.CartItemAdapter
import com.rf.tiffinService.utils.SharedPreferenceHelper
import javax.inject.Inject

class CartFragment : BaseFragment<FragmentCartBinding>(R.layout.fragment_cart) {

    private var dataList: ArrayList<Data> = arrayListOf()
    private var breakfastList: ImageData? = ImageData()
    private var lunchList: ImageData? = ImageData()
    private var dinnerList: ImageData? = ImageData()


    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        initializeView()
    }

    private fun initializeView() {/*val dataList = sharedPreferenceHelper.retrieveImageDataList()*/

        /*val docRef= mFirebaseDatabaseInstance?.collection("TiffinServices")?.document("user_id")*/

        val docRef = FirebaseFirestore.getInstance()
        docRef.collection("TiffinServices").document("BreakFast").get()
            .addOnSuccessListener { documentSnapshot ->
                breakfastList = documentSnapshot.toObject(ImageData::class.java)
                breakfastList?.let { setAdapter(it) }
            }.addOnFailureListener {
                showMessage("${it.message}")
                Log.i("FailureListener", "${it.message}")
            }
        docRef.collection("TiffinServices").document("Lunch").get()
            .addOnSuccessListener { documentSnapshot ->
                lunchList = documentSnapshot.toObject(ImageData::class.java)
                lunchList?.let { setAdapter(it) }
            }
        docRef.collection("TiffinServices").document("Dinner").get()
            .addOnSuccessListener { documentSnapshot ->
                dinnerList = documentSnapshot.toObject(ImageData::class.java)
                dinnerList?.let { setAdapter(it) }
            }
    }

    private fun setAdapter(itemList: ImageData) {

        itemList?.let {
            it.data?.forEach { item ->
                dataList.add(item)
            }
        }

        val itemAdapter = CartItemAdapter(dataList, requireActivity())
        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter
    }/* fun convertFirestoreMapToImageDataList(data: Map<String, Any>): List<ImageData> {
         val imageDataList = mutableListOf<ImageData>()

         for ((key, value) in data) {
             if (value is Map<*, *>) {
                 val imageUrl = value["imageUrl"] as? String
                 val imageName = value["imageName"] as? String

                 if (imageUrl != null && imageName != null) {
                     imageDataList.add(ImageData(imageUrl, imageName))
                 }
             }
         }

         return imageDataList
     }*/


}