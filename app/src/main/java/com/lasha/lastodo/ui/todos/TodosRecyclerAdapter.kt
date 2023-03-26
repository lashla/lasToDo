package com.lasha.lastodo.ui.todos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lasha.lastodo.data.model.Todo
import com.lasha.lastodo.databinding.TodoItemBinding
import com.lasha.lastodo.ui.utils.toTimeString

class TodosRecyclerAdapter(private val onItemClickListener: (Todo) -> Unit) :
    RecyclerView.Adapter<TodoViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    fun clearTodos() {
        todoList.clear()
        notifyItemRangeRemoved(INSERT_POSITION, todoList.count())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClickListener
        )
    }

    fun updateTodoInfo(newInfo: MutableList<Todo>) {
        todoList = newInfo
        notifyItemRangeInserted(INSERT_POSITION, newInfo.count())
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = todoList[position]
        holder.bindItems(item)
    }

    override fun getItemCount() = todoList.size

    companion object {
        private const val INSERT_POSITION = 0
    }
}

class TodoViewHolder(
    private val binding: TodoItemBinding,
    private val onItemClickListener: (Todo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItems(item: Todo) {
        binding.run {
            subject.text = item.subject
            contents.text = item.contents
            dateOfDeadline.text = item.date.toLong().toTimeString()
            root.setOnClickListener {
                onItemClickListener.invoke(item)
            }
        }
    }
}