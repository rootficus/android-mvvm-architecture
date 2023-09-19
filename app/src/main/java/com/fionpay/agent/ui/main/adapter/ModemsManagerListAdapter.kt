package com.fionpay.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsByFilterResponse
import com.fionpay.agent.databinding.ItemModemsManagerBinding

class ModemsManagerListAdapter(private var itemList: ArrayList<GetModemsByFilterResponse>) :
    RecyclerView.Adapter<ModemsManagerListAdapter.ItemViewHolder>() {

    interface ModemCardEvent {
        fun onCardClicked(title: GetModemsByFilterResponse)
    }

    var listener: ModemCardEvent? = null

    inner class ItemViewHolder(val binding: ItemModemsManagerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemModemsManagerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: GetModemsByFilterResponse = itemList[position]
        with(holder)
        {
     /*       binding.txtSrNo.text = position.toString()
            binding.txtStatus.text = item.isActive.toString()
            binding.txtPhone.text = item.phoneNumber.toString()
            binding.txtOperator.text = item.operator.toString()
            binding.txtDeviceId.text = item.deviceId.toString()
            binding.cardHead.setOnClickListener {
                listener?.onCardClicked(item)
            }*/
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}