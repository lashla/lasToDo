package com.lasha.lastodo.ui.todos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lasha.lastodo.R
import com.lasha.lastodo.data.model.Todos

class TodosRecyclerAdapter: RecyclerView.Adapter<TodosRecyclerAdapter.ViewHolder>() {

    private var todoList = ArrayList<Todos>()

    private lateinit var mlistner: onItemClickListener

    fun onItemClickListener(listener: onItemClickListener){
        mlistner = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_view, parent, false)
        return ViewHolder(view, mlistner)
    }

    fun updateTodoInfo(newInfo: ArrayList<Todos>){
        todoList = newInfo
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = todoList[position]
        holder.todoContents.text = items.contents
        holder.subject.text = items.subject
        holder.dateOfDeadline.text = items.date
    }

    override fun getItemCount(): Int { return todoList.size }


    class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val subject: TextView = itemView.findViewById(R.id.subject)
        val todoContents: TextView = itemView.findViewById(R.id.contents)
        val dateOfDeadline: TextView = itemView.findViewById(R.id.dateOfDeadline)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

}