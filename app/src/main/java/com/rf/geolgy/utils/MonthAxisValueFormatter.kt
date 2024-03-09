package com.rf.geolgy.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DateFormatSymbols


class MonthAxisValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
        val month = value.toInt()
        return DateFormatSymbols().shortMonths[month]
    }
}
