package com.makesoftware.todolist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.makesoftware.todolist.model.TodoItem
import com.makesoftware.todolist.ui.viewmodel.TodoListViewModel

@Composable
fun HomeScreen(
    viewModel: TodoListViewModel,
    todoList: List<TodoItem>,
    modifier: Modifier = Modifier,
    isNewTodoItemBeingCreated: Boolean,
    onSaveNewItem: () -> Unit,
    onCancelNewItem: () -> Unit
) {
    LazyColumn(modifier = modifier.imePadding()) {
        items(todoList.size) {
            TodoCard(
                activityId = it,
                activityAction = todoList[it].action,
                isActivityChecked = todoList[it].isDone,
                isActivityActionReadOnly = todoList[it].isActionReadonly,
                onCheckAsDone = { activityId: Int, isChecked: Boolean ->
                    viewModel.checkTodoItem(activityId, isChecked)
                },
                onEditRequest = { todoItemIndex ->
                    viewModel.setTodoItemForEdition(todoItemIndex)
                },
                onMarkAsEditionCompleted = {
                    viewModel.saveTodoItemEdition()
                },
                onActivityActionChanged = { activityId: Int, newAction: String ->
                    viewModel.updateTodoItemAction(activityId, newAction)
                },
                onDelete = { activityId: Int ->
                    viewModel.deleteTodoItem(activityId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        }

        if (isNewTodoItemBeingCreated) {
            item {
                TemporaryTodoCard(
                    onSave = { todoItemAction ->
                        viewModel.createTodoItem(todoItemAction)

                        onSaveNewItem()
                    },
                    onCancel = {
                        onCancelNewItem()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoCard(
    modifier: Modifier = Modifier,
    activityId: Int,
    activityAction: String,
    isActivityChecked: Boolean,
    actionFocusRequester: FocusRequester = remember { FocusRequester() },
    onCheckAsDone: (Int, Boolean) -> Unit = { _: Int, _: Boolean -> },
    onEditRequest: (Int) -> Unit = {},
    onActivityActionChanged: (Int, String) -> Unit,
    isActivityActionReadOnly: Boolean,
    onMarkAsEditionCompleted: () -> Unit,
    onDelete: (Int) -> Unit = {},
) {
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1F)
            ) {
                Checkbox(checked = isActivityChecked,
                    onCheckedChange = { onCheckAsDone(activityId, it) })

                val keyboardController = LocalSoftwareKeyboardController.current

                BasicTextField(readOnly = isActivityActionReadOnly,
                    value = activityAction,
                    onValueChange = { onActivityActionChanged(activityId, it) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (isActivityChecked) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    modifier = Modifier
                        .focusRequester(actionFocusRequester)
                        .onFocusChanged {
                            keyboardController?.show()
                        })
            }

            if (isActivityActionReadOnly) {
                IconButton(onClick = {
                    onEditRequest(activityId)

                    actionFocusRequester.requestFocus()
                }) {
                    Icon(Icons.Outlined.Create, contentDescription = null)
                }
            } else {
                IconButton(onClick = { onMarkAsEditionCompleted() }) {
                    Icon(Icons.Outlined.Check, contentDescription = null)
                }
            }

            IconButton(onClick = { onDelete(activityId) }) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun TemporaryTodoCard(
    modifier: Modifier = Modifier, onSave: (String) -> Unit, onCancel: () -> Unit
) {
    var todoAction by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TodoCard(
        modifier = modifier,
        activityId = 0,
        activityAction = todoAction,
        isActivityChecked = false,
        onActivityActionChanged = { _: Int, todoItemAction: String ->
            todoAction = todoItemAction
        },
        onMarkAsEditionCompleted = {
            onSave(todoAction)
        },
        onDelete = {
            onCancel()
        },
        isActivityActionReadOnly = false,
        actionFocusRequester = focusRequester
    )
}
