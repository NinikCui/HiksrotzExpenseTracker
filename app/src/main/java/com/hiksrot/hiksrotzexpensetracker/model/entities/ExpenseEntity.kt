package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = BudgetEntity::class,
        parentColumns = ["id"],
        childColumns = ["budget_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExpenseEntity(
    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "amount")
    var amount: Double,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "budget_id")
    var budgetId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}