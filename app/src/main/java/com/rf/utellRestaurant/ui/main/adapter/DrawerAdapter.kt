package com.rf.utellRestaurant.ui.main.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.rf.utellRestaurant.R


class DrawerAdapter(
    var context: Context,
    var drawerHash: HashMap<String, List<String>>,
    var expandableListTitle: ArrayList<String>
) :
    BaseExpandableListAdapter() {

    interface OnClickListener {
        fun click(text: String)
    }

    var listener: OnClickListener? = null

    override fun getGroupCount(): Int {
        return expandableListTitle.size
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.drawerHash[this.expandableListTitle[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListTitle[listPosition]
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.drawerHash[this.expandableListTitle[listPosition]]!![expandedListPosition]
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        listPosition: Int,
        b: Boolean,
        view: View?,
        viewGroup: ViewGroup
    ): View? {
        var convertView = view
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.adapter_header, viewGroup, false)
        }
        val listTitleTextView = convertView?.findViewById<TextView>(R.id.tv_name)
        listTitleTextView?.setTypeface(null, Typeface.BOLD)
        listTitleTextView?.text = listTitle
        return convertView
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        b: Boolean,
        view: View?,
        viewGroup: ViewGroup
    ): View? {
        var convertView = view
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.adapter_childview, viewGroup, false)
        }
        val textView = convertView?.findViewById<TextView>(R.id.tv_name)
        val textCount = convertView?.findViewById<TextView>(R.id.txt_count)
        val cardCount = convertView?.findViewById<CardView>(R.id.card_count)
        /* if(textView?.equals("Success") == true)
         {
             cardCount?.visibility = View.VISIBLE
             cardCount?.setBackgroundColor(ContextCompat.getColor(context, R.color.success))
         }else if(textView?.equals("Pending") == true)
         {
             cardCount?.visibility = View.VISIBLE
             cardCount?.setBackgroundColor(ContextCompat.getColor(context, R.color.pending))
         }else if(textView?.equals("Rejected") == true)
         {
             cardCount?.visibility = View.VISIBLE
             cardCount?.setBackgroundColor(ContextCompat.getColor(context, R.color.reject))
         } else{
             cardCount?.visibility = View.GONE
         }
         if(expandedListText.contains(',')) {
             textView?.text = expandedListText.split(',')[0]
             textCount?.text = expandedListText.split(',')[1]
         }
         else {*/
        textView?.text = expandedListText
        //}
        textView?.setOnClickListener {
            listener?.click(expandedListText)
        }
        return convertView
    }

    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return false
    }
}