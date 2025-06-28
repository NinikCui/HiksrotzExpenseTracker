package com.hiksrot.hiksrotzexpensetracker.view.loginReg

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentLoginBinding
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentRegisterBinding
import com.hiksrot.hiksrotzexpensetracker.model.database.AppDatabase
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(
            inflater,
            container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(LoginRegisterViewModel::class.java)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.actionLoginFragment)
        }

        binding.btnRegis.setOnClickListener {
            try {
                val username = binding.txtUsername.text.toString().trim()
                val firstName = binding.txtFirstName.text.toString().trim()
                val lastName = binding.txtLastName.text.toString().trim()
                val password = binding.txtPassword.text.toString()
                val repeatPassword = binding.txtRepeatPassword.text.toString()

                if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password != repeatPassword) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val hashedPassword = viewModel.hashPassword(password)
                val user = UserEntity(username, hashedPassword, firstName, lastName)
                viewModel.Register(user)

                Toast.makeText(requireContext(), "Account created!", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.actionLoginFragment)
            } catch (e: Exception) {
                Log.e("RegisterFragment", "Register error", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }

        }

    }
}