package com.example.spender.ui.navigation.screens.firstScreens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomNavGraph
import com.example.spender.ui.navigation.screens.NavGraphs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@BottomNavGraph
@Destination
@Composable
fun BottomBarScreen(
    navigator: DestinationsNavigator
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        it.calculateBottomPadding()
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.bottom
        )
    }
}
