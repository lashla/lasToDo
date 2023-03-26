package com.lasha.lastodo.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lasha.lastodo.databinding.FragmentLoginBinding
import com.lasha.lastodo.ui.utils.CheckInternetConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment: Fragment() {

    private val viewModel by viewModels<SignInViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.syncData(checkIsInternetConnected())
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setClickListeners()
    }

    private fun setClickListeners(){
        binding.run {
            loginBtn.setOnClickListener {
                viewModel.loginUser(loginEt.text.toString(), passwordEt.text.toString())
            }
            dontHaveAccountTv.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun initViewModel(){
        viewModel.currentUser.observe(viewLifecycleOwner){
            if (it != null){
                val action = SignInFragmentDirections.actionSignInFragmentToTodosFragment(newTodo = null)
                findNavController().navigate(action)
            }
        }
        viewModel.exception.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        }
        viewModel.isLoggedIn.observe(viewLifecycleOwner){
            if (it){
                val action = SignInFragmentDirections.actionSignInFragmentToTodosFragment(newTodo = null)
                findNavController().navigate(action)
            }
        }
    }

    fun checkIsInternetConnected() = CheckInternetConnection.connectivityStatus(requireContext())
}