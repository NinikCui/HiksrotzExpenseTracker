package com.hiksrot.hiksrotzexpensetracker.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityMainBinding
import com.hiksrot.hiksrotzexpensetracker.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.fetch(userId = 2)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.budgetsLD.observe(this) { budgets ->
            viewModel.expensesLD.observe(this) { expenses ->

                val budgetMap = budgets.groupBy { it.userId }
                val expenseMap = expenses.groupBy { expense ->
                    val budget = budgets.find { it.id == expense.budgetId }
                    budget?.userId ?: -1
                }

                val output = StringBuilder()

                val userNames = mapOf(
                    1 to "Ariel",
                    2 to "Nico",
                    3 to "Nadya"
                )

                for (userId in userNames.keys) {
                    output.append("\nUser: ${userNames[userId]}\n")

                    val userBudgets = budgetMap[userId] ?: emptyList()
                    output.append("  Budgets:\n")
                    userBudgets.forEach {
                        output.append("    - ${it.name} : ${it.amount}\n")
                    }

                    val userExpenses = expenseMap[userId] ?: emptyList()
                    output.append("  Expenses:\n")
                    userExpenses.forEach { e ->
                        val budgetName = budgets.find { it.id == e.budgetId }?.name ?: "-"
                        output.append("    - ${e.description} (${budgetName}) : ${e.amount}\n")
                    }
                }

                binding.txtBudgets.text = output.toString()
            }
        }
    }
}
