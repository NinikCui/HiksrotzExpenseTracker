package com.hiksrot.hiksrotzexpensetracker.util

import java.text.NumberFormat
import java.util.Locale

object helper {
    fun formatRupiah(amount: Double): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        format.maximumFractionDigits = 0
        return format.format(amount).replace("Rp", "IDR ").trim()
    }
}