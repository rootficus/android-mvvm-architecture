package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.databinding.ItemPaymentBinding

class PaymentItemAdapter (private var dataList: ArrayList<Data>, private val context: Context?):
    RecyclerView.Adapter<PaymentItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val Binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(Binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        holder.itemName.text = cardData.item
        holder.itemCount.text = cardData.count.toString()
        val total : Int? = cardData.count?.times(cardData.price!!)
        holder.totalPrice.text = total.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemPaymentBinding) : RecyclerView.ViewHolder(binding.root) {

        var itemCount = binding.count
        var itemName = binding.itemName
        var totalPrice = binding.totalCost

    }

}