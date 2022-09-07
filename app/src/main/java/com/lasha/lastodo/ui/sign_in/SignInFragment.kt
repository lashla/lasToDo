package com.lasha.lastodo.ui.sign_in

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lasha.lastodo.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_login) {

    private lateinit var viewModel: SignInViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setClickListeners()
    }

    private fun setClickListeners(){
        loginBtn.setOnClickListener {
            viewModel.loginUser(loginEt.text.toString(), passwordEt.text.toString())
        }
        dontHaveAccountTv.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        viewModel.nav.observe(viewLifecycleOwner){
            if (it == true){
                val action = SignInFragmentDirections.actionSignInFragmentToTodosFragment()
                findNavController().navigate(action)
            }
        }
    }
}