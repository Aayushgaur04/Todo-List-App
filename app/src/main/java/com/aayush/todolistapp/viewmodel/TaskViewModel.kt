package com.aayush.todolistapp.viewmodel

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aayush.todolistapp.adapter.TaskAdapter
import com.aayush.todolistapp.model.Task
import com.aayush.todolistapp.repository.TaskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(app: Application, private val taskRepository: TaskRepository) : AndroidViewModel(app){


    private val _taskCompletionStatusUpdated = MutableLiveData<Unit>()
    val taskCompletionStatusUpdated: LiveData<Unit>
        get() = _taskCompletionStatusUpdated

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    init {
        viewModelScope.launch {
            delay(3000)
            _isLoading.value = false
        }
    }

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun addTask(task: Task) = viewModelScope.launch {
        taskRepository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskRepository.deleteTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        taskRepository.updateTask(task)
    }

    fun getAllTasks() = taskRepository.getAllTasks()

    fun updateTaskCompletionStatus(taskId: Int, completed: Boolean, date: String, time: String) = viewModelScope.launch {
        taskRepository.updateTaskCompletionStatus(taskId, completed, date, time)
        _taskCompletionStatusUpdated.postValue(Unit)
    }

}