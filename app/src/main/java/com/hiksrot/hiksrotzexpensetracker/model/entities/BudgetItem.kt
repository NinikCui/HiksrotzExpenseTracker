package com.hiksrot.hiksrotzexpensetracker.model.entities

data class BudgetItem(
    val name: String,
    val totalBudget: Double,
    val totalSpent: Double
) {
    val budgetLeft: Double get() = totalBudget - totalSpent
    val progressPercent: Int get() = if (totalBudget == 0.0) 0 else ((totalSpent / totalBudget) * 100).toInt()
}

