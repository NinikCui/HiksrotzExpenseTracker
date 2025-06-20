package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hiksrot.hiksrotzexpensetracker.model.database.AppDatabase
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    val budgetsLD = MutableLiveData<List<BudgetEntity>>()
    val expensesLD = MutableLiveData<List<ExpenseEntity>>()
    val loadingLD = MutableLiveData<Boolean>()
    val errorLD = MutableLiveData<Boolean>()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun fetch(userId: Int) {
        loadingLD.postValue(true)
        errorLD.postValue(false)

        launch {
            val db = AppDatabase.buildDatabase(getApplication())
            budgetsLD.postValue(db.budgetDao().getBudgetsByUser(userId))
            expensesLD.postValue(db.expenseDao().getAllExpenses())
            loadingLD.postValue(false)
        }
    }



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
