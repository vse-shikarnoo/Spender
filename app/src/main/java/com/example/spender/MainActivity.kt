package com.example.spender

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.spender.destinations.*
import com.example.spender.ui.theme.SpenderTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Balance(BalanceScreenDestination, Icons.Default.Email, R.string.app_name),
    RideMap(RideMapScreenDestination, Icons.Default.Add, R.string.app_name),
    CreateRide(CreateRideScreenDestination, Icons.Default.Create, R.string.app_name),
    Profile(ProfileScreenDestination, Icons.Default.AccountBox, R.string.app_name),
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpenderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // в терминале вызываем ./gradlew kspDebugKotlin если не появляется
                    // импорт какого-либо ScreenDestination
                    DestinationsNavHost(navGraph = NavGraphs.first)
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination: TypedDestination<*> = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation {
        BottomBarDestination.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(destination.direction) {
                        popUpTo(destination.direction.route)
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

///////////////////////////////block_start//////////////////////////////////////
@RootNavGraph(start = true)
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class FirstNavGraph(
    val start: Boolean = false
)

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
    val user_in = true // взятие user_state откуда-нибудь
    when (user_in) {
        true -> {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomBar(navController)
                }
            ) {
                DestinationsNavHost(
                    navController = navController, //!! this is important
                    navGraph = NavGraphs.bottom
                )
            }
        }
        false -> {
            navigator.navigate(FirstScreenDestination)
        }
    }
}

@FirstNavGraph
@Destination
@Composable
fun FirstScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Row() {
                Text("Screen first")
            }
        }
    }
}

@FirstNavGraph
@Destination
@Composable
fun SignUpScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Row() {
                Text("Sign Up")
            }
        }
    }
}

@FirstNavGraph
@Destination
@Composable
fun LogInScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Row() {
                Text("Log In")
            }
        }
    }
}

///////////////////////////////block_end////////////////////////////////////////

///////////////////////////////block_start//////////////////////////////////////
@RootNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class BottomNavGraph(
    val start: Boolean = false
)
///////////////////////////////block_end////////////////////////////////////////

///////////////////////////////block_start//////////////////////////////////////
// Экран с балансом и связанные с ним @Composable
@BottomNavGraph(start = true)
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class BalanceNavGraph(
    val start: Boolean = false
)

@BalanceNavGraph(start = true)
@Destination
@Composable
fun BalanceScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Row() {
                Text("Screen balance")
            }
        }
    }
}

///////////////////////////////block_end////////////////////////////////////////

///////////////////////////////block_start//////////////////////////////////////
// Экран карты с поездками и связанные с ним @Composable
@BottomNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class RideMapNavGraph(
    val start: Boolean = false
)

@RideMapNavGraph(start = true)
@Destination
@Composable
fun RideMapScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Row() {
                Text("Rides on map")
            }
        }
    }
}

///////////////////////////////block_end////////////////////////////////////////

///////////////////////////////block_start//////////////////////////////////////
// Экран создания поездки и связанные с ним @Composable
@BottomNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class CreateRideNavGraph(
    val start: Boolean = false
)

@CreateRideNavGraph(start = true)
@Destination
@Composable
fun CreateRideScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Row() {
                Text("Ride creation")
            }
        }
    }
}

///////////////////////////////block_end////////////////////////////////////////

///////////////////////////////block_start//////////////////////////////////////
// Экран профиля и друзей и связанные с ним @Composable
@BottomNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class ProfileNavGraph(
    val start: Boolean = false
)

@ProfileNavGraph(start = true)
@Destination
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        for (i in 1..10) {
            Row() {
                Text("Profile")
            }
        }
    }
}

///////////////////////////////block_end////////////////////////////////////////