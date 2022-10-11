package com.lasha.lastodo.ui.todos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos
import kotlinx.android.synthetic.main.todo_view.view.*

class TodosRecyclerAdapter: RecyclerView.Adapter<TodosRecyclerAdapter.ViewHolder>() {

    private var todoList = ArrayList<Todos>()

    fun clearTodos(){
        todoList.clear()
        notifyItemRangeRemoved(0, todoList.count())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_view, parent, false)
        return ViewHolder(view)
    }

    fun updateTodoInfo(newInfo: ArrayList<Todos>){
        todoList = newInfo
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = todoList[position]
        Log.i("Recycler todoList", todoList.size.toString() + todoList[position])
        holder.itemView.apply {
            subject.text = items.subject
            dateOfDeadline.text = items.date
            contents.text = items.contents
            setOnClickListener {
                onItemClickListener?.let {
                    it(items)
                }
            }
        }
    }

    override fun getItemCount(): Int { return todoList.size }

    private var onItemClickListener: ((Todos) -> Unit)? = null

    fun setOnItemClickListener(listener: (Todos) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

//    interface onItemClickListener{
//        fun onItemClick(position: Int)
//    }

}