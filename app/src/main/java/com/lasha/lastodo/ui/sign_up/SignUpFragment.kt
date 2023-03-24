package com.lasha.lastodo.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lasha.lastodo.R
import com.lasha.lastodo.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.run {
            signupBtn.setOnClickListener {
                createAccount()
            }
            toSignIn.setOnClickListener {
                val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun createAccount() {
        binding.run {
            val email = emailEt.text.toString()
            val newPassword = newPasswordEt.text.toString()
            val passwordConfirm = passwordConfimEt.text.toString()
            if (email.isNotEmpty() && newPassword == passwordConfirm && newPassword.isNotEmpty()) {
                viewModel.registerUser(email, newPassword)
            }
        }
    }

    private fun initViewModel() {
        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                val action = SignUpFragmentDirections.actionSignUpFragmentToTodosFragment(newTodo = null)
                findNavController().navigate(action)
            }
        }
        viewModel.exception.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        }
    }
}