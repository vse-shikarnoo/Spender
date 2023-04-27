package com.example.spender.ui.navigation.screens.firstScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.ui.navigation.screens.destinations.LogInScreenDestination
import com.example.spender.ui.navigation.screens.destinations.SignUpScreenDestination
import com.example.spender.ui.navigation.screens.helperfunctions.GreetingGroup
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.theme.WhiteBackground
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FirstScreen(
    navigator: DestinationsNavigator,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GreetingGroup()
        ButtonGroup(navigator)
    }
}

@Composable
fun ButtonGroup(navigator: DestinationsNavigator) {
    Column(
        modifier = Modifier
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FirstScreenButtons(navigator = navigator)
    }
}

@Composable
fun FirstScreenButtons(navigator: DestinationsNavigator) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
    FirstScreenButton(
        text = "Log In",
        onClick = { navigator.navigate(LogInScreenDestination) },
        modifier = buttonModifier
    )
    FirstScreenButton(
        text = "Sign Up",
        onClick = { navigator.navigate(SignUpScreenDestination) },
        modifier = buttonModifier
    )
}

@Composable
fun FirstScreenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = GreenMain,
            contentColor = WhiteBackground
        )
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium)
    }
}
