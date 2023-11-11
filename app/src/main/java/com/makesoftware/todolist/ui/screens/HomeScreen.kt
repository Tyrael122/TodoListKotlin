package com.makesoftware.todolist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.makesoftware.todolist.model.TodoActivity
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    todoList: List<TodoActivity>,
    modifier: Modifier = Modifier,
    onActivityCheck: (Int, Boolean) -> Unit,
    onEditRequest: (Int) -> Unit,
    onActivityActionChanged: (Int, String) -> Unit,
    onUpdate: (Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(todoList.size) {
            TodoCard(
                isActivityChecked = todoList[it].isDone,
                activityAction = todoList[it].action,
                activityId = todoList[it].id,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                onCheck = onActivityCheck,
                onEditRequest = onEditRequest,
                onUpdate = onUpdate,
                onActivityActionChanged = onActivityActionChanged,
                isActivityActionReadOnly = todoList[it].isActionReadonly
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoCard(
    isActivityChecked: Boolean,
    activityAction: String,
    activityId: Int,
    modifier: Modifier = Modifier,
    onCheck: (Int, Boolean) -> Unit,
    onEditRequest: (Int) -> Unit,
    onActivityActionChanged: (Int, String) -> Unit,
    isActivityActionReadOnly: Boolean,
    onUpdate: (Int) -> Unit
) {
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            val actionFocusRequester = remember { FocusRequester() }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = isActivityChecked, onCheckedChange = { onCheck(activityId, it) })

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
                IconButton(onClick = { onUpdate(activityId) }) {
                    Icon(Icons.Outlined.Check, contentDescription = null)
                }
            }
        }
    }
}
