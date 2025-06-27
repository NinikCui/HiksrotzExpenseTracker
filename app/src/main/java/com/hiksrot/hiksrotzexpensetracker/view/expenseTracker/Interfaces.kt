package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.view.View
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity

interface ExpenseClickListener {
    // dipanggil oleh FAB
    fun onAddExpenseClick(v: View)
    // dipanggil oleh tiap item
    fun onExpenseItemClick(v: View)
}