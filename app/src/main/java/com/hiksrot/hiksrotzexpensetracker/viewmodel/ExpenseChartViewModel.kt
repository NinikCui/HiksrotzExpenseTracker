package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetReport
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExpenseChartViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Dispatchers.IO

    private val _budgetReports = MutableLiveData<List<BudgetReport>>()
    val budgetReports: LiveData<List<BudgetReport>> get() = _budgetReports

    fun fetchBudgets(userId: Int, month: Int, year: Int) {
        launch {
            val db = buildDb(getApplication())
            val data = db.budgetDao().getBudgetReportForMonth(userId, month, year)
            _budgetReports.postValue(data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
