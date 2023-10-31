package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.databinding.ItemBankBinding

class BankListAdapter(
    private var itemList: List<Bank>?
) :
    RecyclerView.Adapter<BankListAdapter.ItemViewHolder>() {

    lateinit var context: Context

    interface BankCardEvent {
        fun onCardClick(bankId: Int?)
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
            ///setPermissionStatus(item, binding)

            binding.cardHead.setOnClickListener {
                //if (item?.bankId == modemSlot.mobileBankingId) {
                    binding.bankImage.setBackgroundResource(R.drawable.selected_bank)
                    binding.txtBankName.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.bankLayout.setBackgroundResource(R.drawable.button_continue_gradient)
               /* } else {
                    binding.bankImage.setBackgroundResource(R.drawable.non_selected_bank)
                    binding.txtBankName.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primaryBlue
                        )
                    )
                    binding.bankLayout.setBackgroundResource(R.drawable.round_bank_card_corners)
                }*/
                listener?.onCardClick(item?.bankId)
                notifyDataSetChanged()
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