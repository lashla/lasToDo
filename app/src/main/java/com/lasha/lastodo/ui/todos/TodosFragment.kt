package com.lasha.lastodo.ui.todos

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.synthetic.main.add_edit_dialog.*
import kotlinx.android.synthetic.main.todos_fragment.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TodosFragment: Fragment(R.layout.todos_fragment) {

    private val adapter = TodosRecyclerAdapter()
    private lateinit var viewModel: TodosViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TodosViewModel::class.java]
        initViewModel()
        initTodosView()
        setupBtnListeners()
    }

    private fun populateTodos(){
        val currentDate = LocalDateTime.now()
        if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()){
            viewModel.insertHandler(titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), "some PATH", deadlineBtn.text.toString())
        }
    }

    private fun initTodosView(){
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }
    private fun setupBtnListeners(){
        addNewTodoBtn.setOnClickListener {
            showAdditionSheetDialog()
        }

        adapter.onItemClickListener(object : TodosRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Navigation.findNavController(requireView()).navigate(R.id.action_todosFragment_to_showTodoFragment)
            }
        })

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
        dpd.show()
    }

    private fun showAdditionSheetDialog(){
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        bottomSheetDialog.setContentView(R.layout.add_edit_dialog)
        bottomSheetDialog.edgeToEdgeEnabled
        bottomSheetDialog.show()
        setupBottomSheetButtons()
    }

    private fun setupBottomSheetButtons(){
        deadlineBtn.setOnClickListener{
            selectDatePicker()
        }
        addEditBtn.setOnClickListener{
            populateTodos()
        }
    }

    private fun initViewModel(){
        viewModel.todosData.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                adapter.updateTodoInfo(it as ArrayList<Todos>)
                Log.i("ViewTodos", "Todos inserted")
            }
        }
    }
}