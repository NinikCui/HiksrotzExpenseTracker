package com.hiksrot.hiksrotzexpensetracker.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityLogregBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityMainBinding
import com.hiksrot.hiksrotzexpensetracker.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("username")

        binding.tvUsername.text = "Selamat datang, $username!"


    }
}