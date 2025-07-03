package com.hiksrot.hiksrotzexpensetracker.view.Budgeting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.BudgetListLayoutBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity

class BudgetItemAdapter(
    private val budgets: List<BudgetEntity>,
    private val onEditClick: (BudgetEntity) -> Unit
) : RecyclerView.Adapter<BudgetItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: BudgetListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BudgetListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = budgets[position]
        holder.binding.txtCategory.text = item.name
        holder.binding.txtAmount.text = "IDR %,d".format(item.amount.toInt())

        holder.binding.btnEdit.setOnClickListener {
            onEditClick(item)
        }
    }

    override fun getItemCount(): Int = budgets.size
}
