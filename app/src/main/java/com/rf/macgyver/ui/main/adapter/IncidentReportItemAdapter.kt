package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.macgyver.data.model.request.incidentReportData.Step1IRData
import com.rf.macgyver.databinding.ItemDailyReportingBinding
import com.rf.macgyver.databinding.ItmIncidentReportBinding
import com.rf.macgyver.roomDB.model.IncidentReport

class IncidentReportItemAdapter(private var dataList: ArrayList<IncidentReport>, private val context: Context?):
    RecyclerView.Adapter<IncidentReportItemAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItmIncidentReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cardData = dataList[position]
        holder.incidentNo.setText(cardData.incidentNo)
        holder.locationId.setText(cardData.incidentLocation)
        holder.vehicleNoId.setText(cardData.vehicleNo)
        holder.dateId.setText(cardData.incidentDate)
        holder.reportedById.setText(cardData.operatorName)
        holder.nameId.setText(cardData.vehicleName)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class RecyclerViewHolder(val binding: ItmIncidentReportBinding) : RecyclerView.ViewHolder(binding.root) {
        var incidentNo = binding.IncidentReportId
        var locationId = binding.etLocationId
        var vehicleNoId = binding.etvehicleNoId
        var dateId = binding.etIncidentDateId
        var nameId = binding.etVehicleNameId
        var reportedById = binding.etReportedById

    }

}