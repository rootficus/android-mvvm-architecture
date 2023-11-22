package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.databinding.ItemPendingRequestBinding
import com.fionpay.agent.utils.Utility


class PendingListAdapter(private var itemList: ArrayList<PendingModemResponse>) :
    RecyclerView.Adapter<PendingListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(pendingModemResponse: PendingModemResponse)
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
            if (item.paymentType == "Cash In") {
                binding.txtTransactionId.text = "☎ ${item.customer.toString()}"
            } else {
                binding.txtTransactionId.text = "#${item.transactionId.toString()}"
            }

            binding.txtDate.text = Utility.convertTransactionDate(item.date)
            setStatusView(item, binding)
            if (!item.bankImage.isNullOrEmpty()) {
                Glide.with(context)
                    .asBitmap()
                    .centerInside()
                    .load(item.bankImage)
                    .error(R.drawable.bank_icon)
                    .into(binding.imageBank)
            }
            binding.cardHead.setOnClickListener {
                listener?.onCardClicked(item)
            }
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
            binding.layoutCard.setBackgroundColor(ContextCompat.getColor(context, R.color.sigInEditTextBackColor))
        }
    }

    private fun setStatusView(
        item: PendingModemResponse,
        binding: ItemPendingRequestBinding
    ) {
        when (item.paymentType) {
            "Cash In" -> {
                binding.labelCustomerNumber.text = "Cash In"
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.reject
                    )
                )
            }

            "Cash Out" -> {
                binding.labelCustomerNumber.text = "Cash Out"
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.greenColor
                    )
                )
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