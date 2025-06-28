package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.*

@Entity(
    tableName = "budgets",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id"])]
)
data class BudgetEntity(
    var name: String,

    var amount: Double,

    @ColumnInfo(name = "user_id")
    var userId: Int,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = System.currentTimeMillis() / 1000
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

