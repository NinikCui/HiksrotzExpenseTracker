package com.hiksrot.hiksrotzexpensetracker.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentEditProfileBinding
import com.hiksrot.hiksrotzexpensetracker.view.loginReg.LoginRegActivity
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ProfileViewModel

class EditProfileFragment : Fragment(),EditPassListener  {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)


        return binding.root
    }

    private fun observeUserData() {
        viewModel.userLD.observe(viewLifecycleOwner) { user ->
            binding.user = user
        }
    }

    private fun observeResultEvents() {
        viewModel.changePasswordResult.observe(viewLifecycleOwner) { result ->
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            if (result == "Password changed successfully") {
                binding.editOldPassword.text?.clear()
                binding.editNewPassword.text?.clear()
                binding.editRepeatPassword.text?.clear()
            }
        }

        viewModel.signOutResult.observe(viewLifecycleOwner) { success ->
            Log.d("LogoutDebug", "signOutResult triggered with: $success")
            if (success) {
                Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginRegActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.editPassListener = this

        observeUserData()
        observeResultEvents()




    }
    override fun onEditPasswordClicked(v: View) {
        val oldPass = binding.editOldPassword.text.toString()
        val newPass = binding.editNewPassword.text.toString()
        val repeatPass = binding.editRepeatPassword.text.toString()

        when {
            oldPass.isBlank() || newPass.isBlank() || repeatPass.isBlank() -> {
                Toast.makeText(requireContext(), "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
            newPass != repeatPass -> {
                Toast.makeText(requireContext(), "Password baru tidak cocok", Toast.LENGTH_SHORT).show()
            }
            else -> {
                viewModel.oldPassword.value = oldPass
                viewModel.newPassword.value = newPass
                viewModel.repeatPassword.value = repeatPass
                viewModel.changePassword()
            }
        }
    }

    override fun btnLogOutClicked(v: View) {
        viewModel.signOut()
    }
}
