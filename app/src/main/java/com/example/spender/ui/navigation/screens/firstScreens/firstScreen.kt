package com.example.spender.ui.navigation.screens.firstScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spender.ui.navigation.nav_graphs.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.LogInScreenDestination
import com.example.spender.ui.navigation.screens.destinations.SignUpScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@FirstNavGraph
@Destination
@Composable
fun FirstScreen(
    navigator: DestinationsNavigator
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                navigator.navigate(SignUpScreenDestination)
            }
        ) {
            Text("Sign Up")
        }
        Button(
            onClick = {
                navigator.navigate(LogInScreenDestination)
            }
        ) {
            Text("Log In")
        }
    }
}
