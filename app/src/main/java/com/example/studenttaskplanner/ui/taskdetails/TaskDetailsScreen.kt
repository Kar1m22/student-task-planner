package com.example.studenttaskplanner.ui.taskdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskDetailsScreen(
    taskId: Long,
    onEditTask: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task details",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Temporary task ID: $taskId",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(onClick = { onEditTask(taskId) }) {
            Text("Edit task")
        }

        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}