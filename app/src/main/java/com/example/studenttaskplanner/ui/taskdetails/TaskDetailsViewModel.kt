package com.example.studenttaskplanner.ui.taskdetails


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.studenttaskplanner.data.local.TaskEntity
import com.example.studenttaskplanner.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface TaskDetailsUiState {
    data object Loading : TaskDetailsUiState
    data object TaskNotFound : TaskDetailsUiState
    data class Success(
        val task: TaskEntity
    ) : TaskDetailsUiState
}
sealed interface TaskDetailsEvent {
    data object TaskDeleted : TaskDetailsEvent
}

class TaskDetailsViewModel(
    private val taskId: Long,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _events = Channel<TaskDetailsEvent>(
        capacity = Channel.BUFFERED
    )
    val events = _events.receiveAsFlow()

    val uiState: StateFlow<TaskDetailsUiState> =
        taskRepository.observeTaskById(taskId)
            .map { task ->
                if (task == null) {
                    TaskDetailsUiState.TaskNotFound
                } else {
                    TaskDetailsUiState.Success(task)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TaskDetailsUiState.Loading
            )

    fun setTaskCompleted(
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            taskRepository.setTaskCompleted(
                taskId = taskId,
                isCompleted = isCompleted
            )
        }
    }
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)

            _events.send(
                TaskDetailsEvent.TaskDeleted
            )
        }
    }

    companion object {
        fun provideFactory(
            taskId: Long,
            taskRepository: TaskRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TaskDetailsViewModel(
                    taskId = taskId,
                    taskRepository = taskRepository
                )
            }
        }
    }
}

