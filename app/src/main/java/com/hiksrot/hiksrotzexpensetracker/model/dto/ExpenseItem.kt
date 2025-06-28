package com.hiksrot.hiksrotzexpensetracker.model.dto

data class ExpenseItem(
    val id: Int,
    val description: String,
    val amount: Double,
    val date: Long,
    val budgetName: String
)