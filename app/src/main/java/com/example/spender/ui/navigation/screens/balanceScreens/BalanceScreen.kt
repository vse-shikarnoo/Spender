package com.example.spender.ui.navigation.screens.balanceScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
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
import com.example.spender.ui.navigation.screens.helperfunctions.EmptyListItem
import com.example.spender.ui.navigation.screens.helperfunctions.OverflowListItem
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.*
import com.example.spender.ui.viewmodel.TripViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

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
    ) {
        BalanceScreenContent(
            paddingValues = it,
            navigator = navigator,
            tripViewModel = tripViewModel
        )
    }
    LaunchedEffect(
        key1 = 1,
        block = {
            tripViewModel.getAdminTrips()
            tripViewModel.getPassengerTrips()
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun BalanceScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    tripViewModel: TripViewModel
) {
    val adminTrips = tripViewModel.getAdminTripsDataResult.observeAsState()
    var adminTripsLst by remember { mutableStateOf(emptyList<Trip>()) }

    val passengerTrips = tripViewModel.getPassengerTripsDataResult.observeAsState()
    var passengerTripsLst by remember { mutableStateOf(emptyList<Trip>()) }

    val tabs = listOf("Your trips", "Associated trips")
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0, 0f)
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BalanceCard()
        TabRow(
            modifier = Modifier.padding(bottom = 16.dp),
            selectedTabIndex = pagerState.currentPage
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(title)
                            Image(
                                painter = painterResource(id = R.drawable.bag),
                                contentDescription = "TripIcon",
                                contentScale = ContentScale.Fit
                            )
                        }
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index, 0f)
                        }
                    },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            pageCount = tabs.size
        ) { page ->
            when (page) {
                0 -> TripsList(
                    navigator = navigator,
                    tripViewModel = tripViewModel,
                    tripsList = adminTripsLst,
                    text = "Your trips"
                )

                1 -> TripsList(
                    navigator = navigator,
                    tripViewModel = tripViewModel,
                    tripsList = passengerTripsLst,
                    text = "Associated trips"
                )
            }
        }
    }

    viewModelResultHandler(
        LocalContext.current,
        adminTrips,
        onSuccess = {
            adminTripsLst = it
        }
    )
    viewModelResultHandler(
        LocalContext.current,
        passengerTrips,
        onSuccess = {
            passengerTripsLst = it
        }
    )
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
    tripViewModel: TripViewModel,
    tripsList: List<Trip>,
    text: String
) {
    var showMore by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        content = {
            if (tripsList.isEmpty()) {
                item { EmptyListItem("No trips") }
                return@LazyColumn
            }
            if (tripsList.size <= 3) {
                items(tripsList) {
                    TripCard(trip = it, navigator)
                }
                return@LazyColumn
            }
            if (!showMore) {
                items(tripsList.subList(0, 3)) {
                    TripCard(trip = it, navigator)
                }
            } else {
                items(tripsList) {
                    TripCard(trip = it, navigator)
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
    )
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
