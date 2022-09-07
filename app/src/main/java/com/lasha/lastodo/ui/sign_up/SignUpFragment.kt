package com.lasha.lastodo.ui.sign_up

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lasha.lastodo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_sign_up) {

    private lateinit var viewModel: SignUpViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupClickListeners()
    }

    private fun setupClickListeners(){
        signupBtn.setOnClickListener {
            createAccount()
        }
        toSignIn.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            findNavController().navigate(action)
        }
    }

    private fun createAccount(){
        val email = emailEt.text.toString()
        val newPassword = newPasswordEt.text.toString()
        val passwordConfirm = passwordConfimEt.text.toString()
        if (email.isNotEmpty() && newPassword == passwordConfirm && newPassword.isNotEmpty()){
            viewModel.registerUser(email, newPassword)
            Log.i("Gone to viewModel","SS")
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        viewModel.nav.observe(viewLifecycleOwner){
            if (it == true){
                val action = SignUpFragmentDirections.actionSignUpFragmentToTodosFragment()
                findNavController().navigate(action)
            }
        }
        viewModel.exception.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        }
    }
}