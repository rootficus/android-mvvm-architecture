package com.rf.macgyver.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentVendorSignupBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.main.activity.VendorDashboardActivity

class VendorSignupFragment: BaseFragment<FragmentVendorSignupBinding>(R.layout.fragment_vendor_signup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }
    private fun initializeView(){

        mDataBinding.continueBtn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val currentUser = auth.currentUser

            val userId = currentUser?.uid

            val userDocRef = userId?.let { firestore.collection("macgyvers").document(it) }

            val detailsCollectionRef = userDocRef?.collection("Restaurant details")
            val restaurantName = mDataBinding.etName.text.toString()
            val restaurantAddress = mDataBinding.etAddress.text.toString()
            val restaurantPhone = mDataBinding.etPhoneNo.text.toString().toInt()
/*
            val detailsData = Detailsdata(
                name = restaurantName,
                phoneNo = restaurantPhone,
                address = restaurantAddress
            )*/
            detailsCollectionRef?.document("Details")?.update("name", restaurantName)
            detailsCollectionRef?.document("Details")?.update("phoneNo", restaurantPhone)
            detailsCollectionRef?.document("Details")?.update("address", restaurantAddress)


            val db = FirebaseFirestore.getInstance()
            db.collection("macgyver").document(userId!!).collection("Categories")
                .get()
                .addOnSuccessListener {
                    documents ->
                   val currentCategories = documents.map { it.id }
                    if(mDataBinding.breakfastCB.isChecked){
                        if(!currentCategories.contains("Breakfast")){
                            addDocument("Breakfast")
                        }
                    }
                    else{
                        deleteDocument("Breakfast")
                    }
                    if(mDataBinding.lunchCB.isChecked){
                        if(!currentCategories.contains("Lunch")){
                            addDocument("Lunch")
                        }
                    }else{
                        deleteDocument("Lunch")
                    }
                    if(mDataBinding.dinnerCB.isChecked){
                        if(!currentCategories.contains("Dinner")){
                            addDocument("Dinner")
                        }
                    }else{
                        deleteDocument("Dinner")
                    }
                }

            val intent = Intent(requireActivity(), VendorDashboardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
    private fun addDocument(documentId: String): Task<Void> {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val documentRef = db.collection("macgyvers").document(userId!!).collection("Categories").document(documentId)
        return documentRef.set(emptyMap<String, Any>())
    }
    private fun deleteDocument(documentId: String) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val documentRef = db.collection("macgyvers").document(userId!!).collection("Categories").document(documentId)

        documentRef.delete()
            .addOnSuccessListener {
                println("Document deleted successfully.")
            }
            .addOnFailureListener { exception ->
                println("Error deleting document: ${exception.message}")
            }
    }
}

