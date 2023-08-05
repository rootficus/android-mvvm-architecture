package com.jionex.agent.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jionex.agent.R
import com.jionex.agent.data.model.response.GetBalanceByFilterResponse
import com.jionex.agent.databinding.ItemBleManagerBinding
import com.jionex.agent.databinding.ItemListLayoutBinding
import com.jionex.agent.utils.getTagName

class BleManagerListAdapter(private var itemList: ArrayList<GetBalanceByFilterResponse>) :
    RecyclerView.Adapter<BleManagerListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemBleManagerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemBleManagerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: GetBalanceByFilterResponse = itemList[position]
        with(holder)
        {
            binding.txtSrNo.text = position.toString()
            binding.txtAmount.text = item.amount
            binding.txtCustmAccNo.text = item.customerAccountNo.toString()
            binding.txtTransactionId.text = item.transactionId.toString()
            binding.txtDate.text = "24/07/2023"  //item.date.toString()


        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}