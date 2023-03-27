package com.example.spender.ui.navigation.screens.createRideScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spender.ui.navigation.CreateRideNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@CreateRideNavGraph
@Destination
@Composable
fun TicketsScreen(
    navigator: DestinationsNavigator
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                // navigator.
            }
        ) {
            Text("Tickets screen")
        }
    }
}
