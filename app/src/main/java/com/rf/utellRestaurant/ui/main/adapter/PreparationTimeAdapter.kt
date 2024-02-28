package com.rf.utellRestaurant.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.data.model.request.PreparationTimeData

class PreparationTimeAdapter(private var dataList: List<PreparationTimeData>, private val context: Context?) :
    RecyclerView.Adapter<PreparationTimeAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.preparation_time_card, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val PreparationTimeData = dataList[position]
        holder.timeTxt.text = PreparationTimeData.time
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeTxt: TextView = itemView.findViewById(R.id.timeLabel)
    }

}

