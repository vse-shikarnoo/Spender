package com.example.spender.ui.navigation.screens.first_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spender.ui.navigation.nav_graphs.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.example.spender.ui.theme.spenderTextFieldColors
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@FirstNavGraph
@Destination
@Composable
fun LogInScreen(
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Welcome back!",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.popBackStack()
                            navigator.navigate(FirstScreenDestination)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Arrow back"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogInContent()
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    val confirm = remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            // проверка входа
                            confirm.value = true
                        },
                        modifier = Modifier.padding(20.dp),
                    ) {
                        Text(
                            text = "Log In",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    if (confirm.value) { // user_state изменился
                        navigator.navigate(FirstScreenDestination)
                    }
                }
            }
        }
    )
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LogInContent(){
    var text by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    GreetingGroup()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        OutlinedTextField(
            value = text,
            label = { Text(text = "Email") },
            onValueChange = {
                text = it
            },
            textStyle = MaterialTheme.typography.titleMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            colors = spenderTextFieldColors(),
            singleLine = true,
        )
        OutlinedTextField(
            value = text,
            label = { Text(text = "Password") },
            onValueChange = {
                text = it
            },
            textStyle = MaterialTheme.typography.titleMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            colors = spenderTextFieldColors(),
            singleLine = true,
        )
    }
}
