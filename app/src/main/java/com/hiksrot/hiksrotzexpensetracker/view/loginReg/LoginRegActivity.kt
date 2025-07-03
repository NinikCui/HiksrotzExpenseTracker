package com.hiksrot.hiksrotzexpensetracker.view.loginReg

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hiksrot.hiksrotzexpensetracker.databinding.ActivityLogregBinding
import com.hiksrot.hiksrotzexpensetracker.view.MainActivity

class LoginRegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogregBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId != -1) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", sharedPref.getString("username", ""))
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityLogregBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
