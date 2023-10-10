package com.fionpay.agent.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.data.model.response.BLTransactionModemResponse
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.databinding.ItemBleManagerBinding
import com.fionpay.agent.utils.Utility

class BleManagerListAdapter(private var itemList: ArrayList<BLTransactionModemResponse>) :
    RecyclerView.Adapter<BleManagerListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(title: GetBalanceManageRecord)
    }

    var listener: CardEvent? = null

    inner class ItemViewHolder(val binding: ItemBleManagerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemBleManagerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: BLTransactionModemResponse = itemList[position]
        with(holder)
        {
            binding.txtSrNo.text = position.toString()
            //binding.txtAmount.text = item.amount
            //binding.txtCAccount.text = item.customerAccountNo.toString()
            //binding.txtTransactionId.text = item.transactionId
            //binding.txtDate.text = Utility.convertTodayUtc2Local(item.date)
            //binding.txtStatus.text = item.status
            binding.layout.setOnClickListener {
                //listener?.onCardClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}