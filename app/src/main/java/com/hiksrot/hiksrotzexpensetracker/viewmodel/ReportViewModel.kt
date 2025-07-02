package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetItem
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReportViewModel(application: Application): AndroidViewModel(application),
    CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val _budgetItems = MutableLiveData<List<BudgetItem>>()
    val budgetItems: LiveData<List<BudgetItem>> get() = _budgetItems

    fun fetchBudgetItems(userId: Int) {
        launch {
            val db = buildDb(getApplication())
            val budgets = db.budgetDao().getBudgetsByUser(userId)
            val expenses = db.expenseDao().getAllExpenses()

            val combined = budgets.map { budget ->
                val totalSpent = expenses
                    .filter { it.budgetId == budget.id }
                    .sumOf { it.amount }

                BudgetItem(
                    name = budget.name,
                    totalBudget = budget.amount,
                    totalSpent = totalSpent
                )
            }

            _budgetItems.postValue(combined)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}