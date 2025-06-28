package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.view.View
import com.hiksrot.hiksrotzexpensetracker.model.dto.ExpenseItem

interface ExpenseListListener {
    fun onFABAddExpenseClick(v: View)
    fun onExpenseItemClick(v: View, exp: ExpenseItem)
}

interface NewExpenseListener {
    fun onAddExpenseClick(v: View)
}
