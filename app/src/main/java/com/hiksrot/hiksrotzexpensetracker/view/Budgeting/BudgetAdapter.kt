package com.hiksrot.hiksrotzexpensetracker.view.Budgeting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentBudgetBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import java.text.NumberFormat
import java.util.*

class BudgetAdapter(

    private val onEditClick: (BudgetEntity) -> Unit,
    private val onDeleteClick: (BudgetEntity) -> Unit

) : ListAdapter<BudgetEntity, BudgetAdapter.BudgetViewHolder>(BudgetDiffCallback()) {
    private lateinit var binding:FragmentBudgetBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = FragmentBudgetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BudgetViewHolder(
        private val binding: FragmentBudgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: BudgetEntity) {
            binding.apply {
                .text = budget.name
                textBudgetAmount.text = formatCurrency(budget.amount)
                textBudgetMonth.text = "${getMonthName(budget.month)} ${budget.year}"

                // Set click listeners
                buttonEdit.setOnClickListener {
                    onEditClick(budget)
                }

                // Set card click listener for details
                root.setOnClickListener {
                    onEditClick(budget)
                }
            }
        }
    }

    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount)
    }

    private fun getMonthName(month: Int): String {
        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return if (month in 1..12) monthNames[month - 1] else "Unknown"
    }
}

class BudgetDiffCallback : DiffUtil.ItemCallback<BudgetEntity>() {
    override fun areItemsTheSame(oldItem: BudgetEntity, newItem: BudgetEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BudgetEntity, newItem: BudgetEntity): Boolean {
        return oldItem == newItem
    }
}