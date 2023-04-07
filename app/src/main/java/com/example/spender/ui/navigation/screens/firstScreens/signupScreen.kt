package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.viewModels.AuthManagerViewModel
import com.example.spender.data.firebase.viewModels.UserViewModel
import com.example.spender.ui.navigation.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.*
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.spenderTextFieldColors
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class SignUpStates {
    INITIAL_STATE,
    NICKNAME_STATE,
    EMAIL_PASSWORD_STATE,
}

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
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    var signUpStates by remember { mutableStateOf(SignUpStates.INITIAL_STATE) }

    val checkNicknameResult = userViewModel.checkNicknameFirebaseCallResult.observeAsState()
    val signUpResult = authManagerViewModel.signUpFirebaseCallResult.observeAsState()
    val createUserResult = userViewModel.createUserFirebaseCallResult.observeAsState()

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
                //
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.Button(
                        onClick = {
                            userViewModel.checkNickname(nickname)
                            signUpStates = SignUpStates.NICKNAME_STATE
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

    checkNicknameResult.value.let { result ->
        when (result) {
            is FirebaseCallResult.Success -> {
                if (signUpStates == SignUpStates.NICKNAME_STATE) {
                    if (result.data) {
                        authManagerViewModel.signUp(email, password)
                        signUpStates = SignUpStates.EMAIL_PASSWORD_STATE
                    } else {
                        Toast.makeText(
                            context,
                            "Nickname is taken",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            is FirebaseCallResult.Error -> {
                if (signUpStates == SignUpStates.NICKNAME_STATE) {
                    Toast.makeText(
                        context,
                        result.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                signUpStates = SignUpStates.INITIAL_STATE
            }
            else -> {}
        }
    }

    signUpResult.value.let { result ->
        when (result) {
            is FirebaseCallResult.Success -> {
                if (signUpStates == SignUpStates.EMAIL_PASSWORD_STATE) {
                    userViewModel.createUser(result.data.uid, nickname)
                }
            }
            is FirebaseCallResult.Error -> {
                if (signUpStates == SignUpStates.EMAIL_PASSWORD_STATE) {
                    Toast.makeText(
                        context,
                        result.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                signUpStates = SignUpStates.INITIAL_STATE
            }
            else -> {}
        }
    }

    createUserResult.value.let { result ->
        when (result) {
            is FirebaseCallResult.Success -> {
                navigator.popBackStack(FirstScreenDestination, true)
                navigator.navigate(BottomBarScreenDestination)
            }
            is FirebaseCallResult.Error -> {
                if (signUpStates == SignUpStates.EMAIL_PASSWORD_STATE) {
                    Toast.makeText(
                        context,
                        result.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                signUpStates = SignUpStates.INITIAL_STATE
            }
            else -> {}
        }
    }
}
