package com.example.studenttaskplanner.core.tasks

import com.example.studenttaskplanner.data.local.TaskEntity

enum class TaskStatus {
    PENDING,
    OVERDUE,
    COMPLETED
}

fun TaskEntity.status(
    currentTimeMillis: Long = System.currentTimeMillis()
): TaskStatus {
    return when {
        isCompleted -> TaskStatus.COMPLETED
        dueAtEpochMillis < currentTimeMillis -> TaskStatus.OVERDUE
        else -> TaskStatus.PENDING
    }
}

fun TaskStatus.displayName(): String {
    return when (this) {
        TaskStatus.PENDING -> "Pending"
        TaskStatus.OVERDUE -> "Overdue"
        TaskStatus.COMPLETED -> "Completed"
    }
}
