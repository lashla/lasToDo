package com.lasha.lastodo.ui.todos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.databinding.TodoViewBinding

class TodosRecyclerAdapter : RecyclerView.Adapter<TodosRecyclerAdapter.ViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    fun clearTodos() {
        todoList.clear()
        notifyItemRangeRemoved(INSERT_POSITION, todoList.count())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodoViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun updateTodoInfo(newInfo: MutableList<Todo>) {
        todoList = newInfo
        notifyItemRangeInserted(INSERT_POSITION, newInfo.count())
    }

    fun insertItem(todo: Todo) {
        todoList.add(todo)
        notifyItemInserted(todoList.lastIndex)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = todoList[position]
        with(holder) {
            binding.run {
                subject.text = items.subject
                dateOfDeadline.text = items.date
                contents.text = items.contents
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(items)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    private var onItemClickListener: ((Todo) -> Unit)? = null

    fun setOnItemClickListener(listener: (Todo) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val INSERT_POSITION = 0
    }
}