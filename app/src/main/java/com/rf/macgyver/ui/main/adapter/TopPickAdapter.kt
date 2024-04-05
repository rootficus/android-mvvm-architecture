package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.databinding.ItemTopPickBinding

class TopPickAdapter(private var dataList: ArrayList<Data>, private val context: Context?):
    RecyclerView.Adapter<TopPickAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val topPickBinding = ItemTopPickBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(topPickBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val itemData = dataList[position]
        Glide.with(holder.imageUrl)
            .load(itemData.imageUrl)
            .into(holder.imageUrl)
        holder.itemName.text = itemData.item
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemTopPickBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageUrl = binding.dishImg
        var itemName = binding.itemName
    }

}