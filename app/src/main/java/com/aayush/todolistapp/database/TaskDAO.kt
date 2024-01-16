package com.aayush.todolistapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aayush.todolistapp.model.Task

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM TASKS ORDER BY id ASC")
    fun getAllTasks() : LiveData<List<Task>>

    @Query("UPDATE tasks SET isComplete = :completed, date = :date, time = :time WHERE id = :taskId")
    suspend fun updateCompletionStatus(taskId: Int, completed: Boolean, date: String, time: String)
}