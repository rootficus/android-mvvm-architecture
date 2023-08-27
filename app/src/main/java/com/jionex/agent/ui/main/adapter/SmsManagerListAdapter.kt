package com.jionex.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jionex.agent.data.model.response.GetMessageManageRecord
import com.jionex.agent.databinding.ItemSmsManagerBinding

class SmsManagerListAdapter(private var itemList: ArrayList<GetMessageManageRecord>) :
    RecyclerView.Adapter<SmsManagerListAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(val binding: ItemSmsManagerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemSmsManagerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: GetMessageManageRecord = itemList[position]
        with(holder)
        {
            binding.txtSrNo.text = position.toString()
            binding.txtMessage.text = item.textMessage
            binding.txtReceiver.text = item.receiver.toString()
            binding.txtSender.text = item.sender.toString()
            binding.txtDate.text = ""+item.smsType//Utility.convertTodayUtc2Local(item.smsDate) //item.date.toString()

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}