package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.dto.ExpenseItem
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ExpenseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val expensesLD = MutableLiveData<List<ExpenseItem>>()

    fun fetchExpenses(userId: Int) {
        launch {
            val list = buildDb(getApplication()).expenseDao().getExpensesByUser(userId)
            expensesLD.postValue(list)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}