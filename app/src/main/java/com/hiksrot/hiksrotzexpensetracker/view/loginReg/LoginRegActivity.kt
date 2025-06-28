package com.hiksrot.hiksrotzexpensetracker.view.loginReg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityLogregBinding

class LoginRegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogregBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogregBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}
