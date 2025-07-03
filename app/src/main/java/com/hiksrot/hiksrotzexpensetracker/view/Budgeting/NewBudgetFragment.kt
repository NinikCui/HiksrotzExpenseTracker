package com.hiksrot.hiksrotzexpensetracker.view.Budgeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentNewBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.BudgetViewModel
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import java.util.*

class NewBudgetFragment : Fragment() {

    private var _binding: FragmentNewBudgetBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BudgetViewModel
    private lateinit var sessionManager: SessionManager
    private var budgetId: Int = -1
    private var isEditMode = false

    companion object {
        private const val ARG_BUDGET_ID = "budget_id"

        fun newInstance(budgetId: Int = -1): NewBudgetFragment {
            val fragment = NewBudgetFragment()
            val args = Bundle()
            args.putInt(ARG_BUDGET_ID, budgetId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        // Check if this is edit mode
        arguments?.let {
            budgetId = it.getInt(ARG_BUDGET_ID, -1)
            isEditMode = budgetId != -1
        }

        setupUI()
        setupObservers()
        setupClickListeners()

        if (isEditMode) {
            viewModel.getBudgetById(budgetId)
        }
    }

    private fun setupUI() {
        binding.apply {
            if (isEditMode) {
                textViewTitle.text = "Edit Budget"
                buttonSaveBudget.text = "UPDATE BUDGET"
            } else {
                textViewTitle.text = "New Budget"
                buttonSaveBudget.text = "ADD BUDGET"
            }
        }
    }

    private fun setupObservers() {
        viewModel.budgetEntity.observe(viewLifecycleOwner) { budget ->
            budget?.let {
                binding.editTextBudgetName.setText(it.name)
                binding.editTextBudgetAmount.setText(it.amount.toString())
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.success.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.buttonSaveBudget.isEnabled = !isLoading
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.buttonSaveBudget.setOnClickListener {
            saveBudget()
        }

        binding.buttonCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun saveBudget() {
        val name = binding.editTextBudgetName.text.toString().trim()
        val amountText = binding.editTextBudgetAmount.text.toString().trim()

        if (name.isEmpty()) {
            binding.editTextBudgetName.error = "Nama budget harus diisi"
            return
        }

        if (amountText.isEmpty()) {
            binding.editTextBudgetAmount.error = "Nominal budget harus diisi"
            return
        }

        val amount = try {
            amountText.toDouble()
        } catch (e: NumberFormatException) {
            binding.editTextBudgetAmount.error = "Nominal tidak valid"
            return
        }

        if (amount <= 0) {
            binding.editTextBudgetAmount.error = "Nominal tidak boleh negatif atau nol"
            return
        }

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val currentUser = sessionManager.getCurrentUser()

        if (isEditMode) {
            viewModel.updateBudget(budgetId, name, amount)
        } else {
            val budget = BudgetEntity(
                name = name,
                amount = amount,
                userId = currentUser.id,
                month = currentMonth,
                year = currentYear
            )
            viewModel.insertBudget(budget)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}