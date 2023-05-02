package com.example.spender.ui.navigation.screens.createRideScreens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomBarDestinations
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.navigation.screens.helperfunctions.FriendCard
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.*
import com.example.spender.ui.viewmodel.TripViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CreateRideScreen(
    navigator: DestinationsNavigator,
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel
) {
    Scaffold(
        topBar = { CreateRideTopBar() },
        bottomBar = { BottomBar(BottomBarDestinations.CreateRide, navigator) },
        content = { CreateRideContent(it, userViewModel, tripViewModel, navigator) }
    )
    LaunchedEffect(
        key1 = 1,
        block = {
            userViewModel.getUserFriends()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRideTopBar() {
    TopAppBar(
        title = {
            Text(
                "Create trip",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        },
    )
}

@Composable
fun CreateRideContent(
    paddingValues: PaddingValues,
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel,
    navigator: DestinationsNavigator
) {
    val scrollState = rememberScrollState()
    var tripName by remember { mutableStateOf("") }

    val friends = userViewModel.getUserFriendsDataResult.observeAsState()
    var friendsList by remember { mutableStateOf(listOf<Friend>()) }
    viewModelResultHandler(
        LocalContext.current,
        friends,
        onSuccess = {
            Log.d("CreateTripButton", " friends : $it")
            friendsList = it
        }
    )

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // /
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.trip_image),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            EditTextField(
                text = tripName,
                onTextChanged = { newName -> tripName = newName },
                label = { Text(text = "Trip title") },
                keyboardType = KeyboardType.Text
            )
        }
        AddFriendsList(userViewModel, tripViewModel, tripName, navigator, friendsList)
    }
}

@Composable
fun AddFriendsList(
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel,
    tripName: String,
    navigator: DestinationsNavigator,
    friendsList: List<Friend>
) {
    var addList by if (friendsList.isNotEmpty())
        remember { mutableStateOf(List(friendsList.size) {false}) }
    else
        remember { mutableStateOf(List(0) {false}) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (friendsList.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    "Add friends",
                    style = MaterialTheme.typography.titleMedium
                )
                Image(
                    modifier = Modifier
                        .size(32.dp),
                    painter = painterResource(id = R.drawable.friends),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
            Divider(
                color = GreenLight,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredSizeIn(maxHeight = 280.dp, maxWidth = 400.dp)
            ) {
                items(
                    items = friendsList,
                ) { friend ->
                    FriendCard(
                        friend = friend,
                        button = {
                            Checkbox(
                                checked = addList[friendsList.indexOf(friend)],
                                onCheckedChange = { add ->
                                    val mutableList = addList.toMutableList()
                                    mutableList[friendsList.indexOf(friend)] = add
                                    addList = mutableList.toList()
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = GreenMain,
                                )
                            )
                        }
                    )
                }
            }
        }
        CreateTripButton(
            navigator,
            tripViewModel,
            tripName,
            buildList {
                friendsList.forEachIndexed { index, friend ->
                    if (addList[index])
                        add(friend)
                }
            }
        )
    }
}

@Composable
fun CreateTripButton(
    navigator: DestinationsNavigator,
    tripViewModel: TripViewModel,
    tripName: String,
    addFriendList: List<Friend>
) {
    val createTripResult = tripViewModel.createTripDataResult.observeAsState()
    val createTripMsgShow = tripViewModel.createTripMsgShow.observeAsState()
    Log.d("CreateTripButton", "add friends : $addFriendList")
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                tripViewModel.createTrip(
                    tripName,
                    addFriendList
                )
            },
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            ),
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "Create",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
    viewModelResultHandler(
        LocalContext.current,
        createTripResult,
        onSuccess = {
            if (createTripMsgShow.value == true) {
                navigator.navigate(BalanceScreenDestination)
            }
        },
        restMsgShowState = {
            tripViewModel.doNotShowCreateTripMsg()
        },
        msgShow = createTripMsgShow.value ?: false
    )
}
