package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.example.spender.ui.navigation.screens.helperfunctions.GreetingGroup
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.spenderTextFieldColors
import com.example.spender.ui.viewmodel.AuthViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun LogInScreen(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            LoginTopBar(navigator)
        },
        content = { paddingValues ->
            LoginScreenContent(navigator, paddingValues, authViewModel)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopBar(
    navigator: DestinationsNavigator
) {
    androidx.compose.material3.TopAppBar(
        title = {
            Text(
                "Welcome back!",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            androidx.compose.material3.IconButton(
                onClick = {
                    navigator.popBackStack()
                    navigator.navigate(FirstScreenDestination)
                }
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow back"
                )
            }
        }
    )
}

@Composable
fun LoginScreenContent(
    navigator: DestinationsNavigator,
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GreetingGroup()
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginOutlinedTextField(
                currentValue = email,
                keyboardType = KeyboardType.Email,
                onValueChange = { email = it },
            )
            LoginOutlinedTextField(
                currentValue = password,
                keyboardType = KeyboardType.Password,
                onValueChange = { password = it },
            )
            LoginButton(navigator, authViewModel, email, password)
        }
    }
}

@Composable
fun LoginButton(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel,
    email: String,
    password: String
) {
    val signInResult = authViewModel.signInDataResult.observeAsState()

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        androidx.compose.material3.Button(
            onClick = {
                authViewModel.signIn(email, password)
            },
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "Log In",
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium
            )
        }
    }

    viewModelResultHandler(
        LocalContext.current,
        result = signInResult,
        onSuccess = {
            navigator.popBackStack(FirstScreenDestination, true)
            navigator.navigate(BalanceScreenDestination)
        },
        restMsgShowState = {
            authViewModel.doNotShowSignInMsg()
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginOutlinedTextField(
    currentValue: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = currentValue,
        label = {
            Text(
                text = keyboardType.toString()
                    .replaceRange(0, 1, keyboardType.toString()[0].uppercase())
            )
        },
        onValueChange = onValueChange,
        textStyle = androidx.compose.material3.MaterialTheme.typography.titleMedium,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
