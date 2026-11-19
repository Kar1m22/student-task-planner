package com.example.studenttaskplanner.ui.taskform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.studenttaskplanner.data.local.TaskEntity
import com.example.studenttaskplanner.data.repository.TaskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskFormUiState(
    val title: String = "",
    val course: String = "",
    val description: String = "",
    val dueAtEpochMillis: Long = System.currentTimeMillis() + 86_400_000L,
    val isCompleted: Boolean = false,
    val showTitleError: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isEditing: Boolean = false
)

class TaskFormViewModel(
    private val taskRepository: TaskRepository,
    private val taskId: Long?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TaskFormUiState(
            isEditing = taskId != null && taskId != 0L
        )
    )
    val uiState: StateFlow<TaskFormUiState> = _uiState.asStateFlow()

    init {
        if (taskId != null && taskId != 0L) {
            loadExistingTask(taskId)
        }
    }

    private fun loadExistingTask(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val existingTask = taskRepository.observeTaskById(id).firstOrNull()
            if (existingTask != null) {
                _uiState.update {
                    it.copy(
                        title = existingTask.title,
                        course = existingTask.course,
                        description = existingTask.description,
                        dueAtEpochMillis = existingTask.dueAtEpochMillis,
                        isCompleted = existingTask.isCompleted,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun refreshForm() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            // Waiting state (simulating async refresh)
            delay(1000L)

            if (taskId != null && taskId != 0L) {
                val existingTask = taskRepository.observeTaskById(taskId).firstOrNull()
                if (existingTask != null) {
                    _uiState.update {
                        it.copy(
                            title = existingTask.title,
                            course = existingTask.course,
                            description = existingTask.description,
                            dueAtEpochMillis = existingTask.dueAtEpochMillis,
                            isRefreshing = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
            } else {
                // Refresh date and time to current system time + 24 hours
                val updatedDeadline = System.currentTimeMillis() + 86_400_000L
                _uiState.update {
                    it.copy(
                        dueAtEpochMillis = updatedDeadline,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update {
            it.copy(
                title = newTitle,
                showTitleError = false
            )
        }
    }

    fun onCourseChange(newCourse: String) {
        _uiState.update { it.copy(course = newCourse) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onDueAtEpochMillisChange(newMillis: Long) {
        _uiState.update { it.copy(dueAtEpochMillis = newMillis) }
    }

    fun saveTask(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState.title.isBlank()) {
            _uiState.update { it.copy(showTitleError = true) }
            return
        }

        viewModelScope.launch {
            val taskToSave = TaskEntity(
                id = taskId ?: 0L,
                title = currentState.title.trim(),
                course = currentState.course.trim(),
                description = currentState.description.trim(),
                dueAtEpochMillis = currentState.dueAtEpochMillis,
                isCompleted = currentState.isCompleted
            )

            if (taskId == null || taskId == 0L) {
                taskRepository.addTask(taskToSave)
            } else {
                taskRepository.updateTask(taskToSave)
            }
            onSuccess()
        }
    }

    fun deleteTask(onSuccess: () -> Unit) {
        if (taskId == null || taskId == 0L) return
        viewModelScope.launch {
            val currentState = _uiState.value
            val taskToDelete = TaskEntity(
                id = taskId,
                title = currentState.title,
                course = currentState.course,
                description = currentState.description,
                dueAtEpochMillis = currentState.dueAtEpochMillis,
                isCompleted = currentState.isCompleted
            )
            taskRepository.deleteTask(taskToDelete)
            onSuccess()
        }
    }

    companion object {
        fun provideFactory(
            taskRepository: TaskRepository,
            taskId: Long?
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TaskFormViewModel(
                    taskRepository = taskRepository,
                    taskId = taskId
                )
            }
        }
    }
}
