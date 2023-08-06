package com.jionex.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jionex.agent.R
import com.jionex.agent.data.model.response.GetBalanceByFilterResponse
import com.jionex.agent.data.model.response.GetMessageByFilterResponse
import com.jionex.agent.databinding.ItemBleManagerBinding
import com.jionex.agent.databinding.ItemListLayoutBinding
import com.jionex.agent.databinding.ItemSmsManagerBinding
import com.jionex.agent.utils.Utility
import com.jionex.agent.utils.getTagName

class SmsManagerListAdapter(private var itemList: ArrayList<GetMessageByFilterResponse>) :
    RecyclerView.Adapter<SmsManagerListAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(val binding: ItemSmsManagerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemSmsManagerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: GetMessageByFilterResponse = itemList[position]
        with(holder)
        {
            binding.txtSrNo.text = position.toString()
            binding.txtSms.text = item.textMessage
            binding.txtReceiver.text = item.receiver.toString()
            binding.txtSender.text = item.sender.toString()
            binding.txtDate.text = Utility.convertUtc2Local(item.smsDate) //item.date.toString()


        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}