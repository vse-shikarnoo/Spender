package com.example.spender.ui.navigation.screens.ridemapScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomBarDestinations
import com.example.spender.ui.navigation.BottomNavGraph
import com.example.spender.ui.navigation.RideMapNavGraph
import com.example.spender.ui.navigation.screens.destinations.ItemScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@BottomNavGraph
@RideMapNavGraph(start = true)
@Destination
@Composable
fun RideMapScreen(
    navigator: DestinationsNavigator
) {
    Scaffold(
        bottomBar = { BottomBar(BottomBarDestinations.RideMap, navigator) },
        content = { RideMapScreenContent(it, navigator) }
    )
}

@Composable
fun RideMapScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                navigator.navigate(ItemScreenDestination)
            }
        ) {
            Text("Ride map screen")
        }
    }
}
