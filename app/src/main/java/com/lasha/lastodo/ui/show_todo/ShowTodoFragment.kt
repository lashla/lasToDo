package com.lasha.lastodo.ui.show_todo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lasha.lastodo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.show_todo.*

@AndroidEntryPoint
class ShowTodoFragment: Fragment(R.layout.show_todo) {

    private val navArgs by navArgs<ShowTodoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupClickListeners()
    }

    private fun initViews(){
        titleOfCurrentTodo.text = navArgs.currentTodo.subject
        todoDescription.text = navArgs.currentTodo.contents
        dateOfTodo.text = navArgs.currentTodo.date
    }

    private fun setupClickListeners(){
        clockBtn.setOnClickListener{

        }
        editBtn.setOnClickListener {
            showEditSheetDialog()
        }
        deleteBtn.setOnClickListener {
            val action = ShowTodoFragmentDirections.actionShowTodoFragmentToDeleteTodoDialog2(navArgs.currentTodo)
            findNavController().navigate(action)
        }
        backBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_showTodoFragment_to_todosFragment)
        }
    }

    private fun showEditSheetDialog(){
        val action = ShowTodoFragmentDirections.actionShowTodoFragmentToBottomSheet(navArgs.currentTodo)
        findNavController().navigate(action)
    }
}