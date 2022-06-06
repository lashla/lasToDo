package com.lasha.lastodo.ui.show_todo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import com.lasha.lastodo.ui.todos.TodosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_edit_dialog.*
import kotlinx.android.synthetic.main.show_todo.*
import java.time.LocalDateTime
import java.util.*

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
        dateOfTodo.text = navArgs.currentTodo.date
    }

    private fun setupClickListeners(){
        clockBtn.setOnClickListener{

        }
        editBtn.setOnClickListener {
            showEditSheetDialog()
        }
        deleteBtn.setOnClickListener {
            viewModel.deleteTodo(navArgs.currentTodo)
        }
        backBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_showTodoFragment_to_todosFragment)
        }
    }

    private fun showEditSheetDialog(){
        val action = ShowTodoFragmentDirections.actionShowTodoFragmentToBottomSheet(navArgs.currentTodo)
        findNavController().navigate(action)
    }

    private fun setupBottomSheetButtons(){
        deadlineBtn.setOnClickListener{
            selectDatePicker()
        }
        addImageBtn.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage(){
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, 1)
    }
    private fun selectDatePicker(){
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day  = myCalendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), R.style.datePicker,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
                deadlineBtn.text = selectedDate
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis()
        dpd.show()
    }
}