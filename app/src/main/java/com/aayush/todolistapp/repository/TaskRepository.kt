package com.aayush.todolistapp.repository

import com.aayush.todolistapp.database.TaskDatabase
import com.aayush.todolistapp.model.Task

class TaskRepository(private val db: TaskDatabase) {

    suspend fun insertTask(task: Task) = db.getTaskDao().insertTask(task)
    suspend fun deleteTask(task: Task) = db.getTaskDao().deleteTask(task)
    suspend fun updateTask(task: Task) = db.getTaskDao().updateTask(task)

    fun getAllTasks() = db.getTaskDao().getAllTasks()

    suspend fun updateTaskCompletionStatus(taskId: Int, completed: Boolean, date: String, time: String) {
        db.getTaskDao().updateCompletionStatus(taskId, completed, date, time)
    }
}