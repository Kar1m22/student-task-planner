package com.example.studenttaskplanner.core.navigation

object PlannerDestination {
    const val TASK_LIST = "task_list"
    const val TASK_FORM = "task_form"
    const val TASK_DETAILS = "task_details"

    const val TASK_ID_ARGUMENT = "taskId"

    const val TASK_FORM_EDIT =
        "$TASK_FORM/{$TASK_ID_ARGUMENT}"

    const val TASK_DETAILS_WITH_ID =
        "$TASK_DETAILS/{$TASK_ID_ARGUMENT}"
}