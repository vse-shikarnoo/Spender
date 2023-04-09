package com.example.spender.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.spender.R
import com.example.spender.ui.navigation.screens.NavGraphs
import com.example.spender.ui.navigation.screens.appCurrentDestinationAsState
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.destinations.CreateRideScreenDestination
import com.example.spender.ui.navigation.screens.destinations.ProfileScreenDestination
import com.example.spender.ui.navigation.screens.destinations.RideMapScreenDestination
import com.example.spender.ui.navigation.screens.destinations.TypedDestination
import com.example.spender.ui.navigation.screens.startAppDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestinations(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Balance(BalanceScreenDestination, Icons.Default.Email, R.string.app_name),
    RideMap(RideMapScreenDestination, Icons.Default.Add, R.string.app_name),
    CreateRide(CreateRideScreenDestination, Icons.Default.Create, R.string.app_name),
    Profile(ProfileScreenDestination, Icons.Default.AccountBox, R.string.app_name),
}

@Composable
fun BottomBar(
    navController: NavController,
) {
    val currentDestination: TypedDestination<*> = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation {
        BottomBarDestinations.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    if (currentDestination != destination.direction) {
                        navController.popBackStack(destination.direction.route, true)

                        navController.navigate(destination.direction)
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
