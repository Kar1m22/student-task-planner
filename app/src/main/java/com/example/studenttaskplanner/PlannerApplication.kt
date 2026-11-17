package com.example.studenttaskplanner

import android.app.Application
import com.example.studenttaskplanner.data.local.PlannerDatabase
import com.example.studenttaskplanner.data.repository.TaskRepository

class PlannerApplication: Application() {

    val database: PlannerDatabase by lazy {
        PlannerDatabase.getInstance(this)
    }

    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }
}