package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BudgetEntity(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "amount")
    var amount: Double,

    @ColumnInfo(name = "user_id")
    var userId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
