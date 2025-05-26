package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = BudgetEntity::class,
        parentColumns = ["id"],
        childColumns = ["budgetId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val date: String,
    val budgetId: Int
)