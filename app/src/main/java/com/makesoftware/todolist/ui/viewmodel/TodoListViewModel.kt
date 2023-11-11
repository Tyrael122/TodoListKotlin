package com.makesoftware.todolist.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.makesoftware.todolist.model.TodoActivity

class TodoListViewModel : ViewModel() {
    private val sampleInitialActivity = TodoActivity(0, "Passear com o cachorro")

    var todoUiState: TodoUiState by mutableStateOf(TodoUiState(listOf(sampleInitialActivity)))
        private set

    private val mutableTodoList: MutableList<TodoActivity> = mutableListOf(sampleInitialActivity)

    fun createActivity(action: String) {
        mutableTodoList.add(TodoActivity(getNextId(), action = action, isActionReadonly = false))

        updateUiStateWithTodoList()
    }

    fun updateActivityAction(activityId: Int, newAction: String) {
        val updatedActivity = todoUiState.todoList[activityId].copy(
            action = newAction
        )

        mutableTodoList[activityId] = updatedActivity

        updateUiStateWithTodoList()
    }

    fun checkActivity(activityId: Int, isChecked: Boolean) {
        val updatedActivity = todoUiState.todoList[activityId].copy(
            isDone = isChecked
        )

        mutableTodoList[activityId] = updatedActivity
        updateUiStateWithTodoList()
    }

    fun deleteActivity(activityId: Int) {
        mutableTodoList.removeAt(activityId)

        updateUiStateWithTodoList()
    }

    fun toggleReadOnlyStateForActivityAction(activityId: Int) {
        val currentState = todoUiState.todoList[activityId].isActionReadonly
        val updatedActivity = todoUiState.todoList[activityId].copy(
            isActionReadonly = !currentState
        )

        mutableTodoList[activityId] = updatedActivity
        updateUiStateWithTodoList()
    }

    private fun getNextId(): Int {
        if (mutableTodoList.isEmpty()) {
            return 0
        }

        return mutableTodoList.last().id + 1
    }

    private fun updateUiStateWithTodoList() {
        todoUiState = TodoUiState(mutableTodoList.toList())
    }
}
