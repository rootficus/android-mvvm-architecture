package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.macgyver.databinding.ItemDailyReportingBinding
import com.rf.macgyver.databinding.ItmIncidentReportBinding

class IncidentReportItemAdapter(private var dataList: ArrayList<Triple<String,String,String>>, private val context: Context?):
    RecyclerView.Adapter<IncidentReportItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItmIncidentReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        holder.locationId.setText(cardData.first)
        holder.vehicleNoId.setText(cardData.second)
        holder.dateId.setText(cardData.third)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItmIncidentReportBinding) : RecyclerView.ViewHolder(binding.root) {
        var locationId = binding.etLocationId
        var vehicleNoId = binding.etvehicleNoId
        var dateId = binding.etIncidentDateId
        var nameId = binding.etVehicleNameId
        var reportedById = binding.etReportedById

    }

}