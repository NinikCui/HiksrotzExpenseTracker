package com.hiksrot.hiksrotzexpensetracker.model.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
        @Query("SELECT * FROM budgets")
        fun getAllBudgets(): Flow<List<BudgetEntity>>

        @Insert
        suspend fun insertBudget(budget: BudgetEntity)

        @Update
        suspend fun updateBudget(budget: BudgetEntity)

        @Delete
        suspend fun deleteBudget(budget: BudgetEntity)
}