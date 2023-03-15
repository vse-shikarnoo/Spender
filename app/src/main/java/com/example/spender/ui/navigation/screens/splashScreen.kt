package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.example.spender.R
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.nav_graphs.FirstNavGraph
import com.example.spender.ui.navigation.screens.NavGraphs
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
) {
    // Splash screen layout
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.sample_svg_bottom),
            contentDescription = "splash"
        )
    }
    // Тут будет выбор запускаемого экрана в зависимости от того вошел юзер или нет
    val userIn = true // взятие user_state откуда-нибудь
    when (userIn) {
        true -> {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomBar(navController)
                }
            ) {
                DestinationsNavHost(
                    navController = navController, // !! this is important
                    navGraph = NavGraphs.bottom
                )
            }
        }
        false -> {
            navigator.navigate(FirstScreenDestination)
        }
    }
}
