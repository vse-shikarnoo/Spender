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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.spend.Spend
import com.example.spender.domain.remotemodel.toTrip
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.ui.navigation.screens.destinations.AddSpendingScreenDestination
import com.example.spender.ui.navigation.screens.helperfunctions.EmptyListItem
import com.example.spender.ui.navigation.screens.helperfunctions.FriendCard
import com.example.spender.ui.navigation.screens.helperfunctions.OverflowListItem
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.navigation.screens.profileScreens.FriendsList
import com.example.spender.ui.navigation.screens.profileScreens.OutgoingFriendsListItem
import com.example.spender.ui.theme.GreenLightBackground
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.viewmodel.SpendViewModel
import com.example.spender.ui.viewmodel.TripViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.google.firebase.firestore.GeoPoint
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddSpendingScreen(
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
    userViewModel: UserViewModel
) {
    Scaffold(
        topBar = { AddSpendingScreenTopBar() },
    ) {
        AddSpendingScreenTopBarContent(
            paddingValues = it,
            navigator = navigator,
            spendViewModel = spendViewModel,
            userViewModel = userViewModel,
        )
    }
    LaunchedEffect(
        key1 = true,
        block = {
            userViewModel.getUserFriends()
        }
    )
}

@Composable
fun AddSpendingScreenTopBar() {
    Image(
        painter = painterResource(id = ),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(2.dp, GreenMain, CircleShape),
        contentDescription = null,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSpendingScreenTopBarContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
    userViewModel: UserViewModel
) {
    var splitEqualChecked by remember { mutableStateOf(false) }
    var totalSpend by remember { mutableStateOf("") }
//    if (splitEqualChecked) {
//        TODO()
//    } else {
//        TODO()
//    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {
        Row(
            modifier = Modifier.padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(0.75f),
                text = "total spend"
            )
            TextField(
                modifier = Modifier.weight(0.25f),
                value = totalSpend,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    totalSpend = if (it.isEmpty()){
                        it
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> totalSpend //old value
                            else -> it   //new value
                        }
                    }
                },
                placeholder = {
                    Text(text = "Amount")
                },
            )
        }
        Row {
            Text(
                modifier = Modifier.weight(0.75f),
                text = "split equally"
            )
            Checkbox(
                modifier = Modifier.weight(0.25f),
                checked = splitEqualChecked,
                onCheckedChange = {
                    splitEqualChecked = it
                }
            )
        }
        FriendsListSpend(userViewModel = userViewModel)
    }
}

@Composable
fun FriendsListSpend(
    userViewModel: UserViewModel,
) {
    val friends = userViewModel.getUserFriendsDataResult.observeAsState()
    var friendsLst by remember { mutableStateOf(listOf<Friend>()) }
    viewModelResultHandler(LocalContext.current, friends, { friendsLst = it })

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
                FriendsListItemSpend(friend = friend, userViewModel = userViewModel)
            }
            return@LazyColumn
        }
        if (!showMore) {
            items(friendsLst.subList(0, 3)) { friend ->
                FriendsListItemSpend(friend = friend, userViewModel = userViewModel)
            }
        } else {
            items(friendsLst) { friend ->
                FriendsListItemSpend(friend = friend, userViewModel = userViewModel)
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
    userViewModel: UserViewModel
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
            Text(
                modifier = Modifier.weight(0.75f),
                text = friend.nickname
            )
            TextField(
                modifier = Modifier.weight(0.25f),
                value = spendAmount,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    spendAmount = if (it.isEmpty()){
                        it
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> spendAmount //old value
                            else -> it   //new value
                        }
                    }
                },
                placeholder = {
                    Text(text = "Amount")
                },
            )
        }
    }
}

