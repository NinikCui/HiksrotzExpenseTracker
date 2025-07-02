package com.hiksrot.hiksrotzexpensetracker.model.dto

data class BudgetReport(
    val id: Int,
    val name: String,
    val totalBudget: Double,
    val transactionCount: Int,
    val totalSpent: Double
) {
    val budgetLeft: Double get() = totalBudget - totalSpent
    val percentLeft: Int get() =
        if (totalBudget == 0.0) 0 else (((totalBudget - totalSpent) / totalBudget) * 100).toInt()
}
