package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.macgyver.databinding.ItemInspectionBinding

class InspectionItemAdapter (private var dataList: ArrayList<String>, private val context: Context?):
    RecyclerView.Adapter<InspectionItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemInspectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        holder.reportId.setText(cardData)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemInspectionBinding) : RecyclerView.ViewHolder(binding.root) {
        var reportId = binding.reportHeading

    }

}