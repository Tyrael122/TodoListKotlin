package com.makesoftware.todolist.model

data class TodoActivity(
    val action: String,
    val isDone: Boolean = false,
    val isActionReadonly: Boolean = true
)
