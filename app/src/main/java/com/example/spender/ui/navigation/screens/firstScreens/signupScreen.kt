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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    val context = LocalContext.current
    val signUpResult = authManagerViewModel.signUpResult.observeAsState()
    val createUserResult = userViewModel.createUserResult.observeAsState()
    var error by remember { mutableStateOf("") }

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
                modifier = Modifier.padding(it).fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // /
                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
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
                    OutlinedTextField(
                        value = nickname,
                        label = { Text(text = "Nickname") },
                        onValueChange = {
                            nickname = it
                        },
                        textStyle = androidx.compose.material3.MaterialTheme.typography.titleMedium,
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
                androidx.compose.material3.Divider(
                    color = GreenLight,
                    modifier = Modifier.padding(24.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        label = { Text(text = "Email") },
                        onValueChange = {
                            email = it
                        },
                        textStyle = androidx.compose.material3.MaterialTheme.typography.titleMedium,
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
                        value = password,
                        label = { Text(text = "Password") },
                        onValueChange = {
                            password = it
                        },
                        textStyle = androidx.compose.material3.MaterialTheme.typography.titleMedium,
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
//        OutlinedTextField(
//            value = text,
//            label = { Text(text = "Phone") },
//            onValueChange = {
//                text = it
//            },
//            textStyle = androidx.compose.material3.MaterialTheme.typography.titleMedium,
//            colors = spenderTextFieldColors(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//            keyboardActions = KeyboardActions(
//                onDone = {
//                    keyboardController?.hide()
//                    focusManager.clearFocus()
//                }
//            ),
//            singleLine = true,
//        )
                }
                // /
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.Button(
                        onClick = {
                            authManagerViewModel.signUp(email, password)
                        },
                        modifier = Modifier.padding(20.dp),
                    ) {
                        Text(
                            text = "Sign Up",
                            style = androidx.compose.material3.MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    )

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
