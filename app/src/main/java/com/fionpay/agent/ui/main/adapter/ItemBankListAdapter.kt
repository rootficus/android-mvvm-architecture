package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.Slots
import com.fionpay.agent.databinding.ItemModemBankBinding

class ItemBankListAdapter(private var itemList: List<Slots>) :
    RecyclerView.Adapter<ItemBankListAdapter.ItemViewHolder>() {
    lateinit var context: Context

    inner class ItemViewHolder(val binding: ItemModemBankBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val binding =
            ItemModemBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        with(holder)
        {
            Glide.with(context)
                .asBitmap()
                .centerInside()
                .load(R.drawable.bank_icon)
                .error(R.drawable.bank_icon)
                .into(binding.imageBank)
            binding.txtBankName.text = item.bankName
            binding.txtNumber.text = item.phoneNumber
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}