package com.hiksrot.hiksrotzexpensetracker.view.Budgeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentNewBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.viewmodel.BudgetViewModel
import java.util.Calendar

class NewBudgetFragment : Fragment() {
    private lateinit var viewModel: BudgetViewModel
    private lateinit var  binding:FragmentNewBudgetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        val userId = SessionManager.getUserId(requireContext())

        binding.buttonAddBudget.setOnClickListener {
            val namaBudget = binding.txtBudget.text.toString().trim()
            val nominalStr = binding.editTxtNominal.text.toString().trim()

            if (namaBudget.isEmpty() || nominalStr.isEmpty()) {
                binding.txtBudget.error = "Harus diisi"
                binding.editTxtNominal.error = "Harus diisi"
                return@setOnClickListener
            }

            val nominalBudget = nominalStr.toDoubleOrNull()
            if (nominalBudget == null) {
                binding.editTxtNominal.error = "Nominal tidak valid"
                return@setOnClickListener
            }

            val newBudget = com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity(
                name = namaBudget,
                amount = nominalBudget,
                userId = userId,
                month = currentMonth,
                year = currentYear
            )

            viewModel.insertBudget(newBudget)
        }

        // Observe LiveData jika berhasil
        viewModel.isBudgetSaved.observe(viewLifecycleOwner) {
            if (it == true) {
                // kembali ke fragment sebelumnya atau tampilkan pesan sukses
                Toast.makeText(requireContext(), "Budget berhasil disimpan", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


    }
}