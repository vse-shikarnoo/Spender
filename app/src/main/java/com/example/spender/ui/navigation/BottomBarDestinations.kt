package com.example.spender.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.GreenLightBackground
import com.example.spender.ui.theme.GreenMain
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestinations(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    @StringRes val label: Int
) {
    Balance(BalanceScreenDestination, R.drawable.icon1, R.string.app_name),
    RideMap(RideMapScreenDestination, R.drawable.icon2, R.string.app_name),
    CreateRide(CreateRideScreenDestination, R.drawable.icon3, R.string.app_name),
    Profile(ProfileScreenDestination, R.drawable.icon4, R.string.app_name),
}

@Composable
fun BottomBar(
    navController: NavController,
) {
    val currentDestination: TypedDestination<*> = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation(
        backgroundColor = GreenLightBackground,
        elevation = 1.dp
    ) {
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
                        ImageVector.vectorResource(id = destination.icon),
                        contentDescription = stringResource(destination.label)
                    )
                },
                // label = { Text(stringResource(destination.label)) },
                selectedContentColor = GreenMain,
                unselectedContentColor = GreenLight,
            )
        }
    }
}
