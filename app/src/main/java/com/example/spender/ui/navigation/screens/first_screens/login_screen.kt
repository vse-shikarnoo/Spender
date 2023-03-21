package com.example.spender.ui.navigation.screens.first_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spender.ui.navigation.nav_graphs.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.SignUpScreenDestination
import com.example.spender.ui.navigation.screens.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@FirstNavGraph
@Destination
@Composable
fun LogInScreen(
    navigator: DestinationsNavigator
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val confirm = remember { mutableStateOf(false) }
        Button(
            onClick = {
                confirm.value = true
                navigator.navigate(SignUpScreenDestination)
            },
        ) {
            Text(
                text = "Log in",
                style = MaterialTheme.typography.labelMedium
            )
        }
        if (confirm.value) { // user_state изменился
            navigator.navigate(SplashScreenDestination)
        }
    }
}
