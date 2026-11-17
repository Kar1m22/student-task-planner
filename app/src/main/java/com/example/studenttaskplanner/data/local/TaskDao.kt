package com.example.studenttaskplanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query(" SELECT * FROM tasks ORDER BY isCompleted ASC, dueAtEpochMillis ASC " )
    fun observeTasks(): Flow<List<TaskEntity>>

    @Query(" SELECT * FROM tasks WHERE id = :taskId ")
    fun observeTaskById(taskId: Long): Flow<TaskEntity?>

    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query(" UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId ")
    suspend fun updateCompletion(taskId: Long, isCompleted: Boolean)

}