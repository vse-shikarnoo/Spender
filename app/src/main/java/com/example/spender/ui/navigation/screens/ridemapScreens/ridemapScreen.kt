package com.example.spender.ui.navigation.screens.ridemapScreens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.spender.domain.remotemodel.spend.GoogleMapsSpend
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomBarDestinations
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.viewmodel.SpendViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

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

fun getMarkersCenter(spendList: List<GoogleMapsSpend>): LatLng {
    val centerBuilder = LatLngBounds.builder()
    spendList.forEach { spend ->
        centerBuilder.include(LatLng(spend.latitude, spend.longitude))
    }
    return centerBuilder.build().center
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: androidx.compose.ui.graphics.Color = GreenMain,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            )
        }
    }

}

@Composable
fun RideMapScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
) {
    val spendListResult = spendViewModel.getAllSpendsDataResult.observeAsState()
    var spendList by remember { mutableStateOf(listOf<GoogleMapsSpend>()) }
    var center by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var spendListLoadAnimation by remember { mutableStateOf(true) }

    viewModelResultHandler(
        LocalContext.current,
        spendListResult,
        onSuccess = { spends ->
            spendList = spends
            center = getMarkersCenter(spendList)
            spendListLoadAnimation = false
        },
        msgShow = true
    )

    if (spendList.isNotEmpty()) {
        MyMap(center = center, spendList = spendList)
    } else {
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (spendListLoadAnimation) {
                LoadingAnimation()
            } else {
                Text(text = "No Spends")
            }
        }
    }
}

@Composable
fun MyMap(center: LatLng, spendList: List<GoogleMapsSpend>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        spendList.forEach { spend ->
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
