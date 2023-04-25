package com.example.spender.ui.navigation.screens.createRideScreens

import android.widget.Toast
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
import androidx.compose.runtime.State
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
import com.example.spender.data.DataResult
import com.example.spender.domain.model.user.Friend
import com.example.spender.ui.navigation.BottomNavGraph
import com.example.spender.ui.navigation.CreateRideNavGraph
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.theme.*
import com.example.spender.ui.viewmodel.TripViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@BottomNavGraph
@CreateRideNavGraph(start = true)
@Destination
@Composable
fun CreateRideScreen(
    navigator: DestinationsNavigator,
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel
) {
    var tripName by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
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
        },
        content = { paddingValues ->
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
                AddFriendsList(userViewModel, tripViewModel, tripName, navigator)
            }
        }
    )
    LaunchedEffect(key1 = 1, block = {
        userViewModel.getUserFriends()
    })
}

@Composable
fun AddFriendsList(
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel,
    tripName: String,
    navigator: DestinationsNavigator
) {
    val friends = userViewModel.getUserFriendsDataResult.observeAsState()
    val friendsLst = getFriends(friends)
    val addedFriends = mutableListOf<Friend>()
    var addFriend by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                items = friendsLst,
            ) {
                FriendCard(
                    friend = it,
                    button = {
                        Checkbox(
                            checked = addFriend,
                            onCheckedChange = { add ->
                                addFriend = add
                                if (addFriend) {
                                    addedFriends.add(it)
                                } else {
                                    addedFriends.remove(it)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = GreenMain,
                            )
                        )
                    }
                )
            }
        }
        if (friendsLst.isEmpty()) {
            Divider(
                color = GreenLight,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
        CreateTripButton(navigator, tripViewModel, tripName, addedFriends)
    }
}

fun getFriends(friends: State<DataResult<List<Friend>>?>): List<Friend> {
    if (friends.value == null) {
        return emptyList<Friend>()
    }
    if ((friends.value!!) is DataResult.Error) {
        return emptyList<Friend>()
    }
    return (friends.value!! as DataResult.Success).data
}

@Composable
fun FriendCard(
    friend: Friend,
    button: @Composable () -> Unit,
) {
    Card(
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
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
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
                Text(
                    friend.nickname,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            button()
        }
    }
}

@Composable
fun CreateTripButton(
    navigator: DestinationsNavigator,
    tripViewModel: TripViewModel,
    tripName: String,
    friends: List<Friend>
) {
    val context = LocalContext.current
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    val createTripResult = tripViewModel.createTripDataResult.observeAsState()
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                tripViewModel.createTrip(tripName, friends)
                error = ""
                success = true
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
    createTripResult.value?.let { dataResult ->
        when (dataResult) {
            is DataResult.Success -> {
                if (success) {
                    navigator.navigate(BalanceScreenDestination)
                }
                success = false
            }

            is DataResult.Error -> {
                if (error != dataResult.exception) {
                    Toast.makeText(
                        context,
                        "Error: ${dataResult.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                error = dataResult.exception
            }
        }
    }
}
