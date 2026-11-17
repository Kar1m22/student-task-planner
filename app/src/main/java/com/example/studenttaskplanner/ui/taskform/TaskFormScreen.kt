package com.example.studenttaskplanner.ui.taskform


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
fun TaskFormScreen(
    taskId: Long?,
    onNavigateBack: () -> Unit
) {
    val screenTitle = if (taskId == null) {
        "Add task"
    } else {
        "Edit task #$taskId"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = screenTitle,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "The real form will be added later.",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(onClick = onNavigateBack) {
            Text("Save placeholder and go back")
        }
    }
}