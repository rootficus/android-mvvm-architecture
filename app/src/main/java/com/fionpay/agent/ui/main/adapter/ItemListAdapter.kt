package com.fionpay.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.R
import com.fionpay.agent.databinding.ItemBleManagerBinding
import com.fionpay.agent.databinding.ItemListLayoutBinding

class ItemListAdapter(private var itemList: List<String>) :
    RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemListLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        with(holder)
        {
            binding.itemTextView.text = item
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateItemList(newItemList: List<String>) {
        itemList = newItemList
    }

}