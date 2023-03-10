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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.example.spender.destinations.*
import com.example.spender.ui.theme.SpenderTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import androidx.navigation.compose.rememberNavController as rememberNavController

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
    navController: NavController,
) {
    val currentDestination: TypedDestination<*> = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation {
        BottomBarDestination.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(destination.direction)
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

// C1 - Splash Screen
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

// C2 - Стартовый экран
@FirstNavGraph
@Destination
@Composable
fun FirstScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..5) {
            Button(onClick = {
                navigator.navigate(SignUpScreenDestination)
            }) {
                Text("Sign Up")
            }
            Button(onClick = {
                navigator.navigate(LogInScreenDestination)
            }) {
                Text("Log In")
            }
        }
    }
}

// С3 - Экран регистрации
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val confirm = remember { mutableStateOf(false) }

        for (i in 1..10) {
            Button(onClick = {
                // подтверждение регистрации
                confirm.value = true
            }) {
                Text("Sign Up")
            }
        }

        if (confirm.value) { // user_state изменился
            navigator.navigate(SplashScreenDestination)
        }
    }
}

// С4 - Экран входа
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FirstNavGraph
@Destination
@Composable
fun LogInScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val confirm = remember { mutableStateOf(false) }

        for (i in 1..10) {
            Button(onClick = {
                // подтверждение регистрации
                confirm.value = true
            }) {
                Text("Log In")
            }
        }

        if (confirm.value) { // user_state изменился
            navigator.navigate(SplashScreenDestination)
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

// С5 - Экран с балансом
@BalanceNavGraph(start = true)
@Destination
@Composable
fun BalanceScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Button(onClick = {
                navigator.navigate(SpendingsScreenDestination)
            }) {
                Text("Balance screen")
            }
        }
    }
}

// С6 - Экран расходов в поездке
@BalanceNavGraph
@Destination
@Composable
fun SpendingsScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Button(onClick = {
                navigator.navigate(AddSpendingScreenDestination)
            }) {
                Text("Spendings screen")
            }
        }
    }
}

// С7 - Экран добавление траты
@BalanceNavGraph
@Destination
@Composable
fun AddSpendingScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Button(onClick = {
                //navigator.
            }) {
                Text("Add screen")
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

// С10 - Экран карты с поездками
@RideMapNavGraph(start = true)
@Destination
@Composable
fun RideMapScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Button(onClick = {
                navigator.navigate(ItemScreenDestination)
            }) {
                Text("Ride map screen")
            }
        }
    }
}

// С9 - Экран просмотра карточки места
@RideMapNavGraph
@Destination
@Composable
fun ItemScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Button(onClick = {
                //navigator.
            }) {
                Text("Item screen")
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

// С12 - Экран создания поездки
@CreateRideNavGraph(start = true)
@Destination
@Composable
fun CreateRideScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Button(onClick = {
                navigator.navigate(TicketsScreenDestination)
            }) {
                Text("Create ride screen")
            }
        }
    }
}

// С11 - Экран поиска билетов
@CreateRideNavGraph
@Destination
@Composable
fun TicketsScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in 1..10) {
            Button(onClick = {
                //navigator.
            }) {
                Text("Tickets screen")
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

// С8 - Экран профиля и друзей
@ProfileNavGraph(start = true)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        for (i in 1..10) {
            Button(onClick = {
                //navigator.
            }) {
                Text("Profile screen")
            }
        }
    }
}

///////////////////////////////block_end////////////////////////////////////////