package com.hiksrot.hiksrotzexpensetracker.view.Budgeting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentLoginBinding
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.viewmodel.BudgetViewModel
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ReportViewModel
import java.util.Calendar


class BudgetFragment : Fragment() {
    private lateinit var  binding:FragmentBudgetBinding
    private lateinit var viewModel: BudgetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        // SETUP RecyclerView
        val recyclerView = binding.recyclerBudget
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Ambil bulan dan tahun saat ini
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        // Ambil userId (misal user login sudah diketahui)
        val userId = SessionManager.getUserId(requireContext())

        // Panggil ViewModel
        viewModel.getBudgetData(userId, currentMonth, currentYear)

        // Observe LiveData
        viewModel.budgetItems.observe(viewLifecycleOwner) { budgets ->
            recyclerView.adapter = BudgetItemAdapter(budgets)
        }

        // Tambahkan klik FAB jika diperlukan
        binding.fabAddBudget.setOnClickListener {
            findNavController().navigate(R.id.actionNewBudget)
        }
    }

}