package com.example.studenttaskplanner.ui.taskform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.studenttaskplanner.data.local.TaskEntity
import com.example.studenttaskplanner.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

        onSuccess()
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
