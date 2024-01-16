package com.aayush.todolistapp.adapter

import android.graphics.Paint
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aayush.todolistapp.R
import com.aayush.todolistapp.databinding.TasksItemLayoutBinding
import com.aayush.todolistapp.fragments.HomeFragmentDirections
import com.aayush.todolistapp.model.Task
import com.aayush.todolistapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskAdapter(private var tasksViewModel : TaskViewModel) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val itemBinding : TasksItemLayoutBinding) : ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id && oldItem.taskTitle == newItem.taskTitle
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TasksItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = differ.currentList[position]

        holder.itemBinding.taskTextView.text = currentTask.taskTitle

        holder.itemBinding.dateTextView.text = currentTask.date
        holder.itemBinding.timeTextView.text = currentTask.time

        holder.itemBinding.radioButton.apply {
            isChecked = currentTask.isComplete

            setOnClickListener {
                val taskId = currentTask.id
                val isChecked = currentTask.isComplete
                tasksViewModel.updateTaskCompletionStatus(taskId, !isChecked,currentTask.date, currentTask.time)
            }
        }
        val isCompleted = currentTask.isComplete
        dullEffect(holder,isCompleted)
        holder.itemBinding.taskTextView.paintFlags =
            if (isCompleted) {
                Paint.STRIKE_THRU_TEXT_FLAG
            } else{
                0
            }

        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToUpdateTaskFragment(currentTask)
            it.findNavController().navigate(direction)
        }


        val dateTimeString = "${holder.itemBinding.dateTextView.text} ${holder.itemBinding.timeTextView.text} "
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val targetDateTime = dateTimeFormat.parse(dateTimeString)

        val handler = Handler()

        val timeChecker = Runnable {
            val currentTime = Calendar.getInstance().time

            if (targetDateTime != null && currentTime.after(targetDateTime)) {
                Log.d("TAG", "This is a debug message")
                tasksViewModel.deleteTask(currentTask)
            }
        }
        handler.postDelayed(timeChecker, 15000)

    }

    private fun dullEffect(holder: TaskViewHolder, isCompleted: Boolean) : Boolean {
        if (isCompleted) {
            holder.itemBinding.cardViewAnim.setCardBackgroundColor(ContextCompat.getColor(holder.itemBinding.cardViewAnim.context,
                R.color.lowAlphaColor))
            holder.itemBinding.taskTextView.alpha = 0.5f
            holder.itemBinding.radioButton.alpha = 0.5f
            holder.itemBinding.dateTextView.alpha = 0.5f
            holder.itemBinding.timeTextView.alpha = 0.5f
        } else {
            holder.itemBinding.cardViewAnim.setCardBackgroundColor(ContextCompat.getColor(holder.itemBinding.cardViewAnim.context,
                R.color.primaryAlphaColor))
            holder.itemBinding.taskTextView.alpha = 1.0f
            holder.itemBinding.radioButton.alpha = 1.0f
            holder.itemBinding.dateTextView.alpha = 1.0f
            holder.itemBinding.timeTextView.alpha = 1.0f
        }
        return isCompleted
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}