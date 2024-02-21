package com.rf.utellRestaurant.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.data.model.Order
import com.rf.utellRestaurant.databinding.ItemOrderBinding

class OrderListAdapter (private var itemList: ArrayList<Order>) :
    RecyclerView.Adapter<OrderListAdapter.ItemViewHolder>() {
    private var selectedItem = 0
    interface CardEvent {
        fun onCardClicked(order: Order)
    }


    var listener: CardEvent? = null
    lateinit var context: Context


    inner class ItemViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            // Handle item click event
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedItem = position
                    notifyDataSetChanged() // Refresh the adapter to update item views
                }
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: Order = itemList[position]
        with(holder)
        {
            binding.txtUserName.text = item.name
            binding.txtOrderNumb.text = "Order #:${item.orderNumber}"
            binding.txtTime.text = "Date: ${item.date}"
            binding.orderMainLayout.setOnClickListener {
                selectedItem = adapterPosition
                notifyDataSetChanged()
                listener?.onCardClicked(item)
            }
            // Update background color based on the selected item
            //holder.itemView.isSelected = selectedItem == position
            if (selectedItem == position) {
                binding.orderMainLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            } else {
                binding.orderMainLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.bgPrimaryColor))
            }
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    fun filterList(filterList: ArrayList<Order>) {
        itemList = filterList
        notifyDataSetChanged()
    }

}