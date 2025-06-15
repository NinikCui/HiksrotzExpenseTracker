package com.hiksrot.hiksrotzexpensetracker.model.daos

import androidx.room.*
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity

@Dao
interface UserDao {

    @Insert
    fun register(user: UserEntity)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String, password: String): UserEntity?

    @Query("""
        UPDATE user 
        SET username = :username, 
            password = :password, 
            first_name = :firstName, 
            last_name = :lastName 
        WHERE id = :id
    """)
    fun updateUser(
        id: Int,
        username: String,
        password: String,
        firstName: String,
        lastName: String
    )
}
