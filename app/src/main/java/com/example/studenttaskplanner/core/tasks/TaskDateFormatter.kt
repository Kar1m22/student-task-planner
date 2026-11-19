package com.example.studenttaskplanner.core.tasks

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatTaskDeadline(
    dueAtEpochMillis: Long
): String {
    return Instant
        .ofEpochMilli(dueAtEpochMillis)
        .atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofPattern(
                "MMM d, h:mm a",
                Locale.getDefault()
            )
        )
}
