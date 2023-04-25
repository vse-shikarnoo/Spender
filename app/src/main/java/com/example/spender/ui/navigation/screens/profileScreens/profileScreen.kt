package com.example.spender.ui.navigation.screens.profileScreens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.spender.R
import com.example.spender.data.DataResult
import com.example.spender.domain.model.user.Friend
import com.example.spender.ui.navigation.BottomNavGraph
import com.example.spender.ui.navigation.ProfileNavGraph
import com.example.spender.ui.navigation.screens.createRideScreens.FriendCard
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.theme.WhiteBackground
import com.example.spender.ui.viewmodel.AuthViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@BottomNavGraph
@ProfileNavGraph(start = true)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    var signOut by remember { mutableStateOf(false) }
    val userNicknameResult = userViewModel.getUserNicknameDataResult.observeAsState()
    val userNickname = getUserName(userNicknameResult)
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        userNickname,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            authViewModel.signOut()
                            signOut = true
                        }
                    ) {
                        Icon(Icons.Filled.ExitToApp, null, tint = GreenMain)
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // /
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.images),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, GreenMain, CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
                FriendsList(userViewModel, navigator)
            }
        }
    )
    if (signOut) {
        val activity = (LocalContext.current as Activity)
        val intent: Intent = activity.intent
        activity.finish()
        ContextCompat.startActivity(activity, intent, intent.extras)
    }
    LaunchedEffect(key1 = 1, block = {
        userViewModel.getUserFriends()
        userViewModel.getUserIncomingFriends()
        userViewModel.getUserOutgoingFriends()
        userViewModel.getUserNickname()
    })
}

fun getUserName(nickname: State<DataResult<String>?>): String {
    if (nickname.value == null) {
        return "Loading..."
    }
    if (nickname.value!! is DataResult.Error) {
        return "Loading..."
    }
    return (nickname.value!! as DataResult.Success).data
}

@Composable
fun FriendsList(
    userViewModel: UserViewModel,
    navigator: DestinationsNavigator
) {
    val friends = userViewModel.getUserFriendsDataResult.observeAsState()
    val incomingFriends = userViewModel.getUserIncomingFriendsDataResult.observeAsState()
    val outgoingFriends = userViewModel.getUserOutgoingFriendsDataResult.observeAsState()

    val friendsLst = getFriends(friends)
    val incomingFriendsLst = getFriends(incomingFriends)
    val outgoingFriendsLst = getFriends(outgoingFriends)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FriendsListTemplate("Your friends", friendsLst, userViewModel)
        FriendsListTemplate("Incoming friend requests", incomingFriendsLst, userViewModel)
        FriendsListTemplate("Outgoing friend requests", outgoingFriendsLst, userViewModel)
        AddFriendGroup(userViewModel)
    }
}

@Composable
fun FriendsListTemplate(
    text: String,
    friendsLst: List<Friend>,
    userViewModel: UserViewModel
) {
    val removeUserFriend = userViewModel.removeUserFriendDataResult.observeAsState()
    var errorFriend by remember { mutableStateOf("") }
    var successFriend by remember { mutableStateOf(false) }

    val removeUserIncomingFriend = userViewModel.removeUserIncomingFriendDataResult.observeAsState()
    var errorIncomingFriend by remember { mutableStateOf("") }
    var successIncomingFriend by remember { mutableStateOf(false) }

    val removeUserOutgoingFriend = userViewModel.removeUserOutgoingFriendDataResult.observeAsState()
    var errorOutgoingFriend by remember { mutableStateOf("") }
    var successOutgoingFriend by remember { mutableStateOf(false) }

    val addUserIncomingFriend = userViewModel.addUserIncomingFriendDataResult.observeAsState()
    var errorAddUserIncomingFriend by remember { mutableStateOf("") }
    var successAddUserIncomingFriend by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()

    ) {
        Text(
            text = text,
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
                    Row {
                        IconButton(onClick = {
                            when (text) {
                                "Your friends" -> {
                                    userViewModel.removeUserFriend(it)
                                    errorFriend = ""
                                    successFriend = true
                                }

                                "Incoming friend requests" -> {
                                    userViewModel.removeUserIncomingFriend(it)
                                    errorIncomingFriend = ""
                                    successIncomingFriend = true
                                }

                                "Outgoing friend requests" -> {
                                    userViewModel.removeUserOutgoingFriend(it)
                                    errorOutgoingFriend = ""
                                    successOutgoingFriend = true
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                tint = GreenMain,
                                contentDescription = ""
                            )
                        }
                        if (text == "Incoming friend requests") {
                            IconButton(onClick = {
                                userViewModel.addUserIncomingFriend(it)
                                errorAddUserIncomingFriend = ""
                                successAddUserIncomingFriend = true
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add),
                                    tint = GreenMain,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
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
    if (removeUserFriend.value != null) {
        when (removeUserFriend.value!!) {
            is DataResult.Success -> {
                if (successFriend) {
                    Toast.makeText(LocalContext.current, "Friend removed", Toast.LENGTH_SHORT)
                        .show()
                    successFriend = false
                }
                userViewModel.getUserFriends()
            }

            is DataResult.Error -> {
                if (errorFriend != (removeUserFriend.value!! as DataResult.Error).exception) {
                    Toast.makeText(
                        LocalContext.current,
                        (removeUserFriend.value!! as DataResult.Error).exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                errorFriend = (removeUserFriend.value!! as DataResult.Error).exception
            }
        }
    }
    if (removeUserIncomingFriend.value != null) {
        when (removeUserIncomingFriend.value!!) {
            is DataResult.Success -> {
                if (successIncomingFriend) {
                    Toast.makeText(
                        LocalContext.current,
                        "Incoming request denied",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    successIncomingFriend = false
                }
                userViewModel.getUserIncomingFriends()
            }

            is DataResult.Error -> {
                if (errorIncomingFriend != (removeUserIncomingFriend.value!! as DataResult.Error).exception) {
                    Toast.makeText(
                        LocalContext.current,
                        (removeUserIncomingFriend.value!! as DataResult.Error).exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                errorIncomingFriend =
                    (removeUserIncomingFriend.value!! as DataResult.Error).exception
            }
        }
    }
    if (removeUserOutgoingFriend.value != null) {
        when (removeUserOutgoingFriend.value!!) {
            is DataResult.Success -> {
                if (successOutgoingFriend) {
                    Toast.makeText(
                        LocalContext.current,
                        "Outgoing request cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                    successOutgoingFriend = false
                }
                userViewModel.getUserOutgoingFriends()
            }

            is DataResult.Error -> {
                if (errorOutgoingFriend != (removeUserOutgoingFriend.value!! as DataResult.Error).exception) {
                    Toast.makeText(
                        LocalContext.current,
                        (removeUserOutgoingFriend.value!! as DataResult.Error).exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                errorOutgoingFriend =
                    (removeUserOutgoingFriend.value!! as DataResult.Error).exception
            }
        }
    }
    if (addUserIncomingFriend.value != null) {
        when (addUserIncomingFriend.value!!) {
            is DataResult.Success -> {
                if (successAddUserIncomingFriend) {
                    Toast.makeText(
                        LocalContext.current,
                        "Friend added",
                        Toast.LENGTH_SHORT
                    ).show()
                    successAddUserIncomingFriend = false
                }
                userViewModel.getUserFriends()
                userViewModel.getUserIncomingFriends()
            }

            is DataResult.Error -> {
                if (errorAddUserIncomingFriend != (addUserIncomingFriend.value!! as DataResult.Error).exception) {
                    Toast.makeText(
                        LocalContext.current,
                        (addUserIncomingFriend.value!! as DataResult.Error).exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                errorAddUserIncomingFriend =
                    (addUserIncomingFriend.value!! as DataResult.Error).exception
            }
        }
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
fun AddFriendGroup(
    userViewModel: UserViewModel
) {
    var friendsNickname by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }
    var clicked by remember { mutableStateOf(false) }
    val addOutgoingFriend = userViewModel.addUserOutgoingFriendDataResult.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditTextField(
            text = friendsNickname,
            onTextChanged = { friendsNickname = it },
            label = { Text("Friend's nickname") },
            keyboardType = KeyboardType.Text
        )
        Button(
            onClick = {
                if (!clicked) {
                    userViewModel.addUserOutgoingFriend(friendsNickname)
                    error = ""
                }
                clicked = true
                success = true
            },
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            ),
            modifier = Modifier.padding(12.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = null,
                colorFilter = ColorFilter.tint(WhiteBackground)
            )
            Text(
                text = "Send Request",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
    if (addOutgoingFriend.value != null) {
        when (addOutgoingFriend.value!!) {
            is DataResult.Success -> {
                if (success) {
                    Toast.makeText(
                        LocalContext.current,
                        "Friend request sent",
                        Toast.LENGTH_LONG
                    ).show()
                    success = false
                }
                userViewModel.getUserOutgoingFriends()
            }

            is DataResult.Error -> {
                if (error != (addOutgoingFriend.value!! as DataResult.Error).exception) {
                    Toast.makeText(
                        LocalContext.current,
                        (addOutgoingFriend.value!! as DataResult.Error).exception,
                        Toast.LENGTH_LONG
                    ).show()
                }
                error = (addOutgoingFriend.value!! as DataResult.Error).exception
            }
        }
        clicked = false
    }
}
