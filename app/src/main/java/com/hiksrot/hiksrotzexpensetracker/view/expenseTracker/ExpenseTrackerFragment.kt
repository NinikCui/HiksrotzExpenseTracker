package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiksrot.hiksrotzexpensetracker.databinding.DialogExpenseDetailBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentExpenseTrackerBinding
import com.hiksrot.hiksrotzexpensetracker.model.dto.ExpenseItem
import com.hiksrot.hiksrotzexpensetracker.model.entities.ExpenseEntity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ExpenseTrackerFragment : Fragment(), ExpenseClickListener {

    private lateinit var binding: FragmentExpenseTrackerBinding
    private lateinit var vm: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) setup binding listener untuk FAB
        binding.listener = this

        // 2) setup ViewModel & data
        vm = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        vm.fetchExpenses(1)

        // 3) setup RecyclerView + adapter
        adapter = ExpenseAdapter(mutableListOf(), this)
        binding.recExpense.layoutManager = LinearLayoutManager(context)
        binding.recExpense.adapter = adapter

        val testTimestamp = 1749988800000L
        val sdf = SimpleDateFormat("dd MMM yyyy hh.mm a", Locale("id")).apply {
            timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        }
        Log.d("WAKTUDEBUG", "Waktu format: ${sdf.format(Date(testTimestamp))}")

        // 4) observe
        vm.expensesLD.observe(viewLifecycleOwner) {
            adapter.updateList(it)
            binding.txtError.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onAddExpenseClick(v: View) {
        val action = ExpenseTrackerFragmentDirections
            .actionNewExpense()
        Navigation.findNavController(v).navigate(action)
    }

    override fun onExpenseItemClick(v: View, exp: ExpenseItem) {
        Log.d("EXPENSECLICK", "Clicked expense = $exp")

        val dialogBinding = DialogExpenseDetailBinding.inflate(layoutInflater)

        dialogBinding.exp = exp

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}