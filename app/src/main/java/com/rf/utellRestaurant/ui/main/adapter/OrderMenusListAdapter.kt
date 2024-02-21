package com.rf.utellRestaurant.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.utellRestaurant.data.model.Menus
import com.rf.utellRestaurant.data.model.Order
import com.rf.utellRestaurant.databinding.OrderMenusItemBinding

class OrderMenusListAdapter(private var menusList: ArrayList<Menus>) :
    RecyclerView.Adapter<OrderMenusListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(order: Order)
    }

    var listener: CardEvent? = null
    lateinit var context: Context


    inner class ItemViewHolder(val binding: OrderMenusItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = OrderMenusItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val menuItem: Menus = menusList[position]
        with(holder)
        {
            binding.labelItem.text = menuItem.itemName
            binding.labelQty.text = menuItem.quantity
            binding.labelPrice.text = menuItem.price
        }
    }


    override fun getItemCount(): Int {
        return menusList.size
    }

    fun filterList(filterList: ArrayList<Menus>) {
        menusList = filterList
        notifyDataSetChanged()
    }

}