package com.example.spender.ui.navigation.screens.firstScreens

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.FirstNavGraph
import com.example.spender.ui.navigation.screens.NavGraphs
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.example.spender.ui.theme.WhiteBackground
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
){
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )
    LaunchedEffect(key1 = true){
        startAnimation = true
        delay(4000)
        navigator.popBackStack()
        navigator.navigate(FirstScreenDestination)
    }
    Splash(alpha = alphaAnim.value)

}
@Composable
fun Splash(alpha: Float){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.size(60.dp))
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .alpha(alpha)
                    .size(180.dp),
                painter = painterResource(id = R.drawable.spender_icon),
                contentDescription = "splash",
                contentScale = ContentScale.Fit
            )
        }
        Text(
            "Spender",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
/*@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
) {
    // Splash screen layout

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
}*/
