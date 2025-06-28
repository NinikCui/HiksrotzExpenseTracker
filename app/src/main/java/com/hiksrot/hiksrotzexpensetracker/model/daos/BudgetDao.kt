package com.hiksrot.hiksrotzexpensetracker.model.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertBudget(budget: BudgetEntity)

        @Query("SELECT * FROM budgets WHERE user_id = :userId ORDER BY id DESC")
        fun getBudgetsByUser(userId: Int): List<BudgetEntity>

        @Query("SELECT * FROM budgets WHERE id = :id")
        fun getBudgetById(id: Int): BudgetEntity

        @Query("UPDATE budgets SET name = :name, amount = :amount WHERE id = :id")
        fun updateBudget(id: Int, name: String, amount: Double)

        @Delete
        fun deleteBudget(budget: BudgetEntity)
}