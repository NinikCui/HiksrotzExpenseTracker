package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiksrot.hiksrotzexpensetracker.databinding.BudgetItemBinding
import com.hiksrot.hiksrotzexpensetracker.model.dto.BudgetReport
import com.hiksrot.hiksrotzexpensetracker.util.helper

class BudgetReportAdapter(
    private val budgetList: MutableList<BudgetReport>,
    private val colors: List<Int>
) : RecyclerView.Adapter<BudgetReportAdapter.VH>() {

    inner class VH(val binding: BudgetItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) = VH(
        BudgetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, pos: Int) {
        val data = budgetList[pos]
        holder.binding.apply {
            tvInitial.text = data.name.firstOrNull()?.toString()?.uppercase() ?: "-"

            val color = colors[pos % colors.size]
            val bgCircle = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(color)
            }
            tvInitial.background = bgCircle

            tvBudgetName.text = data.name
            tvSubtitle.text = "${data.transactionCount} transactions · ${ helper.formatRupiah(data.totalSpent)}"
            tvPercentage.text = "${100 - data.percentLeft}%"
            progressBar.progress = 100 - data.percentLeft
        }
    }

    override fun getItemCount() = budgetList.size

    fun updateList(newList: List<BudgetReport>) {
        budgetList.clear()
        budgetList.addAll(newList)
        notifyDataSetChanged()
    }
}
