package com.lasha.lastodo.ui.show_todo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lasha.lastodo.R
import com.lasha.lastodo.ui.bottom_sheet.AddEditDialogFragmentArgs
import com.lasha.lastodo.utils.CheckInternetConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.delete_todo.*

@AndroidEntryPoint
class DeleteTodoDialog: DialogFragment() {
    private lateinit var viewModel: DeleteTodoViewModel
    private val navArgs by navArgs<AddEditDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(requireContext(), R.layout.delete_todo, null)
        dialog.let {
            it?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?:return
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DeleteTodoViewModel::class.java]
        deleteClickListeners()
    }

    private fun deleteClickListeners(){
        deleteTodo.setOnClickListener {
            viewModel.deleteTodo(navArgs.currentTodo!!, isInternetConnected())
            findNavController().navigate(R.id.action_deleteTodoDialog_to_todosFragment)
        }
        cancelTodo.setOnClickListener {
            dismiss()
        }
    }
    private fun isInternetConnected(): Boolean{
        return CheckInternetConnection.connectivityStatus(requireContext())
    }
}