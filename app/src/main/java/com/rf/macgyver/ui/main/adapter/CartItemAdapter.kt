package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.databinding.ItemCartBinding

class CartItemAdapter(private var dataList: ArrayList<Data>, private val context: Context?):
    RecyclerView.Adapter<CartItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val cartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(cartBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        Glide.with(holder.imageUrl)
            .load(cardData.imageUrl)
            .into(holder.imageUrl)
        holder.itemName.text = cardData.item
        holder.itemCount.text = cardData.count.toString()
        holder.itemCountMultiply.text = cardData.count.toString()
        holder.priceId.text = cardData.price.toString()
        holder.timeId.text = cardData.time
        val total : Int? = cardData.count?.times(cardData.price!!)
        holder.totalPrice.text = total.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageUrl = binding.imgLabel
        var itemCount = binding.countLabel
        var itemName = binding.itemName
        var timeId = binding.timeLabel
        var priceId = binding.singleItemCost
        var totalPrice = binding.totalCost
        var itemCountMultiply = binding.multiplyCount

    }

}