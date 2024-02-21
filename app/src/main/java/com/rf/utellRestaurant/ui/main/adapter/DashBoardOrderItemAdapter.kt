package com.rf.utellRestaurant.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rf.utellRestaurant.R

class DashBoardOrderItemAdapter(private val dataList: List<Triple<String, String, String>>) :
    RecyclerView.Adapter<DashBoardOrderItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (column1, column2, column3) = dataList[position]
        holder.bind(column1, column2, column3)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemColumn: TextView = itemView.findViewById(R.id.item_label)
        private val priceColumn: TextView = itemView.findViewById(R.id.price_label)
        private val statusColumn: TextView = itemView.findViewById(R.id.status_label)

        fun bind(column1: String, column2: String, column3: String) {
            itemColumn.text = column1
            priceColumn.text = column2
            statusColumn.text = column3
        }
    }
}
