package com.hiksrot.hiksrotzexpensetracker.model.daos

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity


interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUser(username: String, password: String): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)
}
