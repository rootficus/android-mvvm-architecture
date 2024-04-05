package com.rf.macgyver.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.Detailsdata
import com.rf.macgyver.databinding.FragmentVendorLoginBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.main.activity.VendorDashboardActivity

class VendorLoginFragment : BaseFragment<FragmentVendorLoginBinding>(R.layout.fragment_vendor_login) {

    private var userEmail : String? = null
    private var userPassword : String? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser!!.uid
    private var restaurantDetails : Detailsdata? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }
    private fun initializeView(){
        mDataBinding.loginBtn.setOnClickListener {
            userEmail = mDataBinding.etUserName.text.toString()
            userPassword = mDataBinding.etPassword.text.toString()
            if(userEmail==null || userPassword==null) {
                Toast.makeText(context,"Enter email or password", Toast.LENGTH_LONG ).show()
            }
            else{
                loginUser(userEmail!!, userPassword!!)
            }
        }
    }
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val docRef = FirebaseFirestore.getInstance()
                    val categoriesData =
                        docRef.collection("macgyvers").document(userId).collection("Restaurant details").document("Details")
                    categoriesData.get().addOnSuccessListener { documentSnapshot ->
                        restaurantDetails = documentSnapshot.toObject(Detailsdata::class.java)

                    if(restaurantDetails == null ||restaurantDetails?.name == null || restaurantDetails?.phoneNo == null || restaurantDetails?.address ==null){
                    findNavController().navigate(R.id.action_fragment_Vendor_login_to_fragment_signup)}
                    else{
                        val intent = Intent(requireActivity(), VendorDashboardActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish() }
                    }.addOnFailureListener {exception ->
                            val TAG = "FETCH_DETAILS_ERROR"
                            Log.e(TAG, "Failed to fetch details", exception) }
                } else {
                    Toast.makeText(context, "incorrect credentials", Toast.LENGTH_SHORT).show()
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                }
            }
    }
}