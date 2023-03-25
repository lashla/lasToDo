package com.lasha.lastodo.ui.show_todo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lasha.lastodo.R
import com.lasha.lastodo.databinding.DialogDeleteTodoBinding
import com.lasha.lastodo.ui.bottom_sheet.AddEditDialogFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteTodoDialog : DialogFragment() {

    private val viewModel by viewModels<DeleteTodoViewModel>()
    private val navArgs by navArgs<AddEditDialogFragmentArgs>()

    private var _binding: DialogDeleteTodoBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDeleteTodoBinding.inflate(inflater, container, false)
        dialog.let {
            it?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog ?: return
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteClickListeners()
    }

    private fun deleteClickListeners() {
        binding.run {
            deleteTodo.setOnClickListener {
                navArgs.currentTodo?.let { itemToDelete -> viewModel.deleteTodo(itemToDelete, requireContext()) }
                findNavController().navigate(R.id.action_deleteTodoDialog_to_todosFragment)
            }
            cancelTodoDeletion.setOnClickListener {
                dismiss()
            }
        }
    }
}