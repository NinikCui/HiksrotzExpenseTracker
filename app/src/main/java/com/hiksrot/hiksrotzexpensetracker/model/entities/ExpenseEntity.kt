package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.*

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budget_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id"), Index(value = ["budget_id"])]
)
data class ExpenseEntity(

    var description: String,

    var amount: Double,

    val date: Long,

    @ColumnInfo(name = "budget_id")
    var budgetId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
