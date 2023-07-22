package com.jionex.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jionex.agent.R

class ItemListAdapter(private var itemList: List<String>) :
    RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemTextView.text = item
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateItemList(newItemList: List<String>) {
        itemList = newItemList
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.itemTextView)
    }
}