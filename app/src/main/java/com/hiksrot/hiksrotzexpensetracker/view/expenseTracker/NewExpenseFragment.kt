package com.hiksrot.hiksrotzexpensetracker.view.expenseTracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentNewExpenseBinding
import com.hiksrot.hiksrotzexpensetracker.model.entities.BudgetEntity
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.viewmodel.NewExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewExpenseFragment : Fragment(), NewExpenseListener {

    private lateinit var binding: FragmentNewExpenseBinding

    private lateinit var vm: NewExpenseViewModel
    private var budgetList: List<BudgetEntity> = emptyList()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var pickedLatitude: Double? = null
    private var pickedLongitude: Double? = null

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        binding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(NewExpenseViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.vm = vm
        binding.listener = this
        binding.lifecycleOwner = viewLifecycleOwner

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id"))
        binding.textDate.text = sdf.format(Date())

        val cal = Calendar.getInstance()
        val monthNames = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val nowMonth = monthNames[cal.get(Calendar.MONTH)]
        val nowYear = cal.get(Calendar.YEAR)
        binding.textTitle.text = "Add your expense on $nowMonth $nowYear"

        val userId = SessionManager.getUserId(requireContext())
        if (userId != -1) {
            vm.loadBudgetsForCurrentMonth(userId)
        }

        observeViewModel()

        binding.buttonAddExpense.setOnClickListener {
            vm.saveExpense(
                onSuccess = {
                    Toast.makeText(requireContext(), "Pengeluaran tersimpan!", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                },
                onError = { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            )
        }
        binding.buttonPickLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1001
                )
                return@setOnClickListener
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    pickedLatitude = location.latitude
                    pickedLongitude = location.longitude
                    binding.textLocation.text = "Lokasi: $pickedLatitude, $pickedLongitude"
                } else {
                    // Fallback: requestLocationUpdates
                    val locationRequest = com.google.android.gms.location.LocationRequest.create()
                        .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setNumUpdates(1)
                        .setInterval(1000)
                    fusedLocationClient.requestLocationUpdates(locationRequest, object : com.google.android.gms.location.LocationCallback() {
                        override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                            val newLoc = result.lastLocation
                            if (newLoc != null) {
                                pickedLatitude = newLoc.latitude
                                pickedLongitude = newLoc.longitude
                                binding.textLocation.text = "Lokasi: $pickedLatitude, $pickedLongitude"
                            } else {
                                binding.textLocation.text = "Lokasi tetap tidak ditemukan"
                            }
                            fusedLocationClient.removeLocationUpdates(this)
                        }
                    }, android.os.Looper.getMainLooper())
                }
            }
        }
    }

    private fun observeViewModel() {
        // 1. Observe budgets → isi spinner
        vm.budgets.observe(viewLifecycleOwner) { list ->
            budgetList = list
            val labels = list.map { it.name }
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, labels).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = adapter
            }
        }

        // 2. Setup listener untuk item spinner
        binding.spinnerCategory.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>,
                    view: android.view.View?,
                    pos: Int,
                    id: Long
                ) {
                    val bud = budgetList[pos]
                    vm.selectedBudget.value = bud
                    vm.loadBudgetUsage(bud.id)
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
            }

        // 3. Observer lainnya → update hint dan progress
        fun updateUI() {
            val left = vm.getRemainingBudget().toInt()
            val pct  = vm.getProgressPercent()
            binding.editTextNominal.hint = "Nominal (IDR $left left)"
            binding.progressBarBudget.progress = pct
        }

        vm.nominal.observe(viewLifecycleOwner) {
            updateUI()
        }
        vm.budgetUsed.observe(viewLifecycleOwner) {
            updateUI()
        }
        vm.selectedBudget.observe(viewLifecycleOwner) {
            updateUI()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onAddExpenseClick(v: View){
        vm.saveExpense(
            latitude = pickedLatitude,
            longitude = pickedLongitude,
            onSuccess = {
                Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            },
            onError = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
