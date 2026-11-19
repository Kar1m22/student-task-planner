package com.example.studenttaskplanner.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studenttaskplanner.ui.taskdetails.TaskDetailsScreen
import com.example.studenttaskplanner.ui.taskform.TaskFormScreen
import com.example.studenttaskplanner.ui.tasklist.TaskListScreen


@Composable
fun PlannerNavHost() {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = PlannerDestination.TASK_LIST
    ) {
        composable(PlannerDestination.TASK_LIST) {
            TaskListScreen(
                onAddTask = {
                    navController.navigate(PlannerDestination.TASK_FORM)
                },
                onTaskClick = { taskId ->
                    navController.navigate(
                        "${PlannerDestination.TASK_DETAILS}/$taskId"
                    )
                }
            )
        }

        composable(PlannerDestination.TASK_FORM) {
            TaskFormScreen(
                taskId = null,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTaskDeleted = {
                    navController.popBackStack(
                        PlannerDestination.TASK_LIST,
                        inclusive = false
                    )
                }
            )
        }
        composable(
            route = PlannerDestination.TASK_FORM_EDIT,
            arguments = listOf(
                navArgument(PlannerDestination.TASK_ID_ARGUMENT) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong(
                PlannerDestination.TASK_ID_ARGUMENT
            ) ?: return@composable

            TaskFormScreen(
                taskId = taskId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTaskDeleted = {
                    navController.popBackStack(
                        PlannerDestination.TASK_LIST,
                        inclusive = false
                    )
                }
            )
        }

        composable(
            route = PlannerDestination.TASK_DETAILS_WITH_ID,
            arguments = listOf(
                navArgument(PlannerDestination.TASK_ID_ARGUMENT) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong(
                PlannerDestination.TASK_ID_ARGUMENT
            ) ?: return@composable

            TaskDetailsScreen(
                taskId = taskId,
                onEditTask = { id ->
                    navController.navigate(
                        "${PlannerDestination.TASK_FORM}/$id"
                    )
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}