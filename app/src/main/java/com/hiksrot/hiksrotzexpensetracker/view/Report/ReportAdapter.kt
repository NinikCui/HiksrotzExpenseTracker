package com.hiksrot.hiksrotzexpensetracker.view.Report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiksrot.hiksrotzexpensetracker.databinding.ReportCardLayoutBinding
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetItem

class ReportAdapter(
    private var budgetItems: MutableList<BudgetItem>
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(val binding: ReportCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ReportCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int = budgetItems.size

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val item = budgetItems[position]
        holder.binding.apply {
            textCategory.text = item.name
            textSpent.text = "IDR %,d".format(item.totalSpent.toInt())
            textBudgetTotal.text = "IDR %,d".format(item.totalBudget.toInt())
            textBudgetLeft.text = "Budget left: IDR %,d".format(item.budgetLeft.toInt())
            progressBudget.progress = item.progressPercent
        }
    }

    fun setData(newList: List<BudgetItem>) {
        budgetItems.clear()
        budgetItems.addAll(newList)
        notifyDataSetChanged()
    }
}
