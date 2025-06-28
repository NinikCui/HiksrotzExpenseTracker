package com.hiksrot.hiksrotzexpensetracker.view.Report

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentReportBinding
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetItem
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ReportViewModel

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: ReportViewModel
    private lateinit var userModel: LoginRegisterViewModel
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        userModel = ViewModelProvider(requireActivity())[LoginRegisterViewModel::class.java]

        setupRecyclerView()

        userModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                viewModel.fetchBudgetItems(user.id)
            } else {
                Toast.makeText(requireContext(), "User belum login", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.budgetItems.observe(viewLifecycleOwner) { budgetItems ->
            reportAdapter.setData(budgetItems)
        }

        viewModel.budgetItems.observe(viewLifecycleOwner) { budgetItemList ->
            reportAdapter.setData(budgetItemList)

            val totalSpent = budgetItemList.sumOf { it.totalSpent }
            val totalBudget = budgetItemList.sumOf { it.totalBudget }

            val summaryText = "IDR %,d / IDR %,d".format(totalSpent.toInt(), totalBudget.toInt())
            binding.txtReportAkhir.text = summaryText
        }
    }

    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter(mutableListOf<BudgetItem>())
        binding.recGambar.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reportAdapter
        }
    }
}
