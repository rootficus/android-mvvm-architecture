package com.rf.macgyver.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.inspectionFormData.InspectionFormData
import com.rf.macgyver.databinding.ItemInspectionBinding

class InspectionItemAdapter(
    private var dataList: ArrayList<InspectionFormData>,
    private val context: Context?
) :
    RecyclerView.Adapter<InspectionItemAdapter.RecyclerViewHolder>() {

    interface InspectionCardEvent {
        fun onItemClicked(inspectionFormData: InspectionFormData)
    }

    var listener: InspectionCardEvent? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        var binding =
            ItemInspectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val item = dataList[position]
        with(holder.binding)
        {
            reportHeading.text = item.title
            txtConcernLevel.text = item.concernLevel
            inspectionCardLayout.setOnClickListener {
                listener?.onItemClicked(item)
            }
            if (item.circle1 == true) {
                circle1.setImageResource(R.drawable.circle_selected);
            } else {
                circle1.setImageResource(R.drawable.circle_unselected);
            }
            if (item.circle2 == true) {
                circle2.setImageResource(R.drawable.circle_selected);
            } else {
                circle2.setImageResource(R.drawable.circle_unselected);
            }
            if (item.circle3 == true) {
                circle3.setImageResource(R.drawable.circle_selected);
            } else {
                circle3.setImageResource(R.drawable.circle_unselected);
            }
            if (item.circle4 == true) {
                circle4.setImageResource(R.drawable.circle_selected);
            } else {
                circle4.setImageResource(R.drawable.circle_unselected);
            }
            if (item.circle5 == true) {
                circle5.setImageResource(R.drawable.circle_selected);
            } else {
                circle5.setImageResource(R.drawable.circle_unselected);
            }
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class RecyclerViewHolder(val binding: ItemInspectionBinding) :
        RecyclerView.ViewHolder(binding.root)

}