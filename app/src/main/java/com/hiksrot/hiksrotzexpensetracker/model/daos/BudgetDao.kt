package com.hiksrot.hiksrotzexpensetracker.model.daos

import androidx.room.*
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetReport

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

        @Query("""
            SELECT b.id, b.name, b.amount AS totalBudget,
                COUNT(e.id) AS transactionCount,
                IFNULL(SUM(e.amount), 0) AS totalSpent
            FROM budgets b
            LEFT JOIN expenses e ON b.id = e.budget_id
                AND b.month = :month
                AND b.year = :year
            WHERE b.user_id = :userId
              AND b.month = :month
              AND b.year = :year
            GROUP BY b.id
        """)
        fun getBudgetReportForMonth(userId: Int, month: Int, year: Int): List<BudgetReport>

        @Query("SELECT * FROM budgets WHERE user_id = :userId AND month = :month AND year = :year")
        fun getBudgetsByUserAndMonth(userId: Int, month: Int, year: Int): List<BudgetEntity>

}