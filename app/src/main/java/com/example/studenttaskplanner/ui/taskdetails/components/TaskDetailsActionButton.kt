package com.example.studenttaskplanner.ui.taskdetails.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CompletionActionButton(
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = if (isCompleted) {
                "Mark pending"
            } else {
                "Mark completed"
            }
        )
    }
}

@Composable
fun EditAndShareButtons(
    onEditTask: () -> Unit,
    onShareTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onEditTask,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Edit task")
        }

        OutlinedButton(
            onClick = onShareTask,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Share")
        }
    }
}

@Composable
fun DeleteTaskButton(
    onDeleteTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onDeleteTask,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Text("Delete task")
    }
}