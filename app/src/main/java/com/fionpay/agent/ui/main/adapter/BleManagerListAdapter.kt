package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.BLTransactionModemResponse
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.databinding.ItemBleManagerBinding
import com.fionpay.agent.databinding.ItemTransactionManagerBinding
import com.fionpay.agent.utils.Utility

class BleManagerListAdapter(private var itemList: ArrayList<BLTransactionModemResponse>) :
    RecyclerView.Adapter<BleManagerListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(title: GetBalanceManageRecord)
    }

    var listener: CardEvent? = null
    lateinit var context: Context

    inner class ItemViewHolder(val binding: ItemBleManagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemBleManagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: BLTransactionModemResponse = itemList[position]
        with(holder)
        {
            setCardBgColor(binding, position)
            binding.txtCustomerNumber.text = item.customerNumber
            binding.txtAmount.text = "৳${item.amount}"
            binding.txtOldBalance.text = "৳${item.oldBalance.toString()}"
            binding.txtDate.text = Utility.convertTransactionDate(item.date)
            binding.txtSuccess.text = item.status
            setStatusView(item, binding)
        }
    }

    private fun setCardBgColor(binding: ItemBleManagerBinding, position: Int) {
        if (position % 2 == 1) {
            binding.layoutCard.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.activeCard
                )
            )
        } else {
            binding.layoutCard.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }
    private fun setStatusView(
        item: BLTransactionModemResponse,
        binding: ItemBleManagerBinding
    ) {
        when(item.status)
        {
            "Success" -> {
                binding.txtSuccess.setTextColor(ContextCompat.getColor(context, R.color.activeGreen))
                binding.txtSuccess.backgroundTintList = (ContextCompat.getColorStateList(context, R.color.activeGreenBg))
            }
            "Pending" -> {
                binding.txtSuccess.setTextColor(ContextCompat.getColor(context, R.color.pending))
                binding.txtSuccess.backgroundTintList = (ContextCompat.getColorStateList(context, R.color.activePendingBg))
            }
            "Danger" -> {
                binding.txtSuccess.setTextColor(ContextCompat.getColor(context, R.color.reject))
                binding.txtSuccess.backgroundTintList = (ContextCompat.getColorStateList(context, R.color.activeDangerBg))
            }
            "Rejected" ->{
                binding.txtSuccess.setTextColor(ContextCompat.getColor(context, R.color.reject))
                binding.txtSuccess.backgroundTintList = (ContextCompat.getColorStateList(context, R.color.activeDangerBg))
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun filterList(filterList: ArrayList<BLTransactionModemResponse>) {
        itemList = filterList
        notifyDataSetChanged()
    }

}