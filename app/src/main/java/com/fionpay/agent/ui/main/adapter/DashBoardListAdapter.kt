package com.fionpay.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.HomeCardBinding

class DashBoardListAdapter(private var itemList: ArrayList<TransactionModel>) :
    RecyclerView.Adapter<DashBoardListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(title: GetBalanceManageRecord)
    }

    var listener: CardEvent? = null

    inner class ItemViewHolder(val binding: HomeCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = HomeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: TransactionModel = itemList[position]
        with(holder)
        {
            binding.txtAmount.text = item.amount
            binding.txtTitle.text = item.title
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}