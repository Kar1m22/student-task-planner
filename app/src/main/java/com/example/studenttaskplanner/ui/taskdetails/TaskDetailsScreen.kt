package com.example.studenttaskplanner.ui.taskdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.studenttaskplanner.ui.taskdetails.components.CompletionActionButton
import com.example.studenttaskplanner.ui.taskdetails.components.EditAndShareButtons
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttaskplanner.PlannerApplication
import com.example.studenttaskplanner.core.theme.PlannerTheme
import com.example.studenttaskplanner.data.local.TaskEntity
import com.example.studenttaskplanner.databinding.ViewTaskInfoBinding
import com.example.studenttaskplanner.core.sharing.shareTask
import com.example.studenttaskplanner.core.tasks.TaskStatus
import com.example.studenttaskplanner.core.tasks.displayName
import com.example.studenttaskplanner.core.tasks.formatTaskDeadline
import com.example.studenttaskplanner.core.tasks.status
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.studenttaskplanner.ui.taskdetails.components.DeleteTaskButton

@Composable
fun TaskDetailsScreen(
    taskId: Long,
    onEditTask: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val application = context.applicationContext as PlannerApplication

    val viewModel: TaskDetailsViewModel = viewModel(
        factory = TaskDetailsViewModel.provideFactory(
            taskId = taskId,
            taskRepository = application.taskRepository
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                TaskDetailsEvent.TaskDeleted -> {
                    onNavigateBack()
                }
            }
        }
    }

    when (val state = uiState) {
        TaskDetailsUiState.Loading -> {
            LoadingContent()
        }

        TaskDetailsUiState.TaskNotFound -> {
            TaskNotFoundContent(
                onNavigateBack = onNavigateBack
            )
        }

        is TaskDetailsUiState.Success -> {
            TaskDetailsContent(
                task = state.task,
                onNavigateBack = onNavigateBack,
                onEditTask = {
                    onEditTask(state.task.id)
                },
                onCompletionChanged = viewModel::setTaskCompleted,
                onShareTask = {
                    shareTask(
                        context = context,
                        task = state.task
                    )
                },
                onDeleteTask = {
                    viewModel.deleteTask(state.task)
                }
            )
        }
    }
}

@Composable
private fun TaskDetailsContent(
    task: TaskEntity,
    onNavigateBack: () -> Unit,
    onEditTask: () -> Unit,
    onCompletionChanged: (Boolean) -> Unit,
    onShareTask: () -> Unit,
    onDeleteTask: () -> Unit
) {
    var showDeleteConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    val taskStatus = task.status()

    val deadlineText = formatTaskDeadline(
        dueAtEpochMillis = task.dueAtEpochMillis
    )

    val statusText = taskStatus.displayName()

    val statusColor = when (taskStatus) {
        TaskStatus.COMPLETED -> PlannerTheme.statusColors.completed
        TaskStatus.OVERDUE -> PlannerTheme.statusColors.overdue
        TaskStatus.PENDING -> MaterialTheme.colorScheme.primary
    }

    val panelBackgroundColor =
        MaterialTheme.colorScheme.surface.toArgb()

    val supportingTextColor =
        MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    val mainTextColor =
        MaterialTheme.colorScheme.onSurface.toArgb()

    val statusTextColor = statusColor.toArgb()


    val completionButtonText = if (task.isCompleted) {
        "Mark pending"
    } else {
        "Mark completed"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onNavigateBack
        ) {
            Text(" <- Back")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = task.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            AndroidViewBinding(
                factory = ViewTaskInfoBinding::inflate,
                modifier = Modifier.fillMaxWidth()
            ) {
                root.setBackgroundColor(panelBackgroundColor)

                courseValue.text = task.course.ifBlank {
                    "No course"
                }
                deadlineValue.text = deadlineText
                statusValue.text = statusText
                courseLabel.setTextColor(supportingTextColor)
                deadlineLabel.setTextColor(supportingTextColor)
                statusLabel.setTextColor(supportingTextColor)
                courseValue.setTextColor(mainTextColor)
                deadlineValue.setTextColor(mainTextColor)
                statusValue.setTextColor(statusTextColor)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = task.description.ifBlank {
                "No description added."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))


        CompletionActionButton(
            isCompleted = task.isCompleted,
            onClick = {
                onCompletionChanged(!task.isCompleted)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        EditAndShareButtons(
            onEditTask = onEditTask,
            onShareTask = onShareTask
        )
        Spacer(modifier = Modifier.height(12.dp))

        DeleteTaskButton(
            onDeleteTask = {
                showDeleteConfirmation = true
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
            },
            title = {
                Text("Delete task?")
            },
            text = {
                Text(
                    "This will permanently delete \"${task.title}\"."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        onDeleteTask()
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TaskNotFoundContent(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Task not found",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This task may have been deleted.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateBack) {
            Text("Back to tasks")
        }
    }
}