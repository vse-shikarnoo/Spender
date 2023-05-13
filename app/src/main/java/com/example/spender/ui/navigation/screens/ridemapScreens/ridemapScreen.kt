package com.example.spender.ui.navigation.screens.ridemapScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.spend.GoogleMapsSpend
import com.example.spender.domain.remotemodel.spend.LocalSpend
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomBarDestinations
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.viewmodel.SpendViewModel
import com.example.spender.ui.viewmodel.TripViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun RideMapScreen(
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel
) {
    Scaffold(
        bottomBar = { BottomBar(BottomBarDestinations.RideMap, navigator) },
        content = { RideMapScreenContent(it, navigator, spendViewModel) }
    )
    LaunchedEffect(
        key1 = true,
        block = {
            spendViewModel.getAllSpends()
        }
    )
}

@Composable
fun RideMapScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
) {
    val spends = spendViewModel.getAllSpendsDataResult.observeAsState()
    var spendsList by remember {
        mutableStateOf(listOf<GoogleMapsSpend>())
    }
    viewModelResultHandler(
        LocalContext.current,
        spends,
        onSuccess = { spendsList = it },
        msgShow = true
    )

    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        spendsList.forEach { spend ->
            MarkerInfoWindowContent(
                state = MarkerState(
                    position = LatLng(spend.latitude, spend.longitude)
                ),
            ) {
                Column {
                    Text(text = spend.name)
                }
            }
        }
    }
}
