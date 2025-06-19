package com.hiksrot.hiksrotzexpensetracker.view.loginReg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityLogregBinding
import com.hiksrot.hiksrotzexpensetracker.viewmodel.MainViewModel

class LoginRegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogregBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogregBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}
