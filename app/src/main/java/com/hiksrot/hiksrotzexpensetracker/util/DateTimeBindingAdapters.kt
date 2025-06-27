package com.hiksrot.hiksrotzexpensetracker.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("formattedDate")
fun TextView.setFormattedDate(timestamp: Long?) {
    if (timestamp == null) {
        text = ""
        return
    }
    val sdf = SimpleDateFormat("dd MMM yyyy hh.mm a", Locale("id"))
    text = sdf.format(Date(timestamp))
}