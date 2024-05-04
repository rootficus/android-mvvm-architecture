package com.rf.accessAli.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.accessAli.databinding.ItemListScreenBinding
import com.rf.accessAli.roomDB.model.UniversityData

class ListAdapter(private var context: Context, private var itemList: List<UniversityData>) :
    RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
    interface CardEvent {
        fun onCardClicked(data: UniversityData?)
    }

    var listener: CardEvent? = null

    inner class ItemViewHolder(val binding: ItemListScreenBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListScreenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: UniversityData = itemList[position]
        with(holder)
        {
            binding.uniName.text = item.name
            binding.uniState.text = item.stateProvince
            binding.itemLayout.setOnClickListener {
                listener?.onCardClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}