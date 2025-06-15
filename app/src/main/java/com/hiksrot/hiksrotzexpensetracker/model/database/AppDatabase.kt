package com.hiksrot.hiksrotzexpensetracker.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hiksrot.hiksrotzexpensetracker.model.entities.*
import com.hiksrot.hiksrotzexpensetracker.model.daos.*
import com.hiksrot.hiksrotzexpensetracker.util.AppDatabaseCallback

@Database(
    entities = [UserEntity::class, BudgetEntity::class, ExpenseEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "expense_db"
            )
                .addCallback(AppDatabaseCallback())
                .build()

        operator fun invoke(context: Context) {
            if (instance == null) {
                synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }
    }
}
