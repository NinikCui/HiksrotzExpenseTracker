package com.hiksrot.hiksrotzexpensetracker.view.Report

import android.R
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

    private var selectedMonth = 1
    private var selectedYear = 2025
    private lateinit var monthList: List<Int>
    fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "Tidak diketahui"
        }
    }

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

        //buat ngambil data bulan sama thun e user
        userModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewModel.loadAvailableMonths(it.id)
                viewModel.loadAvailableYears(it.id)
                observeSpinnersAndFetch(it.id)
            } ?: Toast.makeText(requireContext(), "User belum login", Toast.LENGTH_SHORT).show()
        }


        //buat ngliat totalannya
        viewModel.budgetItems.observe(viewLifecycleOwner) { list ->
            reportAdapter.setData(list)

            val totalSpent = list.sumOf { it.totalSpent }
            val totalBudget = list.sumOf { it.totalBudget }

            binding.txtReportAkhir.text = "IDR %,d / IDR %,d".format(
                totalSpent.toInt(), totalBudget.toInt()
            )
        }
    }

    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter(mutableListOf())
        binding.recGambar.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reportAdapter
        }
    }

    private fun observeSpinnersAndFetch(userId: Int) {
        //memantau available month, nanti dimasukin ke adapter, terus masukin ke spinner, kalau tahun e sudah ada ya langsung ambil data
        viewModel.availableMonths.observe(viewLifecycleOwner) { months ->
            val monthNames = months.map { getMonthName(it) }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, monthNames)
            binding.spinnerMonth.adapter = adapter

            selectedMonth = months.maxOrNull() ?: 1
            binding.spinnerMonth.setSelection(months.indexOf(selectedMonth))

            viewModel.availableYears.value?.let {
                viewModel.fetchBudgetItems(userId, selectedMonth, selectedYear)
            }

            monthList = months
        }

        //memantau available tahun, nanti dimasukin ke adapter, terus masukin ke spinner, kalau bulan e sudah ada ya langsung ambil data
        viewModel.availableYears.observe(viewLifecycleOwner) { years ->
            val yearAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, years)
            binding.spinnerYear.adapter = yearAdapter

            selectedYear = years.maxOrNull() ?: 2025
            binding.spinnerYear.setSelection(years.indexOf(selectedYear))

            viewModel.availableMonths.value?.let {
                viewModel.fetchBudgetItems(userId, selectedMonth, selectedYear)
            }
        }


        //ini kalau spinner month dipilih
        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                selectedMonth = monthList[position]
                viewModel.fetchBudgetItems(userId, selectedMonth, selectedYear)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //ini kalau spinner tahun dipilihi
        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selectedYear = parent.getItemAtPosition(pos) as Int
                viewModel.fetchBudgetItems(userId, selectedMonth, selectedYear)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
