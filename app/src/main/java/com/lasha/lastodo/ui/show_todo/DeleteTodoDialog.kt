package com.lasha.lastodo.ui.show_todo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.ui.bottom_sheet.AddEditDialogFragmentArgs
import kotlinx.android.synthetic.main.delete_todo.*

class DeleteTodoDialog: DialogFragment() {
    private lateinit var viewModel: ShowTodoViewModel
    private val navArgs by navArgs<AddEditDialogFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShowTodoViewModel::class.java]
        deleteClickListeners()
    }

    private fun deleteClickListeners(){
        deleteTodo.setOnClickListener {
            viewModel.deleteTodo(navArgs.currentTodo!!)
            dismiss()
        }
        cancelTodo.setOnClickListener {
            dismiss()
        }
    }
}