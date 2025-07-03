package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiksrot.hiksrotzexpensetracker.R

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentExpenseChartBinding
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetReport
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.util.helper
import com.hiksrot.hiksrotzexpensetracker.util.helper.formatRupiah
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ExpenseChartViewModel

class ExpenseChartFragment : Fragment() {

    private lateinit var binding: FragmentExpenseChartBinding
    private lateinit var viewModel: ExpenseChartViewModel
    private lateinit var adapter: BudgetReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val chartColors = listOf(
        Color.parseColor("#A694FE"),
        Color.parseColor("#FF89A4"),
        Color.parseColor("#6B47FF"),
        Color.parseColor("#34D374"),
        Color.parseColor("#FFD600"),
        Color.parseColor("#18B0FF")
    )

    private var currentMonth: Int = 1
    private var currentYear: Int = 2025

    private val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Adapter & RecyclerView
        adapter = BudgetReportAdapter(mutableListOf(), chartColors)
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter

        // Setup ViewModel
        viewModel = ViewModelProvider(this).get(ExpenseChartViewModel::class.java)

        // Observe LiveData untuk update RecyclerView dan PieChart
        viewModel.budgetReports.observe(viewLifecycleOwner) { data ->
            Log.d("DEBUG_ADAPTER", "Item count: ${data.size}")
            adapter.updateList(data)
            setupPieChartData(data)
            binding.tvBudgets.visibility = if (data.isEmpty()) View.GONE else View.VISIBLE
        }

        // Ambil userId, month, year (default: hari ini)
        val userId = SessionManager.getUserId(requireContext())
        val calendar = java.util.Calendar.getInstance()
        currentMonth = calendar.get(java.util.Calendar.MONTH) + 1
        currentYear = calendar.get(java.util.Calendar.YEAR)

        updateMonthYearUI()
        fetchBudgetForCurrentMonth()

        if (userId != -1) {
            viewModel.fetchBudgets(userId, currentMonth, currentYear)
        }
        binding.btnPrev.setOnClickListener { moveToPreviousMonth() }
        binding.btnNext.setOnClickListener { moveToNextMonth() }

        binding.btnViewAllExpense.setOnClickListener {
            val action = ExpenseChartFragmentDirections.actionToListExpense()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun moveToPreviousMonth() {
        if (currentMonth == 1) {
            currentMonth = 12
            currentYear--
        } else {
            currentMonth--
        }
        updateMonthYearUI()
        fetchBudgetForCurrentMonth()
    }

    private fun moveToNextMonth() {
        if (currentMonth == 12) {
            currentMonth = 1
            currentYear++
        } else {
            currentMonth++
        }
        updateMonthYearUI()
        fetchBudgetForCurrentMonth()
    }

    private fun updateMonthYearUI() {
        binding.tvMonth.text = monthNames[currentMonth - 1]
        binding.tvYear.text = "$currentYear"
    }

    private fun fetchBudgetForCurrentMonth() {
        val userId = SessionManager.getUserId(requireContext())
        if (userId != -1) {
            viewModel.fetchBudgets(userId, currentMonth, currentYear)
        }
    }

    private fun setupPieChartData(data: List<BudgetReport>) {
        val pieChart = binding.pieChart
        val tvAmount = binding.tvAmount
        val tvNoData = binding.tvNoData

        val monthText = monthNames[currentMonth - 1]

        if (data.isEmpty()) {
            pieChart.clear()
            tvAmount.text = "IDR 0"
            tvNoData.text = "No data in $monthText $currentYear"
            tvNoData.visibility = View.VISIBLE
            return
        } else {
            tvNoData.visibility = View.GONE
        }

        val entries = data.map { PieEntry(it.totalSpent.toFloat(), it.name) }
        val dataSet = PieDataSet(entries, "").apply {
            setColors(chartColors)
            valueTextColor = Color.WHITE
            valueTextSize = 14f
            sliceSpace = 2f
            yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
            xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
        }

        val pieData = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(pieChart))
        }

        val totalSpent = data.sumOf { it.totalSpent }
        tvAmount.text = formatRupiah(totalSpent)

        pieChart.apply {
            this.data = pieData
            setUsePercentValues(true)
            holeRadius = 60f
            transparentCircleRadius = 65f
            legend.isEnabled = false
            description.isEnabled = false
            setDrawEntryLabels(false)
            setDrawCenterText(true)
            centerText = "Your spent\n${helper.formatRupiah(totalSpent)}"
            setCenterTextSize(16f)
            setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            animateY(800, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
            invalidate()
        }
    }
}
