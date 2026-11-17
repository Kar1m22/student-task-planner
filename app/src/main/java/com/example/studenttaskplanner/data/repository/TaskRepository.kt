package com.example.studenttaskplanner.data.repository

import com.example.studenttaskplanner.data.local.TaskDao
import com.example.studenttaskplanner.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {
    fun observeTasks(): Flow<List<TaskEntity>> {
        return taskDao.observeTasks()
    }

    fun observeTaskById(taskId: Long): Flow<TaskEntity?> {
        return taskDao.observeTaskById(taskId)
    }

    suspend fun addTask(task: TaskEntity): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun setTaskCompleted(
        taskId: Long,
        isCompleted: Boolean
    ) {
        taskDao.updateCompletion(taskId, isCompleted)
    }
}