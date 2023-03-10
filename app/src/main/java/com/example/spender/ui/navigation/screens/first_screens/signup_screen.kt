package com.example.spender.ui.navigation.screens.first_screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spender.ui.navigation.nav_graphs.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val confirm = remember { mutableStateOf(false) }

        Button(onClick = {
            // подтверждение регистрации
            confirm.value = true
        }) {
            Text("Sign Up")
        }

        if (confirm.value) { // user_state изменился
            navigator.navigate(SplashScreenDestination)
        }
    }
}