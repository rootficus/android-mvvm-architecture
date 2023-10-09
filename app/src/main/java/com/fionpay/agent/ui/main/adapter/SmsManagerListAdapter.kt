package com.fionpay.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.databinding.ItemTransactionManagerBinding

class SmsManagerListAdapter(private var itemList: ArrayList<GetMessageManageRecord>) :
    RecyclerView.Adapter<SmsManagerListAdapter.ItemViewHolder>() {

    interface SmsCardEvent {
        fun onCardClicked(title: GetMessageManageRecord)
    }

    var listener: SmsCardEvent? = null

    inner class ItemViewHolder(val binding: ItemTransactionManagerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTransactionManagerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: GetMessageManageRecord = itemList[position]
        with(holder)
        {
            binding.cardHead.setOnClickListener {
                listener?.onCardClicked(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}