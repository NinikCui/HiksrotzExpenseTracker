package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetItem
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BudgetViewModel (application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    private val _budgetItems = MutableLiveData<List<BudgetEntity>>()
    val budgetItems: LiveData<List<BudgetEntity>> get() = _budgetItems

    val isBudgetSaved = MutableLiveData<Boolean>()
    fun getBudgetData(userId: Int, month: Int, year: Int) {
        launch {
            val db = buildDb(getApplication())
            val budgets = db.budgetDao().getBudgetsByUserAndMonth(userId, month, year)

            _budgetItems.postValue(budgets)

        }
    }

    fun insertBudget(budget: BudgetEntity) {
        launch {
            val db = buildDb(getApplication())
            db.budgetDao().insertBudget(budget)
            isBudgetSaved.postValue(true)

        }
    }

    fun updateBudget(id: Int, name: String, amount: Double) {
        launch {
            val db = buildDb(getApplication())
            db.budgetDao().updateBudget(id, name, amount)
            isBudgetSaved.postValue(true)
        }
    }


    val totalExpense = MutableLiveData<Double>()

    fun getTotalExpense(budgetId: Int) {
        launch {
            val db = buildDb(getApplication())
            val total = db.expenseDao().getTotalExpenseForBudget(budgetId)
            totalExpense.postValue(total)
        }
    }

    val isNameDuplicate = MutableLiveData<Boolean>()

    fun checkDuplicateName(budgetId: Int, userId: Int, month: Int, year: Int, name: String) {
        launch {
            val db = buildDb(getApplication())
            val count = db.budgetDao().isBudgetNameDuplicate(budgetId, userId, month, year, name)
            isNameDuplicate.postValue(count > 0)
        }
    }

    val isNewNameDuplicate = MutableLiveData<Boolean>()

    fun checkNewBudgetName(userId: Int, month: Int, year: Int, name: String) {
        launch {
            val db = buildDb(getApplication())
            val count = db.budgetDao().isNewBudgetNameExists(userId, month, year, name)
            isNewNameDuplicate.postValue(count > 0)
        }
    }

}