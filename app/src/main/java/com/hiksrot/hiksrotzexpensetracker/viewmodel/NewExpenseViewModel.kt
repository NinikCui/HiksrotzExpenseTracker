package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.*
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class NewExpenseViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val budgets = MutableLiveData<List<BudgetEntity>>()
    val selectedBudget = MutableLiveData<BudgetEntity>()
    val budgetUsed = MutableLiveData<Double>()
    val nominal = MutableLiveData<String>()
    val note = MutableLiveData<String>()

//    fun loadBudgets(userId: Int) {
//        launch {
//            val list = buildDb(getApplication()).budgetDao().getBudgetsByUser(userId)
//            budgets.postValue(list)
//        }
//    }

    fun loadBudgetUsage(budgetId: Int) {
        launch {
            val list = buildDb(getApplication()).expenseDao().getExpensesByBudget(budgetId)
            budgetUsed.postValue(list.sumOf { it.amount })
        }
    }

    fun loadBudgetsForCurrentMonth(userId: Int) {
        launch {
            val calendar = Calendar.getInstance()
            val thisMonth = calendar.get(Calendar.MONTH) + 1
            val thisYear = calendar.get(Calendar.YEAR)
            val list = buildDb(getApplication()).budgetDao()
                .getBudgetsByUserAndMonth(userId, thisMonth, thisYear)
            budgets.postValue(list)
        }
    }

    /** sisa budget = totalBudget − totalSpent − input */
    fun getRemainingBudget(): Double {
        val total = selectedBudget.value?.amount ?: 0.0
        val spent = budgetUsed.value ?: 0.0
        val input = nominal.value?.toDoubleOrNull() ?: 0.0
        return (total - spent - input).coerceAtLeast(0.0)
    }

    /** progress (%) = (spent + input) / totalBudget */
    fun getProgressPercent(): Int {
        val total = selectedBudget.value?.amount ?: 1.0
        val spent = budgetUsed.value ?: 0.0
        val input = nominal.value?.toDoubleOrNull() ?: 0.0
        return (((spent + input) / total) * 100).toInt().coerceIn(0, 100)
    }

    fun saveExpense(latitude: Double? = null,
                    longitude: Double? = null,
                    onSuccess: () -> Unit,
                    onError: (String) -> Unit) {
        val amt = nominal.value?.toDoubleOrNull()
        val bud = selectedBudget.value

        if (bud == null) {
            onError("Pilih budget dulu.")
            return
        }
        if (amt == null || amt <= 0.0) {
            onError("Masukkan nominal valid.")
            return
        }
        if ((budgetUsed.value ?: 0.0) + amt > bud.amount) {
            onError("Nominal melebihi budget.")
            return
        }

        val exp = ExpenseEntity(
            description = note.value.orEmpty(),
            amount = amt,
            date = System.currentTimeMillis(),
            budgetId = bud.id,
            userId = bud.userId,
            latitude = latitude,
            longitude = longitude
        )
        launch {
            buildDb(getApplication()).expenseDao().insertExpense(exp)
            withContext(Dispatchers.Main) { onSuccess() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
