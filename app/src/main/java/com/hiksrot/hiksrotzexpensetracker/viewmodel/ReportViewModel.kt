package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.daos.UserDao
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetItem
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReportViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Dispatchers.IO

    private val _budgetItems = MutableLiveData<List<BudgetItem>>()
    val budgetItems: LiveData<List<BudgetItem>> get() = _budgetItems

    private val _availableMonths = MutableLiveData<List<Int>>()
    val availableMonths: LiveData<List<Int>> get() = _availableMonths

    private val _availableYears = MutableLiveData<List<Int>>()
    val availableYears: LiveData<List<Int>> get() = _availableYears

    fun fetchBudgetItems(userId: Int, month: Int, year: Int) {
        launch {
            val db = buildDb(getApplication())
            val budgets = db.userDao().getBBudgetMonthYear(userId, month, year)

            val result = budgets.map { budget ->
                val expenses = db.expenseDao().getExpensesByBudgetId(budget.id)
                val totalSpent = expenses.sumOf { it.amount }

                BudgetItem(
                    name = budget.name,
                    totalBudget = budget.amount,
                    totalSpent = totalSpent
                )
            }

            _budgetItems.postValue(result)
        }
    }

    fun loadAvailableMonths(userId: Int) {
        launch {
            val months = buildDb(getApplication()).userDao().getMonthUser(userId)
            _availableMonths.postValue(months)
        }
    }

    fun loadAvailableYears(userId: Int) {
        launch {
            val years = buildDb(getApplication()).userDao().getYearUser(userId)
            _availableYears.postValue(years)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
