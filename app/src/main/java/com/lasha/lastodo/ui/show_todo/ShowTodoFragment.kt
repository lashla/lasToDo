package com.lasha.lastodo.ui.show_todo

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lasha.lastodo.R
import com.lasha.lastodo.ui.todos.TodosViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.show_todo.*

@AndroidEntryPoint
class ShowTodoFragment: Fragment(R.layout.show_todo) {

    private val navArgs by navArgs<ShowTodoFragmentArgs>()
    private lateinit var viewModel: ShowTodoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShowTodoViewModel::class.java]
        initViews()
        setupClickListeners()
    }

    private fun initViews(){
        titleOfCurrentTodo.text = navArgs.currentTodo.subject
        todoDescription.text = navArgs.currentTodo.contents
        if (navArgs.currentTodo.photoPath != "null"){
            Picasso.get()
                .load(navArgs.currentTodo.photoPath)
                .error(R.drawable.ic_dialog_line)
                .resize(300,400)
                .centerCrop()
                .into(textImage)
            textImage.visibility = View.VISIBLE
        } else if (!navArgs.currentTodo.photoLink.isNullOrEmpty() && navArgs.currentTodo.photoLink != "null"){
            navArgs.currentTodo.photoLink!!.toUri().lastPathSegment?.let {
                viewModel.downloadPicture(
                    it, requireContext().contentResolver, navArgs.currentTodo)
            }
            Picasso.get()
                .load(navArgs.currentTodo.photoLink)
                .error(R.drawable.ic_dialog_line)
                .resize(300,400)
                .centerCrop()
                .into(textImage)
            textImage.visibility = View.VISIBLE
        }
        todoDescription.visibility = View.VISIBLE
        deadlineDateView.text = navArgs.currentTodo.deadlineDate
        dateOfTodo.text = navArgs.currentTodo.date
    }

    private fun setupClickListeners(){
        clockBtn.setOnClickListener{
            deadlineDateView.visibility = View.VISIBLE
            deadlineDateViewHolder.visibility = View.VISIBLE
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