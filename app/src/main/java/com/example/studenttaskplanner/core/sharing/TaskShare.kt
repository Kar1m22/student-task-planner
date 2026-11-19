package com.example.studenttaskplanner.core.sharing

import android.content.Context
import android.content.Intent
import com.example.studenttaskplanner.core.tasks.displayName
import com.example.studenttaskplanner.core.tasks.formatTaskDeadline
import com.example.studenttaskplanner.core.tasks.status
import com.example.studenttaskplanner.data.local.TaskEntity

fun shareTask(
    context: Context,
    task: TaskEntity
) {
    val shareText = """
        Task: ${task.title}
        Course: ${task.course.ifBlank { "Not specified" }}
        Deadline: ${formatTaskDeadline(task.dueAtEpochMillis)}
        Status: ${task.status().displayName()}
        Description: ${task.description.ifBlank { "No description" }}
    """.trimIndent()

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    context.startActivity(
        Intent.createChooser(
            shareIntent,
            "Share task"
        )
    )
}

