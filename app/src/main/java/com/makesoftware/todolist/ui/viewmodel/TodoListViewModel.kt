package com.makesoftware.todolist.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.makesoftware.todolist.model.TodoItem

class TodoListViewModel : ViewModel() {
    private val sampleInitialTodoItem =
        TodoItem("Passear com o cachorro da vizinha e não deixar seu coco na rua para não levar multa")

    var todoUiState: TodoUiState by mutableStateOf(
        TodoUiState(
            listOf(
                sampleInitialTodoItem,
                sampleInitialTodoItem,
                sampleInitialTodoItem,
                sampleInitialTodoItem,
                sampleInitialTodoItem,
                sampleInitialTodoItem,
                sampleInitialTodoItem,
                sampleInitialTodoItem
            )
        )
    )
        private set

    private val mutableTodoList: MutableList<TodoItem> = mutableListOf(sampleInitialTodoItem)

    private var indexTodoItemBeingEdited: Int? = null

    fun createTodoItem(action: String) {
        mutableTodoList.add(TodoItem(action = action))

        updateUiStateWithTodoList()
    }

    fun notifyNewTodoItemIsBeingCreated() {
        saveTodoItemEdition()
    }

    fun setTodoItemForEdition(todoItemIndex: Int) {
        saveTodoItemEdition()

        setReadOnlyStateForTodoItemAction(todoItemIndex, false)

        indexTodoItemBeingEdited = todoItemIndex
    }

    fun saveTodoItemEdition() {
        if (indexTodoItemBeingEdited == null) {
            return
        }

        indexTodoItemBeingEdited?.let { setReadOnlyStateForTodoItemAction(it, true) }

        indexTodoItemBeingEdited = null
    }

    fun updateTodoItemAction(todoItemIndex: Int, newAction: String) {
        val updatedTodoItem = todoUiState.todoList[todoItemIndex].copy(
            action = newAction
        )

        mutableTodoList[todoItemIndex] = updatedTodoItem

        updateUiStateWithTodoList()
    }

    fun checkTodoItem(todoItemIndex: Int, isChecked: Boolean) {
        val updatedTodoItem = todoUiState.todoList[todoItemIndex].copy(
            isDone = isChecked
        )

        mutableTodoList[todoItemIndex] = updatedTodoItem
        updateUiStateWithTodoList()
    }

    fun deleteTodoItem(activityIndex: Int) {
        mutableTodoList.removeAt(activityIndex)

        updateUiStateWithTodoList()
    }

    private fun updateUiStateWithTodoList() {
        todoUiState = TodoUiState(mutableTodoList.toList())
    }

    private fun setReadOnlyStateForTodoItemAction(activityIndex: Int, isReadOnly: Boolean) {
        val updatedActivity = todoUiState.todoList[activityIndex].copy(
            isActionReadonly = isReadOnly
        )

        mutableTodoList[activityIndex] = updatedActivity
        updateUiStateWithTodoList()
    }
}
