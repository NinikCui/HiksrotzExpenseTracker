package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.*

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budget_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["budget_id"])]
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
