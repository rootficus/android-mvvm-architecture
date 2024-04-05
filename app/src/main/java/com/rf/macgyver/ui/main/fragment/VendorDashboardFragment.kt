package com.rf.macgyver.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.data.model.request.ImageData
import com.rf.macgyver.databinding.AddItemAlertBinding
import com.rf.macgyver.databinding.FragmentVendorDashboardBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.main.adapter.VendorDashboardItemAdapter

class VendorDashboardFragment : BaseFragment<FragmentVendorDashboardBinding>(R.layout.fragment_vendor_dashboard) {

    private lateinit var itemTabLayout: TabLayout
    private var dataList: ArrayList<Data> = arrayListOf()
    private var dataImage: ImageData? = ImageData()
    private var breakfastData: ImageData? = ImageData()
    private var lunchData: ImageData? = ImageData()
    private var dinnerData: ImageData? = ImageData()
    private var breakfastList: ArrayList<Data> = arrayListOf()
    private var lunchList: ArrayList<Data> = arrayListOf()
    private var dinnerList: ArrayList<Data> = arrayListOf()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser!!.uid
    private var itemName: String? = null
     private var itemPrice: String? = null

    private var documentId: String? = ""
    private var fieldList: ArrayList<Data> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        //var x: ImageData?

        val docRef = FirebaseFirestore.getInstance()
        val categoriesData =
            docRef.collection("macgyvers").document(userId).collection("Categories")
        categoriesData.document("Breakfast").get()
            .addOnSuccessListener { documentSnapshot ->
                breakfastData = documentSnapshot.toObject(ImageData::class.java)
                breakfastData?.let { setAdapter(it) }
            }.addOnFailureListener {
                showMessage("${it.message}")
                Log.i("FailureListener", "${it.message}")
            }
        categoriesData.document("Lunch").get()
            .addOnSuccessListener { documentSnapshot ->
                lunchData = documentSnapshot.toObject(ImageData::class.java)
            }
        categoriesData.document("Dinner").get()
            .addOnSuccessListener { documentSnapshot ->
                dinnerData = documentSnapshot.toObject(ImageData::class.java)
            }

        itemTabLayout = mDataBinding.itemTabLayout
        val currentCategories: ArrayList<String> = arrayListOf()

        val db = FirebaseFirestore.getInstance()
        db.collection("macgyvers").document(userId).collection("Categories")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id
                    currentCategories.add(documentId)
                }
                println("Current Categories: $currentCategories") // Logging for debugging
                for (i in currentCategories) {
                    itemTabLayout.addTab(itemTabLayout.newTab().setText(i))
                }

                itemTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        val selectedCategory = tab.text.toString()
                        fetchDataAndSetAdapter(selectedCategory)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}
                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                })
                view?.post {
                    itemTabLayout.getTabAt(0)?.select()
                }
            }
            .addOnFailureListener {
                showMessage("${it.message}")
                Log.i("FailureListener", "${it.message}")

            }

        mDataBinding.addBtn.setOnClickListener {
            val mBuilder = AlertDialog.Builder(requireActivity())
            val view = AddItemAlertBinding.inflate(layoutInflater)
            mBuilder.setView(view.root)
            val dialog: AlertDialog = mBuilder.create()
            dialog.show()

            view.addItemBtn.setOnClickListener {
                breakfastData?.let { dataToList(breakfastList, it) }
                lunchData?.let { dataToList(lunchList, it) }
                dinnerData?.let { dataToList(dinnerList, it) }

                itemName = view.etItemName.text.toString()
                itemPrice = view.etItemPrice.text.toString()
                val itemCount = 0
                val itemTime = "00:00"
                val itemImage = ""
                if (itemName == null || itemPrice == null) {
                    Toast.makeText(context, "Please enter details", Toast.LENGTH_LONG).show()
                } else {
                    val newData = Data(
                        price = itemPrice!!.toInt(),
                        item = itemName,
                        count = itemCount,
                        time = itemTime,
                        imageUrl = itemImage
                    )
                    val tabSelected = itemTabLayout.selectedTabPosition
                    when (currentCategories[tabSelected]) {
                        "Breakfast" -> {
                            documentId = "Breakfast"
                            fieldList = breakfastList
                            dataImage = breakfastData
                        }

                        "Lunch" -> {
                            documentId = "Lunch"
                            fieldList = lunchList
                            dataImage = lunchData
                        }

                        "Dinner" -> {
                            documentId = "Dinner"
                            fieldList = dinnerList
                            dataImage = dinnerData
                        }
                    }

                    updateData(documentId, newData)
                    dialog.dismiss()
                }
            }
        }
    }

    fun fetchDataAndSetAdapter(category: String) {
        val docRef = FirebaseFirestore.getInstance()
        docRef.collection("macgyvers").document(userId).collection("Categories")
            .document(category)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val imageData = documentSnapshot.toObject(ImageData::class.java)
                setAdapter(imageData!!)
            }
            .addOnFailureListener {
                showMessage("${it.message}")
                Log.i("FailureListener", "${it.message}")
            }
    }

    private fun setAdapter(itemList: ImageData) {
        dataList.clear()
        itemList.let {
            it.data?.forEach { item ->
                dataList.add(item)
            }
        }
        val itemAdapter = VendorDashboardItemAdapter(dataList, requireActivity())
        val layoutManager = LinearLayoutManager(requireActivity())
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter
    }

    private fun updateData(documentId: String?, data: Data) {

        val list : ArrayList<Data> = arrayListOf()
        var dataImage: ImageData?
        val docRef = FirebaseFirestore.getInstance()
        val categoriesData =
            docRef.collection("macgyvers").document(userId).collection("Categories")

        categoriesData.document(documentId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                dataImage = documentSnapshot.toObject(ImageData::class.java)
                dataToList(list, dataImage!!)

                if (list.any { it.item == data.item }) {
                    list.removeIf { it.item == data.item }
                }

                list.add(data)
                val db = Firebase.firestore
                val usersCollection = db.collection("macgyvers").document(userId).collection("Categories")
                usersCollection.document(documentId).update("data", list)
                    .addOnSuccessListener {
                        usersCollection.document(documentId).get()
                            .addOnSuccessListener { documentSnapshot ->
                                dataImage = documentSnapshot.toObject(ImageData::class.java)
                                dataImage?.let { it2 -> setAdapter(it2) }
                            }
                        println("Document data updated successfully!")
                    }
                    .addOnFailureListener {
                        Log.i("FailureListener", "")
                    }
            }
    }
        private fun dataToList(list: ArrayList<Data>, imageData: ImageData) {
            list.clear()
            imageData.let {
                it.data?.forEach { item ->
                    list.add(item)
                }
            }
        }
    }



