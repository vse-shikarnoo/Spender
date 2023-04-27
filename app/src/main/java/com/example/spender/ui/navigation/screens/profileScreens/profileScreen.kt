package com.example.spender.ui.navigation.screens.profileScreens

import android.app.Activity
import android.content.Intent
import android.util.Log
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
import com.example.spender.domain.model.user.Friend
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomBarDestinations
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.navigation.screens.helperfunctions.FriendCard
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.theme.WhiteBackground
import com.example.spender.ui.viewmodel.AuthViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    Scaffold(
        topBar = {
            ProfileScreenTopBar(
                authViewModel = authViewModel,
                userViewModel = userViewModel
            )
        },
        bottomBar = { BottomBar(BottomBarDestinations.Profile, navigator) },
        content = {
            ProfileScreenContent(
                paddingValues = it,
                userViewModel = userViewModel
            )
        }
    )
    LaunchedEffect(
        key1 = true,
        block = {
            userViewModel.getUserFriends()
            userViewModel.getUserIncomingFriends()
            userViewModel.getUserOutgoingFriends()
            userViewModel.getUserNickname()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTopBar(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    val userNicknameResult = userViewModel.getUserNicknameDataResult.observeAsState()
    val signOutResult = authViewModel.signOutDataResult.observeAsState()
    var userNickname by remember { mutableStateOf("Loading...") }

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
            IconButton(onClick = { authViewModel.signOut() }) {
                Icon(Icons.Filled.ExitToApp, null, tint = GreenMain)
            }
        }
    )

    viewModelResultHandler(LocalContext.current, userNicknameResult, { userNickname = it })
    viewModelResultHandler(
        LocalContext.current,
        signOutResult,
        onSuccess = {
            val activity = (LocalContext.current as Activity)
            val intent: Intent = activity.intent
            activity.finish()
            ContextCompat.startActivity(activity, intent, intent.extras)
        }
    )
}

@Composable
fun ProfileScreenContent(
    paddingValues: PaddingValues,
    userViewModel: UserViewModel
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(paddingValues)
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
        FriendsList(userViewModel)
    }
}

@Composable
fun FriendsList(
    userViewModel: UserViewModel,
) {
    val friends = userViewModel.getUserFriendsDataResult.observeAsState()
    val incomingFriends = userViewModel.getUserIncomingFriendsDataResult.observeAsState()
    val outgoingFriends = userViewModel.getUserOutgoingFriendsDataResult.observeAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        viewModelResultHandler(
            LocalContext.current,
            friends,
            onSuccess = {
                if (it.isNotEmpty())
                    FriendsListTemplate("Friends", it, userViewModel)
            },
        )
        viewModelResultHandler(
            LocalContext.current,
            incomingFriends,
            onSuccess = {
                if (it.isNotEmpty())
                    FriendsListTemplate("Incoming friend requests", it, userViewModel)
            },
        )
        viewModelResultHandler(
            LocalContext.current,
            outgoingFriends,
            onSuccess = {
                if (it.isNotEmpty())
                    FriendsListTemplate("Outgoing friend requests", it, userViewModel)
            }
        )
        AddFriendGroup(userViewModel)
    }
}

@Composable
fun FriendsListTemplate(
    text: String,
    friendsLst: List<Friend>,
    userViewModel: UserViewModel
) {
    FriendListTitle(text)
    Divider(color = GreenLight, modifier = Modifier.padding(vertical = 12.dp))
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
        ) { friend ->
            FriendCard(
                friend = friend,
                button = { FriendCardButton(text, friend, userViewModel) }
            )
        }
    }
    if (friendsLst.isEmpty()) {
        Divider(color = GreenLight, modifier = Modifier.padding(vertical = 12.dp))
    }
}

@Composable
fun FriendListTitle(text: String) {
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
}

@Composable
fun FriendCardButton(
    text: String,
    friend: Friend,
    userViewModel: UserViewModel
) {
    val removeUserFriend = userViewModel.removeUserFriendDataResult.observeAsState()
    val removeUserIncomingFriend = userViewModel.removeUserIncomingFriendDataResult
        .observeAsState()
    val removeUserOutgoingFriend = userViewModel.removeUserOutgoingFriendDataResult
        .observeAsState()
    val addUserIncomingFriend = userViewModel.addUserIncomingFriendDataResult.observeAsState()

    val removeUserFriendMsgShow = userViewModel.removeUserFriendMsgShow.observeAsState()
    val removeUserIncomingFriendMsgShow = userViewModel.removeUserIncomingFriendMsgShow
        .observeAsState()
    val removeUserOutgoingFriendMsgShow = userViewModel.removeUserOutgoingFriendMsgShow
        .observeAsState()
    val addUserIncomingFriendMsgShow = userViewModel.addUserIncomingFriendMsgShow.observeAsState()

    Row {
        FriendCardIconButton(
            onClick = {
                if (text == "Friends") {
                    userViewModel.removeUserFriend(friend)
                }
                if (text == "Incoming friend requests") {
                    userViewModel.removeUserIncomingFriend(friend)
                }
                if (text == "Outgoing friend requests") {
                    userViewModel.removeUserOutgoingFriend(friend)
                }
            },
            R.drawable.baseline_close_24
        )
        if (text == "Incoming friend requests") {
            FriendCardIconButton(
                onClick = {
                    userViewModel.addUserIncomingFriend(friend)
                },
                drawable = R.drawable.add
            )
        }
    }

    viewModelResultHandler(
        LocalContext.current,
        removeUserFriend,
        onSuccess = {
            userViewModel.getUserFriends()
        },
        onComplete = {
            userViewModel.doNotShowRemoveUserFriendMsg()
        },
        msgShow = removeUserFriendMsgShow.value ?: false
    )
    viewModelResultHandler(
        LocalContext.current,
        removeUserIncomingFriend,
        onSuccess = {
            userViewModel.getUserIncomingFriends()
        },
        onComplete = {
            userViewModel.doNotShowRemoveUserIncomingFriendMsg()
        },
        msgShow = removeUserIncomingFriendMsgShow.value ?: false
    )
    viewModelResultHandler(
        LocalContext.current,
        removeUserOutgoingFriend,
        onSuccess = {
            userViewModel.getUserOutgoingFriends()
        },
        onComplete = {
            userViewModel.doNotShowRemoveUserOutgoingFriendMsg()
        },
        msgShow = removeUserOutgoingFriendMsgShow.value ?: false
    )
    viewModelResultHandler(
        LocalContext.current,
        addUserIncomingFriend,
        onSuccess = {
            userViewModel.getUserFriends()
            userViewModel.getUserIncomingFriends()
        },
        onComplete = {
            userViewModel.doNotShowAddUserIncomingFriendMsg()
        },
        msgShow = addUserIncomingFriendMsgShow.value ?: false
    )
}

@Composable
fun FriendCardIconButton(onClick: () -> Unit, drawable: Int) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = drawable),
            tint = GreenMain,
            contentDescription = ""
        )
    }
}

@Composable
fun AddFriendGroup(
    userViewModel: UserViewModel
) {
    val addOutgoingFriend = userViewModel.addUserOutgoingFriendDataResult.observeAsState()
    val showMsg = userViewModel.addUserOutgoingFriendMsgShow.observeAsState()
    Log.d("ABOBA", "AddFriendGroup: $showMsg")
    var friendsNickname by remember { mutableStateOf("") }
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
                userViewModel.addUserOutgoingFriend(friendsNickname)
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
    viewModelResultHandler(
        LocalContext.current,
        addOutgoingFriend,
        onSuccess = {
            userViewModel.getUserOutgoingFriends()
            userViewModel.getUserIncomingFriends()
            userViewModel.getUserFriends()
        },
        onComplete = {
            userViewModel.doNotShowAddUserOutgoingFriendMsg()
        },
        msgShow = showMsg.value ?: false
    )
}
