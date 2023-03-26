package com.lasha.lastodo.ui.show_todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lasha.lastodo.R
import com.lasha.lastodo.databinding.FragmentTodoDetailsBinding
import com.lasha.lastodo.ui.utils.toTimeString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowTodoFragment : Fragment(R.layout.fragment_todo_details) {

    private val navArgs by navArgs<ShowTodoFragmentArgs>()

    private var _binding: FragmentTodoDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        binding.run {
            titleOfCurrentTodo.text = navArgs.currentTodo.subject
            todoDescription.text = navArgs.currentTodo.contents
            todoDescription.visibility = View.VISIBLE
            deadlineDateView.text = navArgs.currentTodo.deadlineDate
            dateOfTodo.text = navArgs.currentTodo.date.toLong().toTimeString()
        }
    }

    private fun setupClickListeners() {
        binding.run {
            clockBtn.setOnClickListener {
                deadlineDateView.visibility = View.VISIBLE
                deadlineDateViewHolder.visibility = View.VISIBLE
            }
            editBtn.setOnClickListener {
                showEditSheetDialog()
            }
            deleteBtn.setOnClickListener {
                val action =
                    ShowTodoFragmentDirections.actionShowTodoFragmentToDeleteTodoDialog2(navArgs.currentTodo)
                findNavController().navigate(action)
            }
            backBtn.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_showTodoFragment_to_todosFragment)
            }
        }
    }

    private fun showEditSheetDialog() {
        val action =
            ShowTodoFragmentDirections.actionShowTodoFragmentToBottomSheet(navArgs.currentTodo)
        findNavController().navigate(action)
    }
}