package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.databinding.ItemPendingRequestBinding
import com.fionpay.agent.utils.Utility

class PendingListAdapter(private var itemList: ArrayList<PendingModemResponse>) :
    RecyclerView.Adapter<PendingListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(title: GetBalanceManageRecord)
    }

    var listener: CardEvent? = null
    lateinit var context: Context


    inner class ItemViewHolder(val binding: ItemPendingRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemPendingRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: PendingModemResponse = itemList[position]

        with(holder)
        {
            setCardBgColor(binding, position)
            binding.txtAmount.text = "৳${item.amount}"
            binding.txtTransactionId.text = "#${item.transactionId.toString()}"
            binding.txtDate.text = Utility.convertTransactionDate(item.date)
            setStatusView(item, binding)
        }
    }

    private fun setCardBgColor(binding: ItemPendingRequestBinding, position: Int) {
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
        item: PendingModemResponse,
        binding: ItemPendingRequestBinding
    ) {
        when (item.status) {
            "Success" -> {
                binding.labelCustomerNumber.text = "Cash In"
                binding.imgWithdraw.setBackgroundResource(R.drawable.arrow_left)
                binding.imgWithdraw.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.reject)
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.reject
                    )
                )
            }

            "Pending" -> {
                binding.labelCustomerNumber.text = "Cash Out"
                binding.imgWithdraw.setBackgroundResource(R.drawable.arrow_right)
                binding.imgWithdraw.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.greenColor)
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.greenColor
                    )
                )
            }

            "Danger" -> {
                // binding.txtSuccess.setTextColor(ContextCompat.getColor(context, R.color.reject))
                // binding.txtSuccess.backgroundTintList = (ContextCompat.getColorStateList(context, R.color.activeDangerBg))
            }

            "Rejected" -> {
                // binding.txtSuccess.setTextColor(ContextCompat.getColor(context, R.color.reject))
                // binding.txtSuccess.backgroundTintList = (ContextCompat.getColorStateList(context, R.color.activeDangerBg))
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun filterList(filterList: ArrayList<PendingModemResponse>) {
        itemList = filterList
        notifyDataSetChanged()
    }


}