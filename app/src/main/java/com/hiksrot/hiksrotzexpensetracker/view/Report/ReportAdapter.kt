package com.hiksrot.hiksrotzexpensetracker.view.Report

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiksrot.hiksrotzexpensetracker.databinding.ReportCardLayoutBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity

class ReportAdapter(val budget:List<BudgetEntity>, val expense:List<ExpenseEntity>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>(){
    class ReportViewHolder(val binding: ReportCardLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ReportCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return ReportViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return expense.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {

    }


}