package com.example.spender.ui.navigation.screens.first_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.navigation.nav_graphs.FirstNavGraph
import com.example.spender.ui.navigation.screens.destinations.LogInScreenDestination
import com.example.spender.ui.navigation.screens.destinations.SignUpScreenDestination
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.WhiteBackground
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.theme.Shapes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@FirstNavGraph
@Destination
@Composable
fun FirstScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GreetingGroup()
        ButtonGroup()
    }
}
//добавить navigator в качестве параметра в ButtonGroup
@Preview
@Composable
fun ButtonGroup(){
    val buttonModifier = Modifier
        .fillMaxWidth()
    Column(
        modifier = Modifier
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Button(
            onClick = {},
            buttonModifier,
            shape = Shapes.small,
            colors = ButtonDefaults.buttonColors(backgroundColor = GreenMain,
                contentColor = WhiteBackground)
        ){
            Text("Log In", style = MaterialTheme.typography.button)
        }
        Button(onClick = {},buttonModifier){
            Text("Sign Up", style = MaterialTheme.typography.button)
        }
        Button(onClick = {},buttonModifier){
            Image(
                painterResource(id = R.drawable.google),
                contentDescription = "google icon",
                modifier = Modifier.size(24.dp)
            )
            Text("Sign in with Google",
                Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.button)
        }
    }
}

@Preview
@Composable
fun GreetingGroup(){
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
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
        Text("Travel more, spend less",
            style = MaterialTheme.typography.h5
        )
        Divider(color = GreenLight, modifier = Modifier.padding(24.dp))
    }
}