package com.hiksrot.hiksrotzexpensetracker.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.databinding.FragmentProfileBinding
import com.hiksrot.hiksrotzexpensetracker.viewmodel.ProfileViewModel

class ProfileFragment : Fragment(), ProfileListener {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)



        return binding.root
    }

    private fun observeViewModel() {
        // Observe user data dari ViewModel
        viewModel.userLD.observe(viewLifecycleOwner, Observer { user ->
            // Set user data ke binding
            binding.user = user
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding.listener = this
        viewModel.loadUserData()
        observeViewModel()

    }


    override fun onButtonEditClicked(v: View) {
            findNavController().navigate(R.id.actionEditProfile)
    }
}