package com.example.spender.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.destinations.CreateRideScreenDestination
import com.example.spender.ui.navigation.screens.destinations.ProfileScreenDestination
import com.example.spender.ui.navigation.screens.destinations.RideMapScreenDestination
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.GreenLightBackground
import com.example.spender.ui.theme.GreenMain
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestinations(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    @StringRes val label: Int
) {
    Balance(BalanceScreenDestination, R.drawable.icon_balance, R.string.bottom_bar_balance),
    RideMap(RideMapScreenDestination, R.drawable.icon_ride_map, R.string.bottom_bar_map),
    CreateRide(CreateRideScreenDestination, R.drawable.icon_create, R.string.bottom_bar_create),
    Profile(ProfileScreenDestination, R.drawable.icon_profile, R.string.bottom_bar_profile);

    companion object {
        const val backStackMax = 2
        val backStackMap = mutableMapOf(
            Balance to 0,
            RideMap to 0,
            CreateRide to 0,
            Profile to 0
        )
    }
}

@Composable
fun BottomBar(
    currentDestination: BottomBarDestinations,
    navigator: DestinationsNavigator
) {
    BottomNavigation(
        backgroundColor = GreenLightBackground,
        elevation = 1.dp
    ) {
        BottomBarDestinations.values().forEach { destination ->
            BottomNavigationItem(

                selected = currentDestination == destination,
                onClick = { bottomBarOnClick(currentDestination, destination, navigator) },
                icon = {
                    Icon(
                        ImageVector.vectorResource(id = destination.icon),
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = { Text(stringResource(destination.label)) },
                selectedContentColor = GreenMain,
                unselectedContentColor = GreenLight,
            )
        }
    }
}

fun bottomBarOnClick(
    currentDestination: BottomBarDestinations,
    newDestination: BottomBarDestinations,
    navigator: DestinationsNavigator
) {
    if (currentDestination == newDestination) {
        return
    }

    if (equalBackStackMapValue(newDestination)) {
        navigator.popBackStack(route = newDestination.direction.route, inclusive = true)
        resetBackStackMapValue(newDestination)
    }

    navigator.navigate(newDestination.direction)
    incrementBackStackMapValue(newDestination)
}

fun incrementBackStackMapValue(destinations: BottomBarDestinations) {
    BottomBarDestinations.backStackMap[destinations] =
        BottomBarDestinations.backStackMap[destinations]!! + 1
}

fun resetBackStackMapValue(destinations: BottomBarDestinations) {
    BottomBarDestinations.backStackMap[destinations] = 0
}

fun equalBackStackMapValue(destinations: BottomBarDestinations): Boolean {
    return BottomBarDestinations.backStackMap[destinations] == BottomBarDestinations.backStackMax
}