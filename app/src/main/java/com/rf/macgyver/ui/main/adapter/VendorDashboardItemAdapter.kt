package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.databinding.ItemVendorDashboardBinding

class VendorDashboardItemAdapter(private var dataList: ArrayList<Data>, private val context: Context?):
    RecyclerView.Adapter<VendorDashboardItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val cartBinding = ItemVendorDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(cartBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        Glide.with(holder.imageUrl)
            .load(cardData.imageUrl)
            .into(holder.imageUrl)
        holder.itemName.text = cardData.item
        holder.price.text = cardData.price.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemVendorDashboardBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageUrl = binding.imgLabel
        var itemName = binding.itemName
        var price = binding.priceId

    }

}