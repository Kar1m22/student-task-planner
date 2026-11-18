package com.example.studenttaskplanner.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.studenttaskplanner.data.local.TaskEntity
import com.example.studenttaskplanner.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


enum class TaskFilter {
    ALL,
    PENDING,
    COMPLETED
}

data class TaskListUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val selectedFilter: TaskFilter = TaskFilter.ALL
)

class TaskListViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(TaskFilter.ALL)

    val uiState = combine(
        taskRepository.observeTasks(),
        selectedFilter
    ) { tasks, filter ->
        TaskListUiState(
            tasks = tasks.filter { task ->
                when (filter) {
                    TaskFilter.ALL -> true
                    TaskFilter.PENDING -> !task.isCompleted
                    TaskFilter.COMPLETED -> task.isCompleted
                }
            },
            selectedFilter = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TaskListUiState()
    )

    fun selectFilter(filter: TaskFilter) {
        selectedFilter.value = filter
    }

    fun setTaskCompleted(
        taskId: Long,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            taskRepository.setTaskCompleted(
                taskId = taskId,
                isCompleted = isCompleted
            )
        }
    }
    companion object {
        fun provideFactory(
            taskRepository: TaskRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TaskListViewModel(taskRepository)
            }
        }
    }
}