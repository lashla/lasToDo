package com.lasha.lastodo.ui.todos

import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.add_edit_dialog.*
import kotlinx.android.synthetic.main.todos_fragment.*
import kotlinx.coroutines.NonDisposableHandle.parent
import java.time.LocalDateTime
import java.util.*


class TodosFragment: Fragment(R.layout.todos_fragment) {

    private val adapter = TodosRecyclerAdapter()
    private lateinit var viewModel: TodosViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TodosViewModel::class.java]
        initTodosView()
        populateTodos()
        setupBtnListeners()
    }

    private fun populateTodos(){
        val currentDate = LocalDateTime.now()
        if (titleEt.text.isNotEmpty() && descriptionEt.text.isNotEmpty()){
            viewModel.insertHandler(titleEt.text.toString(), descriptionEt.text.toString(), currentDate.toString(), "some PATH", "some date"  )
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
        addEditBtn.setOnClickListener{
            populateTodos()
        }
        adapter.onItemClickListener(object : TodosRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Navigation.findNavController(requireView()).navigate(R.id.action_todosFragment_to_showTodoFragment)
            }

        })
    }

    private fun showAdditionSheetDialog(){
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        bottomSheetDialog.setContentView(R.layout.add_edit_dialog)
        bottomSheetDialog.show()
    }
}