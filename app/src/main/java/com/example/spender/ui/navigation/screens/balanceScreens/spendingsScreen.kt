package com.example.spender.ui.navigation.screens.balanceScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.domain.remotemodel.LocalTrip
import com.example.spender.domain.remotemodel.spend.RemoteSpend
import com.example.spender.domain.remotemodel.toTrip
import com.example.spender.ui.navigation.screens.destinations.AddSpendingScreenDestination
import com.example.spender.ui.navigation.screens.helperfunctions.EmptyListItem
import com.example.spender.ui.navigation.screens.helperfunctions.OverflowListItem
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.GreenLightBackground
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.theme.RedBalance
import com.example.spender.ui.viewmodel.SpendViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SpendingsScreen(
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
    trip: LocalTrip
) {
    Scaffold(
        topBar = { SpendingsScreenTopBar(trip) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = GreenMain,
                shape = CircleShape,
                onClick = { navigator.navigate(AddSpendingScreenDestination) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Spend")
            }
        }
    ) {
        SpendingsScreenContent(
            paddingValues = it,
            navigator = navigator,
            spendViewModel = spendViewModel,
            trip = trip
        )
    }
    LaunchedEffect(
        key1 = 1,
        block = {
            spendViewModel.getSpends(trip.toTrip())
        }
    )
}

@Composable
fun SpendingsScreenTopBar(
    trip: LocalTrip
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "${trip.name} by ${trip.creator.nickname}"
        )
    }
}

@Composable
fun SpendingsScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
    trip: LocalTrip
) {
    var showMore by remember { mutableStateOf(false) }
    val spends = spendViewModel.getSpendsDataResult.observeAsState()
    var spendsList by remember { mutableStateOf(emptyList<RemoteSpend>()) }
    viewModelResultHandler(
        LocalContext.current,
        spends,
        onSuccess = {
            spendsList = it
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        Text(
            text = "Spends",
        )
        Divider()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            if (spendsList.isEmpty()) {
                item { EmptyListItem("No trips") }
                return@LazyColumn
            }
            if (spendsList.size <= 3) {
                items(spendsList) {
                    SpendingsListItem(navigator, it)
                }
                return@LazyColumn
            }
            if (!showMore) {
                items(spendsList.subList(0, 3)) {
                    SpendingsListItem(navigator, it)
                }
            } else {
                items(spendsList) {
                    SpendingsListItem(navigator, it)
                }
            }
            item {
                OverflowListItem(
                    text = {
                        if (!showMore) {
                            "Show more"
                        } else {
                            "Show less"
                        }
                    },
                    onClick = { showMore = !showMore }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingsListItem(
    navigator: DestinationsNavigator,
    spend: RemoteSpend
) {
    Card(
        onClick = { TODO() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GreenLightBackground)
                .clip(MaterialTheme.shapes.large)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trip_image),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, GreenMain, CircleShape),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier.weight(4f)
            ) {
                Text(spend.name, style = MaterialTheme.typography.titleMedium)
            }
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    colorFilter = ColorFilter.tint(RedBalance)
                )
            }
        }
    }
}
