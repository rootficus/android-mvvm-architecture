package com.rf.tiffinService.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.tiffinService.data.model.request.Data
import com.rf.tiffinService.databinding.ItemCartBinding

class CartItemAdapter(private var dataList: ArrayList<Data>, private val context: Context?):
    RecyclerView.Adapter<CartItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val cartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(cartBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        //cardData.item?.let { holder.imageUrl.setImageResource(it) }
        holder.itemName.text = cardData.item
        holder.itemCount.text = cardData.count.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageUrl = binding.imgLabel
        var itemCount = binding.countLabel
        var itemName = binding.itemName
    }

}