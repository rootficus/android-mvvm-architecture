package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.SupportResponse
import com.fionpay.agent.databinding.ItemSupportManagerBinding
import com.fionpay.agent.utils.Utility

class SupportListAdapter(private var itemList: ArrayList<SupportResponse>) :
    RecyclerView.Adapter<SupportListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onSupportItemClick(title: SupportResponse)
    }

    var listener: CardEvent? = null
    lateinit var context: Context


    inner class ItemViewHolder(val binding: ItemSupportManagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemSupportManagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: SupportResponse = itemList[position]

        with(holder)
        {
            setCardBgColor(binding, position)
            binding.txtDate.text = Utility.convertTransactionDate(item.createdAt)
            binding.txtTicketNumber.text = item.ticketNumber
            if (item.status.equals("open", true)) {
                binding.txtStatus.setTextColor(context.getColor(R.color.greenColor))
            } else if (item.status.equals("close", true)) {
                binding.txtStatus.setTextColor(context.getColor(R.color.reject))
            } else {
                binding.txtStatus.setTextColor(context.getColor(R.color.pending))
            }
            binding.txtSubject.text = item.subject
            binding.txtDescription.text = item.description
            binding.txtStatus.text = item.status

            binding.cardHead.setOnClickListener{
                //listener?.onSupportItemClick(item)
            }
        }
    }

    private fun setCardBgColor(binding: ItemSupportManagerBinding, position: Int) {
        if (position % 2 == 1) {
            binding.cardHead.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.activeCard
                )
            )
        } else {
            binding.cardHead.setBackgroundColor(
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

    fun filterList(filterList: ArrayList<SupportResponse>) {
        itemList = filterList
        notifyDataSetChanged()
    }

}