package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomNavGraph
import com.example.spender.ui.navigation.screens.NavGraphs
import com.example.spender.ui.viewmodel.AuthViewModel
import com.example.spender.ui.viewmodel.SpendViewModel
import com.example.spender.ui.viewmodel.TripViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@BottomNavGraph
@Destination
@Composable
fun BottomBarScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
        content = {
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.bottom,
                dependenciesContainerBuilder = { //this: DependenciesContainerBuilder<*>
                    // 👇 To tie ActivityViewModel to the activity, making it available to all destinations
                    dependency(hiltViewModel<AuthViewModel>(LocalContext.current.findActivity()!!))
                    dependency(hiltViewModel<UserViewModel>(LocalContext.current.findActivity()!!))
                    dependency(hiltViewModel<TripViewModel>(LocalContext.current.findActivity()!!))
                    dependency(hiltViewModel<SpendViewModel>(LocalContext.current.findActivity()!!))
                }
            )
        }
    )
}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> { null }
}