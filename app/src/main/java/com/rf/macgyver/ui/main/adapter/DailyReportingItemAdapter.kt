package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.macgyver.databinding.ItemDailyReportingBinding

class DailyReportingItemAdapter (private var dataList: ArrayList<Triple<String,String,String>>, private val context: Context?):
    RecyclerView.Adapter<DailyReportingItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val cartBinding = ItemDailyReportingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(cartBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        holder.reportId.setText(cardData.first)
        holder.vehicleId.setText(cardData.second)
        holder.dateId.setText(cardData.third)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemDailyReportingBinding) : RecyclerView.ViewHolder(binding.root) {
        var reportId = binding.etReportNo
        var vehicleId = binding.etVehicle
        var dateId = binding.etDate

    }

}