package com.example.spender.ui.navigation.screens.first_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.ui.navigation.nav_graphs.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.spenderTextFieldColors
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Spender",
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
                modifier = Modifier.padding(it).fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Content()
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    val confirm = remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            // проверка регистрации
                            confirm.value = true
                        },
                        modifier = Modifier.padding(20.dp),
                    ) {
                        Text(
                            text = "Sign Up",
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
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Content() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            modifier = Modifier
                .size(80.dp),
            painter = painterResource(id = com.example.spender.R.drawable.profile_icon),
            contentDescription = "profile_icon",
            contentScale = ContentScale.Fit
        )
        OutlinedTextField(
            value = text,
            label = { Text(text = "Name") },
            onValueChange = {
                text = it
            },
            textStyle = MaterialTheme.typography.titleMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
    Divider(color = GreenLight, modifier = Modifier.padding(24.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
        OutlinedTextField(
            value = text,
            label = { Text(text = "Phone") },
            onValueChange = {
                text = it
            },
            textStyle = MaterialTheme.typography.titleMedium,
            colors = spenderTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
        )
    }
}
