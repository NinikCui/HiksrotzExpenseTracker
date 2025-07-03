package com.hiksrot.hiksrotzexpensetracker.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityMainBinding
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginRegisterViewModel::class.java]

        val username = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            viewModel.fetchUserByUsername(username)
        }

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment
            val navController = navHostFragment.navController

            binding.bottomNav.setupWithNavController(navController)
            binding.bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.itemExpense -> {
                        // Reset ke itemExpense jika bukan di sana
                        if (navController.currentDestination?.id != R.id.itemExpense) {
                            navController.popBackStack(R.id.itemExpense, false)
                            navController.navigate(R.id.itemExpense)
                        }
                        true
                    }

                    R.id.itemReport -> {
                        navController.navigate(R.id.itemReport)
                        true
                    }

                    R.id.itemProfile -> {
                        navController.navigate(R.id.itemProfile)
                        true
                    }

                    R.id.itemBudget -> {
                        navController.navigate(R.id.itemBudget)
                        true
                    }

                    else -> false
                }
            }
    }
}
