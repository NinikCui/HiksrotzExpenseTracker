package com.hiksrot.hiksrotzexpensetracker.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hiksrot.hiksrotzexpensetracker.model.daos.BudgetDao
import com.hiksrot.hiksrotzexpensetracker.model.daos.ExpenseDao
import com.hiksrot.hiksrotzexpensetracker.model.daos.UserDao
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity

@Database(
    entities = [UserEntity::class, BudgetEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao

}