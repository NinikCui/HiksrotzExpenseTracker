package com.hiksrot.hiksrotzexpensetracker.view.Budgeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.BudgetViewModel
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager

class BudgetFragment : Fragment() {

    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!

    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var budgetAdapter: BudgetAdapter

    private var currentUserId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeComponents()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Load data
        loadBudgets()
    }

    private fun initializeComponents() {
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        currentUserId = sessionManager.getUserId()
    }

    private fun setupRecyclerView() {
        budgetAdapter = BudgetAdapter(
            onEditClick = { budget ->
                showEditBudgetDialog(budget)
            },
            onDeleteClick = { budget ->
                showDeleteConfirmation(budget)
            }
        )

        binding.recyclerViewBudgets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = budgetAdapter
        }
    }

    private fun setupObservers() {
        // Observe budgets list
        budgetViewModel.budgets.observe(viewLifecycleOwner) { budgets ->
            budgetAdapter.submitList(budgets)
            updateEmptyState(budgets.isEmpty())
        }

        // Observe loading state
        budgetViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe operation success
        budgetViewModel.operationSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Operation successful", Toast.LENGTH_SHORT).show()
                budgetViewModel.resetOperationStatus()
            }
        }

        // Observe error messages
        budgetViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                budgetViewModel.resetOperationStatus()
            }
        }
    }

    private fun setupClickListeners() {
        // FAB untuk menambah budget baru
        binding.fabAddBudget.setOnClickListener {
            showAddBudgetDialog()
        }

        // Swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            loadBudgets()
        }
    }

    private fun loadBudgets() {
        budgetViewModel.getBudgetsByUser(currentUserId)
        binding.swipeRefresh.isRefreshing = false
    }

    private fun showAddBudgetDialog() {
        val dialog = AddEditBudgetDialog.newInstance()
        dialog.setOnBudgetSavedListener { name, amount ->
            budgetViewModel.insertBudget(name, amount, currentUserId)
        }
        dialog.show(childFragmentManager, "AddBudgetDialog")
    }

    private fun showEditBudgetDialog(budget: BudgetEntity) {
        val dialog = AddEditBudgetDialog.newInstance(budget)
        dialog.setOnBudgetSavedListener { name, amount ->
            budgetViewModel.updateBudget(budget.id, name, amount, currentUserId)
        }
        dialog.show(childFragmentManager, "EditBudgetDialog")
    }

    private fun showDeleteConfirmation(budget: BudgetEntity) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Budget")
            .setMessage("Are you sure you want to delete '${budget.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                budgetViewModel.deleteBudget(budget, currentUserId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.apply {
            if (isEmpty) {
                emptyStateLayout.visibility = View.VISIBLE
                recyclerViewBudgets.visibility = View.GONE
            } else {
                emptyStateLayout.visibility = View.GONE
                recyclerViewBudgets.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}