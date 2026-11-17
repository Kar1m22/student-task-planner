package com.example.studenttaskplanner.ui.tasklist

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
fun TaskListScreen(
    onAddTask: () -> Unit,
    onTaskClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "My tasks",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Temporary navigation screen",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(onClick = onAddTask) {
            Text("Add task")
        }

        Button(onClick = { onTaskClick(1L) }) {
            Text("Open sample task")
        }
    }
}