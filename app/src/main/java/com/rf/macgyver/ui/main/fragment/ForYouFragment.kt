package com.rf.macgyver.ui.main.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.data.model.request.ImageData
import com.rf.macgyver.databinding.FragmentForYouBinding
import com.rf.macgyver.databinding.ItemCardBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.main.adapter.SliderAdapter
import com.rf.macgyver.ui.main.adapter.TiffinItemAdapter
import com.rf.macgyver.ui.main.adapter.TopPickAdapter
import java.util.Timer
import java.util.TimerTask

class ForYouFragment : BaseFragment<FragmentForYouBinding>(R.layout.fragment_for_you) {

    private lateinit var viewPager: ViewPager
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var tabLayout: TabLayout
    private val images = listOf(
        R.drawable.kalyani_akella_vcnlo20cuwy_unsplash,
        R.drawable._26597,
        R.drawable.pexels_chan_walrus_1059905
    )

    private lateinit var itemTabLayout: TabLayout

    private var dataList: ArrayList<Data> = arrayListOf()
    private var breakfastData: ImageData? = ImageData()
    private var lunchData: ImageData? = ImageData()
    private var dinnerData: ImageData? = ImageData()
    private var currentPage = 0
    private val timer = Timer()
    private var breakfastList: ArrayList<Data> = arrayListOf()
    private var lunchList: ArrayList<Data> = arrayListOf()
    private var dinnerList: ArrayList<Data> = arrayListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.post{
            itemTabLayout.getTabAt(0)?.select()

        }
        initializeView()
    }

    private fun initializeView(){

        val cardBinding = ItemCardBinding.inflate(layoutInflater)
        cardBinding.itemName.background.alpha = 70

        //Slider
        viewPager = mDataBinding.slider
        tabLayout = mDataBinding.tabLayout
        sliderAdapter = SliderAdapter(requireActivity(), images)
        viewPager.adapter = sliderAdapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        timer.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    if (currentPage == images.size) {
                        currentPage = 0
                    }
                    viewPager.setCurrentItem(currentPage++, true)
                }
            }
        }, 3000, 3000)

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
                    }
                    val dinnerRef = macgyversRef.document(userId)
                        .collection("Categories")
                        .document("Dinner")
                    dinnerRef.get().addOnSuccessListener { documentSnapshot ->
                        dinnerData= documentSnapshot.toObject(ImageData::class.java)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        itemTabLayout= mDataBinding.itemTabLayout
        itemTabLayout.addTab(itemTabLayout.newTab().setText("Breakfast"))
        itemTabLayout.addTab(itemTabLayout.newTab().setText("Lunch"))
        itemTabLayout.addTab(itemTabLayout.newTab().setText("Dinner"))
        var x : ImageData?

        itemTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {x = breakfastData
                        x?.let { setAdapter(it) }}
                    1 -> {x = lunchData
                        x?.let { setAdapter(it) }}
                    2 -> {x = dinnerData
                        x?.let { setAdapter(it) }}
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setAdapter(itemList: ImageData) {
        dataList.clear()
        itemList.let {
            it.data?.forEach { item ->
                dataList.add(item)
            }
        }
        val itemAdapter = TiffinItemAdapter(dataList, requireActivity())
        val layoutManager = GridLayoutManager(requireActivity(),2)
        itemAdapter.listener = cardListener
        mDataBinding.recyclerViewId.layoutManager = layoutManager
        mDataBinding.recyclerViewId.adapter = itemAdapter

        val topPickAdapter = TopPickAdapter(dataList, requireActivity())
        val topPickLayoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL, false)
        mDataBinding.TopPickRecyclerView.layoutManager = topPickLayoutManager
        mDataBinding.TopPickRecyclerView.adapter = topPickAdapter

    }
    private val cardListener = object  : TiffinItemAdapter.CardEvent {
        override fun onCardClicked(data: Data) {

            var documentId : String? = ""
            var fieldList : ArrayList<Data> = arrayListOf()

            breakfastData?.let { dataToList(breakfastList, it) }
            lunchData?.let { dataToList(lunchList, it) }
            dinnerData?.let { dataToList(dinnerList, it) }

            if(breakfastList.any { it.item == data.item } ){
                fieldList.clear()
                documentId = "Breakfast"
                fieldList = breakfastList
            }else if(lunchList.any { it.item == data.item } ){
                fieldList.clear()
                documentId = "Lunch"
                fieldList = lunchList
            }else if(dinnerList.any { it.item == data.item } ){
                fieldList.clear()
                documentId = "Dinner"
                fieldList = dinnerList
            }
                updateData(documentId,fieldList, data)
        }
    }
    fun updateData (documentId : String?,list: ArrayList<Data>,data :Data){
        if(data.count!! <=2) {
            if (list.any { it.item == data.item }) {
                list.removeIf { it.item == data.item }
            }
            list.add(data)
            val db = Firebase.firestore
            val macgyversRef = db.collection("macgyvers")
            macgyversRef.get()
                .addOnSuccessListener { documents ->
                    for (userDocument in documents) {
                        val userId = userDocument.id
                        val documentRef = macgyversRef.document(userId)
                            .collection("Categories")
                            .document(documentId!!)

                        documentRef.update("data", list)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully updated for user $userId")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error updating document for user $userId", e)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }
    private fun dataToList(list :ArrayList<Data>, imageData: ImageData){
        list.clear()
        imageData.let {
            it.data?.forEach { item ->
                list.add(item)
            }
        }
    }
}

