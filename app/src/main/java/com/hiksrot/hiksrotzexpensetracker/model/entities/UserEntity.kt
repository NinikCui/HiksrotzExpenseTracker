package com.hiksrot.hiksrotzexpensetracker.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "firstname")
    var firstName: String,

    @ColumnInfo(name = "lastname")
    var lastName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
