package com.lasha.lastodo.ui.todos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import kotlinx.android.synthetic.main.todos_fragment.*

class TodosFragment: Fragment(R.layout.todos_fragment) {

   private val adapter = TodosRecyclerAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTodosView()
        populateTodos()
    }

    private fun populateTodos(){
        val todos = ArrayList<Todos>()
        val test = Todos("Room", "YOU NEED TO LEARN", "26 march 2022")
        for (element in 0..4){
            todos.add(test)
        }
        adapter.updateTodoInfo(todos)
    }

    private fun initTodosView(){
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }
}