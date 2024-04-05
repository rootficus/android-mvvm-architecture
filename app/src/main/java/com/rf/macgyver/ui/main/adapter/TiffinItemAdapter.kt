package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rf.macgyver.data.model.request.Data
import com.rf.macgyver.databinding.ItemCardBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TiffinItemAdapter(private var dataList : List<Data>, private val context : Context?):
    RecyclerView.Adapter<TiffinItemAdapter.RecyclerViewHolder>() {

    interface CardEvent {
        fun onCardClicked(data: Data)

    }
    var listener: CardEvent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData : Data = dataList[position]
        with(holder){
            itemname.text = cardData.item
            Glide.with(holder.imageUrl)
                .load(cardData.imageUrl)
                .into(holder.imageUrl)

            quantity.text = cardData.count.toString()
            binding.plusButton.setOnClickListener {
                val formatter = DateTimeFormatter.ofPattern("hh.mm a")
                val formattedDateTime = LocalDateTime.now().format(formatter)
                cardData.time = formattedDateTime
                if (cardData.count!! < 2) {
                    cardData.count = cardData.count!! + 1
                    notifyDataSetChanged()
                }else {
                    Toast.makeText(context, "Maximum Limit reached", Toast.LENGTH_SHORT).show()
                }
                listener?.onCardClicked(dataList[position])
            }
            binding.minusButton.setOnClickListener {
                val formatter = DateTimeFormatter.ofPattern("hh.mm a")
                val formattedDateTime = LocalDateTime.now().format(formatter)
                cardData.time = formattedDateTime
                if (cardData.count!! > 0) {
                    cardData.count = cardData.count!! - 1
                   notifyDataSetChanged()
                }
                listener?.onCardClicked(dataList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class RecyclerViewHolder(val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var imageUrl = binding.imgLabel
        var quantity = binding.countLabel
        var itemname = binding.itemName
    }
}

