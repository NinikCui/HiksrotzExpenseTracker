package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentNewExpenseBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.viewmodel.NewExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewExpenseFragment : Fragment(), NewExpenseListener {

    private lateinit var binding: FragmentNewExpenseBinding

    private lateinit var vm: NewExpenseViewModel
    private var budgetList: List<BudgetEntity> = emptyList()

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        binding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(NewExpenseViewModel::class.java)

        binding.vm = vm
        binding.listener = this
        binding.lifecycleOwner = viewLifecycleOwner

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id"))
        binding.textDate.text = sdf.format(Date())

        val userId = SessionManager.getUserId(requireContext())
        if (userId != -1) {
            vm.loadBudgets(userId)
        }

        observeViewModel()

        binding.buttonAddExpense.setOnClickListener {
            vm.saveExpense(
                onSuccess = {
                    Toast.makeText(requireContext(), "Pengeluaran tersimpan!", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                },
                onError = { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun observeViewModel() {
        // 1. Observe budgets → isi spinner
        vm.budgets.observe(viewLifecycleOwner) { list ->
            budgetList = list
            val labels = list.map { it.name }
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, labels).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = adapter
            }
        }

        // 2. Setup listener untuk item spinner
        binding.spinnerCategory.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>,
                    view: android.view.View?,
                    pos: Int,
                    id: Long
                ) {
                    val bud = budgetList[pos]
                    vm.selectedBudget.value = bud
                    vm.loadBudgetUsage(bud.id)
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
            }

        // 3. Observer lainnya → update hint dan progress
        fun updateUI() {
            val left = vm.getRemainingBudget().toInt()
            val pct  = vm.getProgressPercent()
            binding.editTextNominal.hint = "Nominal (IDR $left left)"
            binding.progressBarBudget.progress = pct
        }

        vm.nominal.observe(viewLifecycleOwner) {
            updateUI()
        }
        vm.budgetUsed.observe(viewLifecycleOwner) {
            updateUI()
        }
        vm.selectedBudget.observe(viewLifecycleOwner) {
            updateUI()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onAddExpenseClick(v: View){
        vm.saveExpense(
            onSuccess = {
                Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            },
            onError = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
