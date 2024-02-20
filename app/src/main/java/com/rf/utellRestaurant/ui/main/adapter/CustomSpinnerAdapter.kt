package com.rf.utellRestaurant.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import com.rf.utellRestaurant.R


class CustomSpinnerAdapter(
    context: Context,
    private val items: List<String>,
    private val icons: IntArray
) : ArrayAdapter<String>(context, R.layout.custom_spinner_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            val inflater = LayoutInflater.from(context)
            itemView = inflater.inflate(R.layout.custom_spinner_item, parent, false)
        }

        val textView = itemView!!.findViewById<TextView>(R.id.spinner_text)
        val imageView = itemView.findViewById<ImageView>(R.id.spinner_icon)

        textView.text = items[position]
        imageView.setImageResource(icons[position])

        return itemView
    }
}