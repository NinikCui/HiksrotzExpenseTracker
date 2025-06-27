package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.view.View
import com.hiksrot.hiksrotzexpensetracker.model.dto.ExpenseItem

interface ExpenseClickListener {
    fun onAddExpenseClick(v: View)
    fun onExpenseItemClick(v: View, exp: ExpenseItem)
}