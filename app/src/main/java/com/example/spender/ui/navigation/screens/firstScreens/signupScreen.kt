package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spender.R
import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.viewModels.AuthManagerViewModel
import com.example.spender.data.firebase.viewModels.UserViewModel
import com.example.spender.ui.navigation.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.BottomBarScreenDestination
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.spenderTextFieldColors
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    authManagerViewModel: AuthManagerViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val email by userViewModel.email.observeAsState("")
    val password by userViewModel.password.observeAsState("")
    val nickname by userViewModel.nickname.observeAsState("")

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    Text(
                        "Spender",
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
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // /
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.images),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                    SignUpTextField(
                        text = nickname,
                        onTextChanged = {userViewModel.updateNickname(it)},
                        label = { Text(text = "Nickname") },
                        keyboardType = KeyboardType.Text
                    )
                }
                androidx.compose.material3.Divider(
                    color = GreenLight,
                    modifier = Modifier.padding(24.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SignUpTextField(
                        text = email,
                        onTextChanged = {userViewModel.updateEmail(it)},
                        label = { Text(text = "Email") },
                        keyboardType = KeyboardType.Email
                    )
                    SignUpTextField(
                        text = password,
                        onTextChanged = {userViewModel.updatePassword(it)},
                        label = { Text(text = "Password") },
                        keyboardType = KeyboardType.Password
                    )
                }
                SignUpButton(
                    userViewModel = userViewModel,
                    authViewModel = authManagerViewModel,
                    navigator
                )
            }
        }
    )
}
@Composable
fun SignUpButton(
    userViewModel: UserViewModel,
    authViewModel: AuthManagerViewModel,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val signUpResult = authViewModel.signUpResult.observeAsState()
    val createUserResult = userViewModel.createUserResult.observeAsState()
    val email by userViewModel.email.observeAsState("")
    val password by userViewModel.password.observeAsState("")
    val nickname by userViewModel.nickname.observeAsState("")
    var error by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        androidx.compose.material3.Button(
            onClick = {
                authViewModel.signUp(email, password)
            },
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "Sign Up",
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium
            )
        }
    }
    signUpResult.value.let { result ->
        when (result) {
            is Result.Success -> {
                userViewModel.createUser(result.data.uid, nickname)
            }
            is Result.Error -> {
                if (error != result.exception) {
                    error = result.exception
                    Toast.makeText(
                        context,
                        result.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {}
        }
    }

    createUserResult.value.let { result ->
        when (result) {
            is Result.Success -> {
                navigator.popBackStack(FirstScreenDestination, true)
                navigator.navigate(BottomBarScreenDestination)
            }
            is Result.Error -> {
                if (error != result.exception) {
                    error = result.exception
                    Toast.makeText(
                        context,
                        result.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {}
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    label: @Composable () -> Unit,
    keyboardType: KeyboardType
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text,
        label = label,
        onValueChange = onTextChanged,
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
