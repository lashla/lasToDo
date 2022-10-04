package com.lasha.lastodo.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private val  viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setClickListeners()
    }

    private fun setClickListeners(){
        logoutBtn.setOnClickListener {
            viewModel.logout()
            val action = ProfileFragmentDirections.actionProfileFragmentToSignInFragment()
            findNavController().navigate(action)
        }
        passwordTv.setOnClickListener {

        }
        settingsBtnProf.setOnClickListener{
            val action = ProfileFragmentDirections.actionProfileFragmentToTodosFragment()
            findNavController().navigate(action)
        }
    }
    private fun setupViews(){
        viewModel.user.observe(viewLifecycleOwner){
            if (it != null){
                fullnameTv.text = it.displayName
                emailTv.text = it.email
            }
        }
    }
}