package com.hiksrot.hiksrotzexpensetracker.view.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentEditProfileBinding
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ProfileViewModel

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.userLD.observe(viewLifecycleOwner, Observer { user ->
            binding.user = user
        })

        viewModel.changePasswordResult.observe(viewLifecycleOwner, Observer { result ->
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()

            if (result == "Password changed successfully") {
                binding.editOldPassword.text?.clear()
                binding.editNewPassword.text?.clear()
                binding.editRepeatPassword.text?.clear()
            }
        })

        viewModel.signOutResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()
                // ERROR
                findNavController().navigate(R.id.action_editProfileFragment_to_loginFragment)
            }
        })
    }
}