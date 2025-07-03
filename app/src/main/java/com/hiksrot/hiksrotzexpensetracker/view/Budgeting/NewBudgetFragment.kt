package com.hiksrot.hiksrotzexpensetracker.view.Budgeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentNewBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.viewmodel.BudgetViewModel
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class NewBudgetFragment : Fragment() {
    private lateinit var viewModel: BudgetViewModel
    private lateinit var  binding:FragmentNewBudgetBinding
    private val args: NewBudgetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun formatToRupiah(amount: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0 // tanpa koma/desimal
        return numberFormat.format(amount) // hasil: Rp12.334
            .replace("Rp", "IDR ")
            .trim()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        val judul = args.judul
        val budgetId = args.budgetId
        val namaBudget = args.namaBudget
        val nominalBudget = args.nominalBudget
        val userId = SessionManager.getUserId(requireContext())
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        // UI & Pre-fill
        binding.titleNewBudget.text = judul
        var totalUsedExpense = 0.0

        if (judul == "Edit Budget") {
            binding.txtBudget.setText(namaBudget)
            binding.editTxtNominal.setText(formatToRupiah(nominalBudget.toDouble()))
            binding.buttonAddBudget.setText("Edit")
            viewModel.getTotalExpense(budgetId)
            viewModel.totalExpense.observe(viewLifecycleOwner) {
                totalUsedExpense = it
            }
        }



        binding.buttonAddBudget.setOnClickListener {
            val nama = binding.txtBudget.text.toString().trim()
            val nominalStr = binding.editTxtNominal.text.toString().trim()

            if (nama.isEmpty()) {
                binding.txtBudget.error = "Nama budget harus diisi"
                return@setOnClickListener
            }

            val nominal = nominalStr.toDoubleOrNull()
            if (nominal == null || nominal <= 0) {
                binding.editTxtNominal.error = "Nominal harus positif"
                return@setOnClickListener
            }

            if (judul == "Edit Budget") {
                if (nominal < totalUsedExpense) {
                    binding.editTxtNominal.error = "Nominal tidak boleh < total pengeluaran: IDR %,d".format(totalUsedExpense.toInt())
                    return@setOnClickListener
                }
                viewModel.checkDuplicateName(budgetId, userId, currentMonth, currentYear, nama)
                viewModel.isNameDuplicate.observe(viewLifecycleOwner) { isDuplicate ->
                    if (isDuplicate) {
                        binding.txtBudget.error = "Nama budget sudah digunakan di periode ini"
                    } else {
                        viewModel.updateBudget(budgetId, nama, nominal)
                    }
                }
            } else {
                viewModel.checkNewBudgetName(userId, currentMonth, currentYear, nama)
                viewModel.isNewNameDuplicate.observe(viewLifecycleOwner) { isDuplicate ->
                    if (isDuplicate) {
                        binding.txtBudget.error = "Nama budget sudah digunakan di periode ini"
                    } else {
                        val newBudget = BudgetEntity(
                            name = nama,
                            amount = nominal,
                            userId = userId,
                            month = currentMonth,
                            year = currentYear
                        )
                        viewModel.insertBudget(newBudget)
                    }
                }

            }
        }


        viewModel.isBudgetSaved.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireContext(), "Budget berhasil disimpan", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


    }
}