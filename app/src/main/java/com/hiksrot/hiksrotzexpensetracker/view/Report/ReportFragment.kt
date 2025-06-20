package com.hiksrot.hiksrotzexpensetracker.view.Report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentLoginBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentRegisterBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentReportBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ReportViewModel


class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: ReportViewModel
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(
            inflater,
            container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(ReportViewModel::class.java)

      /*  viewModel.getBudgetItems(user.id).observe(viewLifecycleOwner) { budgetItems ->
            reportAdapter.setData(budgetItems)
        }
        setupRecyclerView()

    }
    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter()
        binding.recGambar.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = reportAdapter
        }*/
    }
}