package com.lasha.lastodo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lasha.lastodo.R
import com.lasha.lastodo.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.run {
            logoutBtn.setOnClickListener {
                viewModel.logout()
                val action = ProfileFragmentDirections.actionProfileFragmentToSignInFragment()
                findNavController().navigate(action)
            }
            passwordTv.setOnClickListener {

            }
            settingsBtnProf.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToTodosFragment(null)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupViews() {
        viewModel.user.observe(viewLifecycleOwner) {
            binding.run {
                fullnameTv.text = it.displayName
                emailTv.text = it.email
            }
        }
    }
}