package com.example.spender.ui.navigation.screens.balanceScreens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.spender.domain.remotemodel.LocalTrip
import com.example.spender.domain.remotemodel.spend.LocalSpend
import com.example.spender.domain.remotemodel.spendmember.LocalSpendMember
import com.example.spender.domain.remotemodel.toTrip
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.LocalFriend
import com.example.spender.domain.remotemodel.user.toFriend
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.navigation.screens.helperfunctions.EmptyListItem
import com.example.spender.ui.navigation.screens.helperfunctions.OverflowListItem
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.GreenLightBackground
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.viewmodel.SpendViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.google.firebase.firestore.GeoPoint
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddSpendingScreen(
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
    trip: LocalTrip
) {
    Scaffold(
        topBar = { AddSpendingScreenTopBar(navigator, trip, spendViewModel) },
    ) {
        AddSpendingScreenContent(
            paddingValues = it,
            navigator = navigator,
            spendViewModel = spendViewModel,
            trip = trip
        )
    }
}

@Composable
fun AddSpendingScreenTopBar(
    navigator: DestinationsNavigator,
    trip: LocalTrip,
    spendViewModel: SpendViewModel,
) {
    val testSpend = LocalSpend(
        name = "ABOBA",
        category = "ABOBA",
        splitMode = "ABOBA",
        amount = 100.0,
        geoPoint = GeoPoint(0.0, 0.0),
        members = buildList {
            trip.members.forEach { friend ->
                this@buildList.add(
                    LocalSpendMember(
                        friend = friend.toFriend(),
                        payment = 0.0,
                        emptyList()
                    )
                )
            }
        }
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = {
                spendViewModel.createSpend(trip.toTrip(), testSpend)
                navigator.popBackStack()
            },
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Go back",
                tint = GreenMain
            )
        }
        IconButton(
            onClick = {
                spendViewModel.createSpend(trip.toTrip(), testSpend)
                navigator.popBackStack()
            },
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Filled.Check,
                contentDescription = "Add spending",
                tint = GreenMain
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSpendingScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
    trip: LocalTrip
) {
    var splitEqualChecked by remember { mutableStateOf(false) }
    var totalSpend by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Box(
                modifier = Modifier.weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                AutoResizedText(
                    text = "Total spend",
                    color = GreenMain,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
                EditTextField(
                    text = totalSpend,
                    onTextChanged = {
                        totalSpend = if (it.isEmpty()) {
                            it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> totalSpend //old value
                                else -> it   //new value
                            }
                        }
                    },
                    label = { Text(text = "Amount") },
                    keyboardType = KeyboardType.Number,
                    fieldNeedToBeHidden = false
                )
            }
        }
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Box(
                modifier = Modifier.weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                AutoResizedText(
                    text = "Split equally",
                    color = GreenMain,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Checkbox(
                modifier = Modifier.weight(0.5f),
                checked = splitEqualChecked,
                onCheckedChange = {
                    splitEqualChecked = it
                }
            )
        }
        FriendsListSpend(friendsLst = buildList {
            trip.members.forEach { friend ->
                this@buildList.add(friend.toFriend())
            }
        })
    }
}

@Composable
fun FriendsListSpend(
    friendsLst: List<Friend>
) {
    var showMore by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ) {
        if (friendsLst.isEmpty()) {
            item { EmptyListItem(text = "No outgoing friends") }
            return@LazyColumn
        }
        if (friendsLst.size <= 3) {
            items(friendsLst) { friend ->
                FriendsListItemSpend(friend = friend)
            }
            return@LazyColumn
        }
        if (!showMore) {
            items(friendsLst.subList(0, 3)) { friend ->
                FriendsListItemSpend(friend = friend)
            }
        } else {
            items(friendsLst) { friend ->
                FriendsListItemSpend(friend = friend)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsListItemSpend(
    friend: Friend,
) {
    var spendAmount by remember { mutableStateOf("") }
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                AutoResizedText(
                    text = friend.nickname,
                    color = GreenMain,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
                EditTextField(
                    text = spendAmount,
                    onTextChanged = {
                        spendAmount = if (it.isEmpty()) {
                            it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> spendAmount //old value
                                else -> it   //new value
                            }
                        }
                    },
                    label = { Text(text = "Amount") },
                    keyboardType = KeyboardType.Number,
                    fieldNeedToBeHidden = false
                )
            }
        }
    }
}

