package com.example.doittesttask.feature.tasklist.recycler

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doittesttask.R
import com.example.doittesttask.data.remote.entity.Priority
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item_task.*
import org.jetbrains.annotations.NotNull

open class TaskRecyclerAdapter : PagedListAdapter<TaskItem, TaskRecyclerAdapter.TaskViewHolder>(DIFF_CALLBACK) {
    open fun onItemClick(task: TaskItem) {

    }

    override fun onCreateViewHolder(@NotNull parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) ?: return

        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
        holder.bind(task)
    }

    class TaskViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(task: TaskItem) {
            textTitle.text = task.title
            //I wanted to use this format in list, but sorting by expiration time on server works wrong) "MM/dd/yyyy hh:mm"
            textDueTo.text = DateFormat.format("MM/dd/yyyy", task.dueBy).toString()
            val context = textPriority.context
            when (task.priority) {
                Priority.LOW -> {
                    textPriority.setText(R.string.taskListPriorityLow)
                    textPriority.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityLow))
                }
                Priority.NORMAL -> {
                    textPriority.setText(R.string.taskListPriorityNormal)
                    textPriority.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityNormal))
                }
                Priority.HIGH -> {
                    textPriority.setText(R.string.taskListPriorityHigh)
                    textPriority.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityHigh))
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TaskItem>() {
            override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}