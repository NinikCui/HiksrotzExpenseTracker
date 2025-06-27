package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "user")
data class UserEntity(
    var username: String,

    var password: String,

    @ColumnInfo(name = "first_name")
    var firstName: String,

    @ColumnInfo(name = "last_name")
    var lastName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
