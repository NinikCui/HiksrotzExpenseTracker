package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiksrot.hiksrotzexpensetracker.databinding.ExpenseItemBinding
import com.hiksrot.hiksrotzexpensetracker.model.dto.ExpenseItem

class ExpenseAdapter(
    private val expenseList: MutableList<ExpenseItem>,
    private val listener: ExpenseClickListener
) : RecyclerView.Adapter<ExpenseAdapter.VH>() {

    inner class VH(val binding: ExpenseItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) = VH(
        ExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            this.listener = listener
        }
    )

    override fun onBindViewHolder(holder: VH, pos: Int) {
        holder.binding.expense = expenseList[pos]
        holder.binding.listener = listener
    }

    override fun getItemCount() = expenseList.size

    fun updateList(newList: List<ExpenseItem>) {
        expenseList.clear()
        expenseList.addAll(newList)
        notifyDataSetChanged()
    }
}

