package com.rf.tiffinService.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rf.tiffinService.data.model.request.Data
import com.rf.tiffinService.data.model.request.ImageData
import com.rf.tiffinService.databinding.ItemCardBinding

class TiffinItemAdapter(private var dataList : List<Data>, private val context : Context?):
    RecyclerView.Adapter<TiffinItemAdapter.RecyclerViewHolder>() {

    interface CardEvent {
        fun onCardClicked()
    }
    var listener: CardEvent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData : Data = dataList[position]
        with(holder){
            /*cardData.imageUrl?.let { imageUrl.setImageResource(it) }*/
            itemname.text = cardData.item
            quantity.text = cardData.count.toString()
            binding.plusButton.setOnClickListener {
                if (cardData.count!! < 2) {
                    cardData.count = cardData.count!! + 1
                    notifyDataSetChanged()
                }else {
                    Toast.makeText(context, "Maximum Limit reached", Toast.LENGTH_SHORT).show()
                }
                listener?.onCardClicked()
            }
            binding.minusButton.setOnClickListener {
                if (cardData.count!! > 0) {
                    cardData.count = cardData.count!! - 1
                   notifyDataSetChanged()
                }
                listener?.onCardClicked()
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

