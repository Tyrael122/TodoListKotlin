package com.makesoftware.todolist.model

data class TodoActivity(
    val id: Int,
    val action: String,
    val isDone: Boolean = false,
    val isActionReadonly: Boolean = true
)
