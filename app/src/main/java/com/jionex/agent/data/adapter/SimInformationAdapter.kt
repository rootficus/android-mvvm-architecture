package com.jionex.agent.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.jionex.agent.R
import com.jionex.agent.roomDB.model.ModemSetting

class SimInformationAdapter(
    val arrayListSIMRecord: ArrayList<ModemSetting>
) :
    RecyclerView.Adapter<SimInformationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sim_information, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListSIMRecord.size
    }

    fun setItemValue(simRecord:ModemSetting){
        if (arrayListSIMRecord.size == 0 ){
            arrayListSIMRecord.add(simRecord)
        }else if (isNotContain(simRecord)){
            arrayListSIMRecord.add(simRecord)
        }
        Log.d("arrayListSIMRecord","size${arrayListSIMRecord.size}")
        notifyDataSetChanged()
    }

    private fun isNotContain(simRecord: ModemSetting): Boolean {
        arrayListSIMRecord.forEach {
            if(it.simId == simRecord.simId){
                return false
            }
        }
        return true
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var simRecord = arrayListSIMRecord[position]
        holder.simSlot.text = simRecord.type
        holder.networkInfo.text = simRecord.operator
        if (simRecord.phoneNumber?.isNotEmpty() == true) {
           // holder.phoneNumberEdit.visibility = View.GONE
            holder.phoneNumber.text = simRecord.phoneNumber
        }
        holder.phoneNumberEdit.visibility = View.GONE
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val simSlot: AppCompatTextView = itemView.findViewById(R.id.simSlot)
        val networkInfo: AppCompatTextView = itemView.findViewById(R.id.networkInfo)
        val phoneNumber: AppCompatTextView = itemView.findViewById(R.id.phoneNumber)
        val phoneNumberEdit: AppCompatTextView = itemView.findViewById(R.id.phoneNumberEdit)
    }
}