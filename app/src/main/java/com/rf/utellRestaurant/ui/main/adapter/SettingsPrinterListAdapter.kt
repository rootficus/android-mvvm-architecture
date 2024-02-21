package com.rf.utellRestaurant.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rf.utellRestaurant.R

data class ListItem(val column1Value: String, val column2Value: String)

class SettingsPrinterListAdapter(private val dataList: List<ListItem>) :
    RecyclerView.Adapter<SettingsPrinterListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.printer_list_settings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (column1, column2) = dataList[position]
        holder.bind(column1, column2)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val printerColumn: TextView = itemView.findViewById(R.id.printer_label)
        private val statusColumn: TextView = itemView.findViewById(R.id.status_label)

        fun bind(column1: String,  column2: String) {
            printerColumn.text = column1
            statusColumn.text = column2
        }

    }
}
