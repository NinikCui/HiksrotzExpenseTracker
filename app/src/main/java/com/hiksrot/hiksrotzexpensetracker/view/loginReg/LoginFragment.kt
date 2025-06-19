package com.hiksrot.hiksrotzexpensetracker.view.loginReg

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentLoginBinding
import com.hiksrot.hiksrotzexpensetracker.view.MainActivity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginRegisterViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(
            inflater,
            container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(LoginRegisterViewModel::class.java)
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.actionRegister)
        }

        binding.btnLogin.setOnClickListener(){
            val username = binding.txtUsername.text.toString().trim()
            val password = binding.txtPassword.text.toString()

            try {
                viewModel.Login(username, password)
            } catch (e: Exception) {
                Log.e("LoginFragment", "Login error: ${e.message}", e)
                Toast.makeText(requireContext(), "Terjadi error: ${e.message}", Toast.LENGTH_LONG).show()
            }


        }


        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Login berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("username", (binding.txtUsername.text.toString().trim()))
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }


    }

}