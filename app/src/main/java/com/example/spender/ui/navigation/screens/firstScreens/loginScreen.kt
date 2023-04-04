package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.spender.R
import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.viewModels.AuthManagerViewModel
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.FirstNavGraph
import com.example.spender.ui.navigation.screens.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph
@Destination
@Composable
fun LogInScreen(
    navigator: DestinationsNavigator,
    authManagerViewModel: AuthManagerViewModel = viewModel()
) {
    val context = LocalContext.current
    val signInResult = authManagerViewModel.signInResult.observeAsState()
    var error by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Button(
            onClick = { authManagerViewModel.signIn(email, password) },
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)
        ) {
            Text("Log In")
        }
    }

    signInResult.value.let { result ->
        when (result) {
            is Result.Success -> {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) {
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.bottom
                    )
                }
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
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        val confirm = remember { mutableStateOf(false) }
//
//        Button(
//            onClick = {
//                // подтверждение регистрации
//                confirm.value = true
//            }
//        ) {
//            Text("Log In")
//        }
//
//        if (confirm.value) { // user_state изменился
//            navigator.navigate(SplashScreenDestination)
//        }
//    }
}
