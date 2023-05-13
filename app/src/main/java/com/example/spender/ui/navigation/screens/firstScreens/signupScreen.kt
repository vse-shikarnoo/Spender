package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.spenderTextFieldColors
import com.example.spender.ui.viewmodel.AuthViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = { SignUpTopBar(navigator) },
        content = { SignUpContent(it, authViewModel, navigator) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTopBar(
    navigator: DestinationsNavigator
) {
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
}

@Composable
fun SignUpContent(
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel,
    navigator: DestinationsNavigator
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            EditTextField(
                text = nickname,
                onTextChanged = { nickname = it },
                label = { Text(text = "Nickname") },
                keyboardType = KeyboardType.Text,
                false,
            )
        }
        androidx.compose.material3.Divider(
            color = GreenLight,
            modifier = Modifier.padding(24.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EditTextField(
                text = email,
                onTextChanged = { email = it },
                label = { Text(text = "Email") },
                keyboardType = KeyboardType.Email,
                false,
            )
            EditTextField(
                text = password,
                onTextChanged = { password = it },
                label = { Text(text = "Password") },
                keyboardType = KeyboardType.Password,
                true,
            )
        }
        SignUpButton(email, password, nickname, authViewModel, navigator)
    }
}

@Composable
fun SignUpButton(
    email: String,
    password: String,
    nickname: String,
    authViewModel: AuthViewModel,
    navigator: DestinationsNavigator
) {
    val signUpResult = authViewModel.signUpDataResult.observeAsState()
    val signUpMsgShow = authViewModel.signUpMsgShow.observeAsState()

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        androidx.compose.material3.Button(
            onClick = {
                authViewModel.signUp(email, password, nickname)
            },
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "Sign Up",
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium
            )
        }
    }
    viewModelResultHandler(
        LocalContext.current,
        signUpResult,
        onSuccess = {
            authViewModel.signUp(email, password, nickname)
            navigator.navigate(BalanceScreenDestination)
        },
        restMsgShowState = {
            authViewModel.doNotShowSignUpMsg()
        },
        msgShow = signUpMsgShow.value ?: false
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    label: @Composable () -> Unit,
    keyboardType: KeyboardType,
    fieldNeedToBeHidden: Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(true) }

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
        visualTransformation = if (!fieldNeedToBeHidden || passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"
            if (fieldNeedToBeHidden){
                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            }
        }
    )
}
