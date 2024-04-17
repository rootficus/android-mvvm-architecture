package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.macgyver.data.model.request.dailyReportData.Step1DrData
import com.rf.macgyver.databinding.ItemDailyReportingBinding

class DailyReportingItemAdapter (private var dataList: ArrayList<Step1DrData?>, private val context: Context?):
    RecyclerView.Adapter<DailyReportingItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val cartBinding = ItemDailyReportingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(cartBinding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        holder.reportId.setText(cardData?.reportName)
        holder.vehicleId.setText(cardData?.vehicle?.vehicleName)
        holder.dateId.setText(cardData?.date)
        when(cardData?.day){
            "Sunday" ->  {holder.sunday.setBackgroundColor(Color.parseColor("#8EBCFF"))}
            "Monday" ->  holder.monday.setBackgroundColor(Color.parseColor("#8EBCFF"))
            "Tuesday" ->  holder.tuesday.setBackgroundColor(Color.parseColor("#8EBCFF"))
            "Wednesday" ->  holder.wednesday.setBackgroundColor(Color.parseColor("#8EBCFF"))
            "Thursday" ->  holder.thursday.setBackgroundColor(Color.parseColor("#8EBCFF"))
            "Friday" ->  holder.friday.setBackgroundColor(Color.parseColor("#8EBCFF"))
            "Saturday" ->  holder.saturday.setBackgroundColor(Color.parseColor("#8EBCFF"))
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItemDailyReportingBinding) : RecyclerView.ViewHolder(binding.root) {
        var reportId = binding.etReportNo
        var vehicleId = binding.etVehicle
        var dateId = binding.etDate
        var monday = binding.Mtxt
        var tuesday = binding.Ttxt
        var wednesday = binding.Wtxt
        var thursday = binding.Thurstxt
        var friday = binding.Ftxt
        var saturday = binding.Sattxt
        var sunday = binding.Sundaytxt

    }

}