package com.example.spender.ui.navigation.screens.firstScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.navigation.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.LogInScreenDestination
import com.example.spender.ui.navigation.screens.destinations.SignUpScreenDestination
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.theme.WhiteBackground
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@FirstNavGraph
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

// add navigator to ButtonGroup
@Composable
fun ButtonGroup(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
    Column(
        modifier = Modifier
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                // navigator.popBackStack()
                navigator.navigate(LogInScreenDestination)
            },
            buttonModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenMain,
                contentColor = WhiteBackground
            )
        ) {
            Text("Log In", style = MaterialTheme.typography.labelMedium)
        }
        Button(
            onClick = {
                // navigator.popBackStack()
                navigator.navigate(SignUpScreenDestination)
            },
            buttonModifier
        ) {
            Text("Sign Up", style = MaterialTheme.typography.labelMedium)
        }
        Button(
            onClick = {
                Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
            },
            buttonModifier
        ) {
            Image(
                painterResource(id = R.drawable.google),
                contentDescription = "google icon",
                modifier = Modifier.size(24.dp)
            )
            Text(
                "Sign in with Google",
                Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Preview
@Composable
fun GreetingGroup() {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(180.dp),
                painter = painterResource(id = R.drawable.spender_icon),
                contentDescription = "splash",
                contentScale = ContentScale.Fit
            )
        }
        Text(
            "Travel more, spend less",
            style = MaterialTheme.typography.headlineMedium
        )
        Divider(color = GreenLight, modifier = Modifier.padding(24.dp))
    }
}
