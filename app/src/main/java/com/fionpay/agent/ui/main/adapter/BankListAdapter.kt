package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.databinding.ItemBankBinding

class BankListAdapter(
    private var itemList: List<Bank>?
) :
    RecyclerView.Adapter<BankListAdapter.ItemViewHolder>() {

    lateinit var context: Context

    interface BankCardEvent {
        fun onCardClick(bank: Bank)
    }

    var listener: BankCardEvent? = null

    inner class ItemViewHolder(val binding: ItemBankBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val binding =
            ItemBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: Bank? = itemList?.get(position)
        with(holder)
        {
            binding.txtBankName.text = item?.bankName
            binding.txtMobile.text = item?.phoneNumber ?: "Not Linked"
            Glide.with(context)
                .asBitmap()
                .centerInside()
                .load(item?.bankImage)
                .error(R.drawable.bank_icon)
                .into(binding.bankImage)
            ///setPermissionStatus(item, binding)
            if (item?.phoneNumber?.isNotEmpty() == true && item?.phoneNumber?.isDigitsOnly()!!) {
                //binding.bankImage.setBackgroundResource(R.drawable.selected_bank)
                binding.txtBankName.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.txtMobile.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding.bankLayout.setBackgroundResource(R.drawable.button_continue_gradient)

            } else {
                //binding.bankImage.setBackgroundResource(R.drawable.non_selected_bank)
                binding.txtBankName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.homeTextColor
                    )
                )
                binding.txtMobile.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.homeTextColor
                    )
                )
                binding.bankLayout.setBackgroundResource(R.drawable.round_bank_card_corners)
            }
            binding.cardHead.setOnClickListener {

                if (item != null) {
                    listener?.onCardClick(item)
                }
                // notifyDataSetChanged()
            }
        }
    }


    fun filterList(filterList: ArrayList<Bank>) {
        itemList = filterList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

}