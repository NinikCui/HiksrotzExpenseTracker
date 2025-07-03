package com.hiksrot.hiksrotzexpensetracker.model.daos

import androidx.room.*
import com.hiksrot.hiksrotzexpensetracker.model.dto.ExpenseItem
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE budget_id = :budgetId ORDER BY date DESC")
    fun getExpensesByBudget(budgetId: Int): List<ExpenseEntity>

    @Query("""
    SELECT e.id,
           e.description,
           e.amount,
           e.date,
           e.latitude,
           e.longitude,
           b.name AS budgetName
    FROM expenses e
    JOIN budgets b
      ON e.budget_id = b.id
    WHERE e.user_id = :userId
    ORDER BY e.date DESC
  """)
    fun getExpensesByUser(userId: Int): List<ExpenseItem>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Int): ExpenseEntity

    @Update
    fun updateExpense(expense: ExpenseEntity)

    @Delete
    fun deleteExpense(expense: ExpenseEntity)


    @Query("SELECT * FROM expenses WHERE budget_id = :budgetId")
    fun getExpensesByBudgetId(budgetId: Int): List<ExpenseEntity>
}