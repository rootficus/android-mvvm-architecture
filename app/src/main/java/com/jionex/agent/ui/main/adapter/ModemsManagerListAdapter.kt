package com.jionex.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jionex.agent.R
import com.jionex.agent.data.model.response.GetBalanceByFilterResponse
import com.jionex.agent.data.model.response.GetMessageByFilterResponse
import com.jionex.agent.data.model.response.GetModemsByFilterResponse
import com.jionex.agent.databinding.ItemBleManagerBinding
import com.jionex.agent.databinding.ItemListLayoutBinding
import com.jionex.agent.databinding.ItemModemsManagerBinding
import com.jionex.agent.databinding.ItemSmsManagerBinding
import com.jionex.agent.utils.getTagName

class ModemsManagerListAdapter(private var itemList: ArrayList<GetModemsByFilterResponse>) :
    RecyclerView.Adapter<ModemsManagerListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemModemsManagerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemModemsManagerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: GetModemsByFilterResponse = itemList[position]
        with(holder)
        {
            binding.txtSrNo.text = position.toString()
            binding.txtStatus.text = item.isActive.toString()
            binding.txtPhone.text = item.phoneNumber.toString()
            binding.txtOperator.text = item.operator.toString()
            binding.txtDeviceId.text = item.deviceId.toString()


        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}