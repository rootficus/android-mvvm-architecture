package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.B2BResponse
import com.fionpay.agent.databinding.ItemB2bManagerBinding
import com.fionpay.agent.utils.Utility

class B2BListAdapter(private var itemList: ArrayList<B2BResponse>) :
    RecyclerView.Adapter<B2BListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(title: B2BResponse)
    }

    var listener: CardEvent? = null
    lateinit var context: Context


    inner class ItemViewHolder(val binding: ItemB2bManagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemB2bManagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: B2BResponse = itemList[position]

        with(holder)
        {
            setCardBgColor(binding, position)
            binding.txtDate.text = Utility.convertTransactionDate(item.date)
            binding.labelEmail.text = item.modem

            if (item.paymentType == "Return" && item.modem?.contains("Distributor") == false) {
                val amount = "+ ${item.amount}"
                binding.txtAmount.text = amount
                binding.txtAmount.setTextColor(context.getColor(R.color.greenColor))
            } else if (item.modem?.contains("Distributor") == true) {
                val amount = "- ${item.amount}"
                binding.txtAmount.text = amount
                binding.txtAmount.setTextColor(context.getColor(R.color.reject))
            } else {
                val amount = "- ${item.amount}"
                binding.txtAmount.text = amount
                binding.txtAmount.setTextColor(context.getColor(R.color.reject))
            }
            binding.txtModemType.text = item.modem

        }
    }

    private fun setCardBgColor(binding: ItemB2bManagerBinding, position: Int) {
        if (position % 2 == 1) {
            binding.layoutCard.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.activeCard
                )
            )
        } else {
            binding.layoutCard.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.sigInEditTextBackColor
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun filterList(filterList: ArrayList<B2BResponse>) {
        itemList = filterList
        notifyDataSetChanged()
    }

}