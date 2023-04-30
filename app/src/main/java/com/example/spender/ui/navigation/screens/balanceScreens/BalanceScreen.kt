package com.example.spender.ui.navigation.screens.balanceScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import com.example.spender.R
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomBarDestinations
import com.example.spender.ui.navigation.screens.destinations.SpendingsScreenDestination
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.*
import com.example.spender.ui.viewmodel.TripViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun BalanceScreen(
    navigator: DestinationsNavigator,
    tripViewModel: TripViewModel
) {
    Scaffold(
        topBar = { BalanceScreenTopBar() },
        bottomBar = { BottomBar(BottomBarDestinations.Balance, navigator) },
        content = { BalanceScreenContent(paddingValues = it, navigator, tripViewModel) }
    )
    LaunchedEffect(
        key1 = 1,
        block = {
            tripViewModel.getAdminTrips()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreenTopBar() {
    TopAppBar(
        title = {
            Text(
                "Balance",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        },
    )
}

@Composable
fun BalanceScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    tripViewModel: TripViewModel
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BalanceCard()
        TripsList(
            navigator,
            tripViewModel
        )
    }
}

@Composable
fun BalanceCard() {
    Card(
        modifier = Modifier.padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(
                    GreenLightBackground
                )
                .padding(16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.images),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, GreenMain, CircleShape),
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OweCard(text = "You owe " + "1234")
                OweCard(text = "You are owed " + "1234")
            }
        }
    }
}

@Composable
fun OweCard(text: String) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(colors = listOf(GreenMain, GreenMain)),
                shape = MaterialTheme.shapes.medium,
                alpha = 0.1f,
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = GreenMain,
                style = MaterialTheme.typography.labelMedium
            )
            Image(
                modifier = Modifier
                    .size(12.dp),
                painter = painterResource(id = R.drawable.ruble),
                contentDescription = null,
                colorFilter = ColorFilter.tint(GreenMain)
            )
        }
    }
}

@Composable
fun TripsList(
    navigator: DestinationsNavigator,
    tripViewModel: TripViewModel
) {
    val trips = tripViewModel.getAdminTripsDataResult.observeAsState()
    var tripsLst by remember { mutableStateOf(emptyList<Trip>()) }
    var showMore by remember { mutableStateOf(false) }
    viewModelResultHandler(LocalContext.current, trips, { tripsLst = it })

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Your Trips", style = MaterialTheme.typography.titleMedium)
            Image(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(id = R.drawable.bag),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxHeight(),
            content = {
                tripListLazyColumnItems(showMore, this, tripsLst, navigator) {
                    showMore = !showMore
                }
            }
        )
    }
}

fun tripListLazyColumnItems(
    showMore: Boolean,
    lazyListScope: LazyListScope,
    tripsLst: List<Trip>,
    navigator: DestinationsNavigator,
    onClick: () -> Unit
) {
    if (tripsLst.isEmpty()) {
        lazyListScope.item { EmptyTripItem() }
        return
    }
    if (tripsLst.size <= 3) {
        lazyListScope.items(tripsLst) {
            TripCard(trip = it, navigator)
        }
        return
    }
    if (!showMore) {
        lazyListScope.items(tripsLst.subList(0, 3)) {
            TripCard(trip = it, navigator)
        }
    } else {
        lazyListScope.items(tripsLst) {
            TripCard(trip = it, navigator)
        }
    }
    lazyListScope.item {
        OverflowTripsItem(
            text = {
                if (!showMore) {
                    "Show more"
                } else {
                    "Show less"
                }
            },
            onClick = onClick
        )
    }
}

@Composable
fun EmptyTripItem() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text("No trips", color = GreenMain)
        }
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
    }
}

@Composable
fun OverflowTripsItem(
    text: () -> String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                ),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLightBackground,
                    contentColor = GreenMain
                ),
            ) {
                AutoResizedText(text = text.invoke(), color = GreenMain)
            }
        }
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
    }
}

@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    color: Color = style.color
) {
    var resizedTextStyle by remember { mutableStateOf(style) }
    var shouldDraw by remember { mutableStateOf(false) }
    val defaultFontSize = MaterialTheme.typography.labelMedium.fontSize
    Text(
        text = text,
        color = color,
        modifier = Modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripCard(trip: Trip, navigator: DestinationsNavigator) {
    Card(
        onClick = { navigator.navigate(SpendingsScreenDestination) },
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
                Text(trip.name, style = MaterialTheme.typography.titleMedium)
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
