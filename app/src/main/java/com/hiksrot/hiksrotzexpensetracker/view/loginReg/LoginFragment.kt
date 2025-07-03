package com.hiksrot.hiksrotzexpensetracker.view.loginReg

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentLoginBinding
import com.hiksrot.hiksrotzexpensetracker.util.SessionManager
import com.hiksrot.hiksrotzexpensetracker.view.MainActivity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.LoginRegisterViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[LoginRegisterViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.actionRegister)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()
            val hashedPassword = viewModel.hashPassword(password)
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Isi username dan password", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(username, hashedPassword)
            }
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                val user = viewModel.loggedInUser.value
                if (user != null) {
                    // Simpan user ID ke SharedPreferences
                    SessionManager.saveUserSession(requireContext(), user.id, user.username)

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("username", user.username)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Login berhasil tapi user null", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
