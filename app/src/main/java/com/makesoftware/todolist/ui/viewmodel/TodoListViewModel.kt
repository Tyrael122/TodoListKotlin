package com.makesoftware.todolist.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.makesoftware.todolist.model.TodoActivity

class TodoListViewModel : ViewModel() {
    private val sampleInitialActivity = TodoActivity("Passear com o cachorro da vizinha e não deixar seu coco na rua para não levar multa")

    var todoUiState: TodoUiState by mutableStateOf(TodoUiState(listOf(sampleInitialActivity)))
        private set

    private val mutableTodoList: MutableList<TodoActivity> = mutableListOf(sampleInitialActivity)

    fun createActivity(action: String) {
        mutableTodoList.add(TodoActivity(action = action, isActionReadonly = false))

        updateUiStateWithTodoList()
    }

    fun updateActivityAction(activityIndex: Int, newAction: String) {
        val updatedActivity = todoUiState.todoList[activityIndex].copy(
            action = newAction
        )

        mutableTodoList[activityIndex] = updatedActivity

        updateUiStateWithTodoList()
    }

    fun checkActivity(activityIndex: Int, isChecked: Boolean) {
        val updatedActivity = todoUiState.todoList[activityIndex].copy(
            isDone = isChecked
        )

        mutableTodoList[activityIndex] = updatedActivity
        updateUiStateWithTodoList()
    }

    fun deleteActivity(activityIndex: Int) {
        mutableTodoList.removeAt(activityIndex)

        updateUiStateWithTodoList()
    }

    fun toggleReadOnlyStateForActivityAction(activityIndex: Int) {
        val currentState = todoUiState.todoList[activityIndex].isActionReadonly
        val updatedActivity = todoUiState.todoList[activityIndex].copy(
            isActionReadonly = !currentState
        )

        mutableTodoList[activityIndex] = updatedActivity
        updateUiStateWithTodoList()
    }

    private fun updateUiStateWithTodoList() {
        todoUiState = TodoUiState(mutableTodoList.toList())
    }
}
