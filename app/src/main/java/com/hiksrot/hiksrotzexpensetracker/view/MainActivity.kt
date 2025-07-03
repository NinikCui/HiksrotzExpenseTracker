package com.hiksrot.hiksrotzexpensetracker.view

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityMainBinding
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LoginRegisterViewModel

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        viewModel = ViewModelProvider(this)[LoginRegisterViewModel::class.java]

        val username = SessionManager.getUsername(this)
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

    override fun onResume() {
        super.onResume()
        lightSensor?.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val lightLevel = it.values[0]
                if (lightLevel < 50) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}
