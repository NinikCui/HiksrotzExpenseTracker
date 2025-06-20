package com.hiksrot.hiksrotzexpensetracker.model.daos

import androidx.room.*
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE budget_id = :budgetId ORDER BY date DESC")
    fun getExpensesByBudget(budgetId: Int): List<ExpenseEntity>


    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Int): ExpenseEntity

    @Update
    fun updateExpense(expense: ExpenseEntity)

    @Delete
    fun deleteExpense(expense: ExpenseEntity)
}