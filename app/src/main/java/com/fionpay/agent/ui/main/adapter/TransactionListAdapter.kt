package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.databinding.ItemTransactionManagerBinding
import com.fionpay.agent.utils.Utility

class TransactionListAdapter(private var itemList: ArrayList<TransactionModemResponse>) :
    RecyclerView.Adapter<TransactionListAdapter.ItemViewHolder>() {

    interface CardEvent {
        fun onCardClicked(title: TransactionModemResponse)
    }

    var listener: CardEvent? = null
    lateinit var context: Context


    inner class ItemViewHolder(val binding: ItemTransactionManagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTransactionManagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: TransactionModemResponse = itemList[position]

        with(holder)
        {
            setCardBgColor(binding, position)

            binding.txtDate.text = Utility.convertTransactionDate(item.date)
            binding.txtSuccess.text = item.status
            setStatusView(item, binding)
            setPaymentStatusView(item, binding)
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

    private fun setCardBgColor(binding: ItemTransactionManagerBinding, position: Int) {
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

    private fun setPaymentStatusView(
        item: TransactionModemResponse,
        binding: ItemTransactionManagerBinding
    ) {
        when (item.paymentType) {
            "Cash In" -> {
                if (item.status == "Approved") {
                    val amount = "- ৳${item.amount}"
                    val transactionId = "#${item.transactionId.toString()}"
                    binding.txtAmount.text = amount
                    binding.txtAmount.setTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            R.color.reject
                        )
                    )
                    binding.txtAmount.paintFlags =
                        binding.txtAmount.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    binding.txtTransactionId.text = transactionId
                } else {
                    val amount = "৳${item.amount}"
                    val customer = "☎ ${item.customer.toString()}"
                    binding.txtAmount.text = amount
                    binding.txtAmount.paintFlags =
                        binding.txtAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.txtAmount.setTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            R.color.textColor
                        )
                    )
                    binding.txtTransactionId.text = customer
                }

                binding.labelWithdraw.text = item.paymentType.toString()


            }

            "Cash Out" -> {
                if (item.status == "Approved") {
                    val amount = "+ ৳${item.amount}"
                    binding.txtAmount.text = amount
                    binding.txtAmount.setTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            R.color.approve
                        )
                    )
                    binding.txtAmount.paintFlags =
                        binding.txtAmount.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    val amount =  "৳${item.amount}"
                    binding.txtAmount.text = amount
                    binding.txtAmount.paintFlags =
                        binding.txtAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.txtAmount.setTextColor(
                        ContextCompat.getColorStateList(
                            context,
                            R.color.textColor
                        )
                    )
                }
                val transactionId =  "#${item.transactionId.toString()}"
                binding.txtTransactionId.text = transactionId
                binding.labelWithdraw.text = item.paymentType.toString()
            }
        }
    }

    private fun setStatusView(
        item: TransactionModemResponse,
        binding: ItemTransactionManagerBinding
    ) {
        when (item.status) {
            "Approved" -> {

                binding.txtSuccess.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.activeGreen
                    )
                )
                binding.txtSuccess.backgroundTintList =
                    (ContextCompat.getColorStateList(context, R.color.activeGreenBg))
            }

            "Rejected" -> {
                binding.txtSuccess.setTextColor(ContextCompat.getColor(context, R.color.reject))
                binding.txtSuccess.backgroundTintList =
                    (ContextCompat.getColorStateList(context, R.color.activeDangerBg))
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun filterList(filterList: ArrayList<TransactionModemResponse>) {
        itemList = filterList
        notifyDataSetChanged()
    }

}