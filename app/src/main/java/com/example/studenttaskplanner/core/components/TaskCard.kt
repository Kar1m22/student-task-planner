package com.example.studenttaskplanner.core.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.studenttaskplanner.core.theme.PlannerTheme
import com.example.studenttaskplanner.data.local.TaskEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun TaskCard(
    task: TaskEntity,
    onTaskClick: () -> Unit,
    onCompletionChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOverdue = !task.isCompleted &&
            task.dueAtEpochMillis < System.currentTimeMillis()

    val deadlineText = Instant
        .ofEpochMilli(task.dueAtEpochMillis)
        .atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofPattern(
                "MMM d, h:mm a",
                Locale.getDefault()
            )
        )

    val statusColor = when {
        task.isCompleted -> PlannerTheme.statusColors.completed
        isOverdue -> PlannerTheme.statusColors.overdue
        else -> MaterialTheme.colorScheme.primary
    }

    val statusText = when {
        task.isCompleted -> "✓ Completed"
        isOverdue -> "Overdue · $deadlineText"
        else -> deadlineText
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTaskClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCompletionChanged
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                if (task.course.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = task.course,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelLarge,
                    color = statusColor
                )
            }
        }
    }
}