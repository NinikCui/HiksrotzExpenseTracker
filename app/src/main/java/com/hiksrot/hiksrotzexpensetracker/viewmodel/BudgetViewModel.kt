package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetReport
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.*
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class BudgetViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    // LiveData untuk daftar budget
    private val _budgets = MutableLiveData<List<BudgetEntity>>()
    val budgets: LiveData<List<BudgetEntity>> get() = _budgets

    // LiveData untuk budget report
    private val _budgetReports = MutableLiveData<List<BudgetReport>>()
    val budgetReports: LiveData<List<BudgetReport>> get() = _budgetReports

    // LiveData untuk status operasi
    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> get() = _operationSuccess

    // LiveData untuk error message
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // LiveData untuk loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData untuk single budget
    private val _selectedBudget = MutableLiveData<BudgetEntity?>()
    val selectedBudget: LiveData<BudgetEntity?> get() = _selectedBudget

//    tambah budget
    fun insertBudget(name: String, amount: Double, userId: Int) {
        launch {
            try {
                _isLoading.postValue(true)

                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH) + 1
                val currentYear = calendar.get(Calendar.YEAR)

                val budget = BudgetEntity(
                    name = name,
                    amount = amount,
                    userId = userId,
                    month = currentMonth,
                    year = currentYear
                )

                val db = buildDb(getApplication())
                db.budgetDao().insertBudget(budget)

                _operationSuccess.postValue(true)
                // Refresh data setelah insert
                getBudgetsByUser(userId)

            } catch (e: Exception) {
                _errorMessage.postValue("Error adding budget: ${e.message}")
                _operationSuccess.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getBudgetsByUser(userId: Int) {
        launch {
            try {
                _isLoading.postValue(true)
                val db = buildDb(getApplication())
                val budgetList = db.budgetDao().getBudgetsByUser(userId)
                _budgets.postValue(budgetList)
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching budgets: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getBudgetById(id: Int) {
        launch {
            try {
                _isLoading.postValue(true)
                val db = buildDb(getApplication())
                val budget = db.budgetDao().getBudgetById(id)
                _selectedBudget.postValue(budget)
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching budget: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateBudget(id: Int, name: String, amount: Double, userId: Int) {
        launch {
            try {
                _isLoading.postValue(true)
                val db = buildDb(getApplication())
                db.budgetDao().updateBudget(id, name, amount)

                _operationSuccess.postValue(true)
                // Refresh data setelah update
                getBudgetsByUser(userId)

            } catch (e: Exception) {
                _errorMessage.postValue("Error updating budget: ${e.message}")
                _operationSuccess.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun deleteBudget(budget: BudgetEntity, userId: Int) {
        launch {
            try {
                _isLoading.postValue(true)
                val db = buildDb(getApplication())
                db.budgetDao().deleteBudget(budget)

                _operationSuccess.postValue(true)
                // Refresh data setelah delete
                getBudgetsByUser(userId)

            } catch (e: Exception) {
                _errorMessage.postValue("Error deleting budget: ${e.message}")
                _operationSuccess.postValue(false)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getBudgetReportForMonth(userId: Int, month: Int? = null, year: Int? = null) {
        launch {
            try {
                _isLoading.postValue(true)

                val calendar = Calendar.getInstance()
                val targetMonth = month ?: (calendar.get(Calendar.MONTH) + 1)
                val targetYear = year ?: calendar.get(Calendar.YEAR)

                val db = buildDb(getApplication())
                val reports = db.budgetDao().getBudgetReportForMonth(userId, targetMonth, targetYear)
                _budgetReports.postValue(reports)

            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching budget reports: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getCurrentMonthBudgetReport(userId: Int) {
        getBudgetReportForMonth(userId)
    }

    fun validateBudgetInput(name: String, amount: String): Pair<Boolean, String> {
        return when {
            name.isBlank() -> Pair(false, "Budget name cannot be empty")
            amount.isBlank() -> Pair(false, "Budget amount cannot be empty")
            else -> {
                try {
                    val amountValue = amount.toDouble()
                    if (amountValue <= 0) {
                        Pair(false, "Budget amount must be greater than 0")
                    } else {
                        Pair(true, "")
                    }
                } catch (e: NumberFormatException) {
                    Pair(false, "Please enter a valid amount")
                }
            }
        }
    }

    fun resetOperationStatus() {
        _operationSuccess.value = false
        _errorMessage.value = ""
    }

    fun clearSelectedBudget() {
        _selectedBudget.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}