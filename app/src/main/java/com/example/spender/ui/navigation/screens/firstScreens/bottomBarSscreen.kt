package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomNavGraph
import com.example.spender.ui.navigation.screens.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
        },
        content = {
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.bottom
            )
        }
    )
}
