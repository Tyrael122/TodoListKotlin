package com.makesoftware.todolist.model

data class TodoItem(
    val action: String,
    val isDone: Boolean = false,
    val isActionReadonly: Boolean = true
)
