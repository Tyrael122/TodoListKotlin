package com.makesoftware.todolist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.makesoftware.todolist.R
import com.makesoftware.todolist.ui.screens.HomeScreen
import com.makesoftware.todolist.ui.viewmodel.TodoListViewModel

@Composable
fun TodoListApp(modifier: Modifier = Modifier, viewModel: TodoListViewModel = viewModel()) {
    var isNewTodoItemBeingCreated by remember { mutableStateOf(false) }

    Scaffold(topBar = { TodoListTopBar() }, bottomBar = {
        TodoListBottomBar(onAdd = {
            isNewTodoItemBeingCreated = true
            viewModel.notifyNewTodoItemIsBeingCreated()
        }, onGoToWebClick = {}, onMoreClick = {})
    }) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val uiState = viewModel.todoUiState

            HomeScreen(
                viewModel = viewModel,
                todoList = uiState.todoList,
                isNewTodoItemBeingCreated = isNewTodoItemBeingCreated,
                onSaveNewItem = {
                    isNewTodoItemBeingCreated = false // TODO: Pass this to the ui state
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Light
            )
        },
        modifier = modifier
    )
}

@Composable
fun TodoListBottomBar(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    onGoToWebClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    BottomAppBar(containerColor = Color.Transparent, modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Row {
                IconButton(onClick = onAdd) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                }

                Spacer(Modifier.width(10.dp))

                IconButton(onClick = onGoToWebClick) {
                    Icon(
                        painter = painterResource(R.drawable.chrome_icon), contentDescription = null
                    )
                }
            }

            IconButton(onClick = onMoreClick) {
                Icon(Icons.Outlined.MoreVert, contentDescription = null)
            }
        }
    }
}