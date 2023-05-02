package com.example.spender.ui.navigation.screens.profileScreens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.spender.R
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.ui.navigation.BottomBar
import com.example.spender.ui.navigation.BottomBarDestinations
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.navigation.screens.helperfunctions.EmptyListItem
import com.example.spender.ui.navigation.screens.helperfunctions.FriendCard
import com.example.spender.ui.navigation.screens.helperfunctions.OverflowListItem
import com.example.spender.ui.navigation.screens.helperfunctions.viewModelResultHandler
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.viewmodel.AuthViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    var isSendFriendRequestClicked by remember { mutableStateOf(false) }

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
                userViewModel = userViewModel,
                isSendFriendRequestClicked = isSendFriendRequestClicked,
                onCancelSendFriedRequest = { isSendFriendRequestClicked = false }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                //contentColor = GreenMain,
                containerColor = GreenMain,
                shape = CircleShape,
                onClick = { isSendFriendRequestClicked = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreenContent(
    paddingValues: PaddingValues,
    userViewModel: UserViewModel,
    isSendFriendRequestClicked: Boolean,
    onCancelSendFriedRequest: () -> Unit
) {
    val tabs = listOf("Friends", "Incoming", "Outgoing")
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0, 0f)
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
        ScrollableTabRow(
            modifier = Modifier.padding(bottom = 16.dp),
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(title)
                            Image(
                                painter = painterResource(id = R.drawable.friends),
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
                0 -> FriendsList(userViewModel = userViewModel)
                1 -> IncomingFriendsList(userViewModel = userViewModel)
                2 -> OutgoingFriendsList(userViewModel = userViewModel)
            }
        }
    }

    if (isSendFriendRequestClicked) {
        SendFriendRequestDialog(
            userViewModel = userViewModel,
            onCancelSendFriedRequest = onCancelSendFriedRequest
        )
    }
}

@Composable
fun SendFriendRequestDialog(
    userViewModel: UserViewModel,
    onCancelSendFriedRequest: () -> Unit
) {
    val addOutgoingFriend = userViewModel.addUserOutgoingFriendDataResult.observeAsState()
    val showMsg = userViewModel.addUserOutgoingFriendMsgShow.observeAsState()
    var friendsNickname by remember { mutableStateOf("") }

    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = onCancelSendFriedRequest
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(2.dp, GreenMain)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Send friend request!",
                    style = MaterialTheme.typography.titleMedium
                )
                EditTextField(
                    text = friendsNickname,
                    onTextChanged = { friendsNickname = it },
                    label = { Text("Friend's nickname") },
                    keyboardType = KeyboardType.Text
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onCancelSendFriedRequest,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) { Text(text = "Cancel", style = MaterialTheme.typography.labelMedium) }
                    Button(
                        onClick = { userViewModel.addUserOutgoingFriend(friendsNickname) },
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) { Text(text = "Send", style = MaterialTheme.typography.labelMedium) }
                }
            }
        }
    }

    viewModelResultHandler(
        LocalContext.current,
        addOutgoingFriend,
        restMsgShowState = { userViewModel.doNotShowAddUserOutgoingFriendMsg() },
        msgShow = showMsg.value ?: false
    )
}

@Composable
fun IncomingFriendsList(
    userViewModel: UserViewModel
) {
    val incomingFriends = userViewModel.getUserIncomingFriendsDataResult.observeAsState()
    var incomingFriendsLst by remember { mutableStateOf(listOf<Friend>()) }
    viewModelResultHandler(LocalContext.current, incomingFriends, { incomingFriendsLst = it })

    val removeUserIncomingFriend = userViewModel.removeUserIncomingFriendDataResult
        .observeAsState()
    val addUserIncomingFriend = userViewModel.addUserIncomingFriendDataResult
        .observeAsState()
    val removeUserIncomingFriendMsgShow = userViewModel.removeUserIncomingFriendMsgShow
        .observeAsState()
    val addUserIncomingFriendMsgShow = userViewModel.addUserIncomingFriendMsgShow
        .observeAsState()

    var showMore by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ) {
        if (incomingFriendsLst.isEmpty()) {
            item { EmptyListItem(text = "No incoming friends") }
            return@LazyColumn
        }
        if (incomingFriendsLst.size <= 3) {
            items(incomingFriendsLst) { friend ->
                IncomingFriendsListItem(friend = friend, userViewModel = userViewModel)
            }
            return@LazyColumn
        }
        if (!showMore) {
            items(incomingFriendsLst.subList(0, 3)) { friend ->
                IncomingFriendsListItem(friend = friend, userViewModel = userViewModel)
            }
        } else {
            items(incomingFriendsLst) { friend ->
                IncomingFriendsListItem(friend = friend, userViewModel = userViewModel)
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

    viewModelResultHandler(
        LocalContext.current,
        removeUserIncomingFriend,
        restMsgShowState = { userViewModel.doNotShowRemoveUserIncomingFriendMsg() },
        msgShow = removeUserIncomingFriendMsgShow.value ?: false
    )
    viewModelResultHandler(
        LocalContext.current,
        addUserIncomingFriend,
        restMsgShowState = { userViewModel.doNotShowAddUserIncomingFriendMsg() },
        msgShow = addUserIncomingFriendMsgShow.value ?: false
    )
}

@Composable
fun IncomingFriendsListItem(
    friend: Friend,
    userViewModel: UserViewModel
) {
    FriendCard(
        friend = friend,
        button = {
            Row {
                IconButton(onClick = { userViewModel.removeUserIncomingFriend(friend) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        tint = GreenMain,
                        contentDescription = ""
                    )
                }
                IconButton(onClick = { userViewModel.addUserIncomingFriend(friend) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        tint = GreenMain,
                        contentDescription = ""
                    )
                }
            }
        }
    )
}

@Composable
fun OutgoingFriendsList(
    userViewModel: UserViewModel
) {
    val outgoingFriends = userViewModel.getUserOutgoingFriendsDataResult.observeAsState()
    var outgoingFriendsLst by remember { mutableStateOf(listOf<Friend>()) }
    viewModelResultHandler(LocalContext.current, outgoingFriends, { outgoingFriendsLst = it })
    val removeUserOutgoingFriend = userViewModel.removeUserOutgoingFriendDataResult
        .observeAsState()
    val removeUserOutgoingFriendMsgShow = userViewModel.removeUserOutgoingFriendMsgShow
        .observeAsState()

    var showMore by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ) {
        if (outgoingFriendsLst.isEmpty()) {
            item { EmptyListItem(text = "No outgoing friends") }
            return@LazyColumn
        }
        if (outgoingFriendsLst.size <= 3) {
            items(outgoingFriendsLst) { friend ->
                OutgoingFriendsListItem(friend = friend, userViewModel = userViewModel)
            }
            return@LazyColumn
        }
        if (!showMore) {
            items(outgoingFriendsLst.subList(0, 3)) { friend ->
                OutgoingFriendsListItem(friend = friend, userViewModel = userViewModel)
            }
        } else {
            items(outgoingFriendsLst) { friend ->
                OutgoingFriendsListItem(friend = friend, userViewModel = userViewModel)
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

    viewModelResultHandler(
        LocalContext.current,
        removeUserOutgoingFriend,
        restMsgShowState = { userViewModel.doNotShowRemoveUserOutgoingFriendMsg() },
        msgShow = removeUserOutgoingFriendMsgShow.value ?: false
    )
}

@Composable
fun OutgoingFriendsListItem(
    friend: Friend,
    userViewModel: UserViewModel
) {
    FriendCard(
        friend = friend,
        button = {
            IconButton(onClick = { userViewModel.removeUserOutgoingFriend(friend) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    tint = GreenMain,
                    contentDescription = ""
                )
            }
        }
    )
}

@Composable
fun FriendsList(
    userViewModel: UserViewModel,
) {
    val friends = userViewModel.getUserFriendsDataResult.observeAsState()
    var friendsLst by remember { mutableStateOf(listOf<Friend>()) }
    viewModelResultHandler(LocalContext.current, friends, { friendsLst = it })
    val removeUserFriend = userViewModel.removeUserFriendDataResult.observeAsState()
    val removeUserFriendMsgShow = userViewModel.removeUserFriendMsgShow.observeAsState()

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
                FriendsListItem(friend = friend, userViewModel = userViewModel)
            }
            return@LazyColumn
        }
        if (!showMore) {
            items(friendsLst.subList(0, 3)) { friend ->
                OutgoingFriendsListItem(friend = friend, userViewModel = userViewModel)
            }
        } else {
            items(friendsLst) { friend ->
                FriendsListItem(friend = friend, userViewModel = userViewModel)
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

    viewModelResultHandler(
        LocalContext.current,
        removeUserFriend,
        restMsgShowState = { userViewModel.doNotShowRemoveUserFriendMsg() },
        msgShow = removeUserFriendMsgShow.value ?: false
    )
}

@Composable
fun FriendsListItem(
    friend: Friend,
    userViewModel: UserViewModel
) {
    FriendCard(
        friend = friend,
        button = {
            IconButton(onClick = { userViewModel.removeUserFriend(friend) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    tint = GreenMain,
                    contentDescription = ""
                )
            }
        }
    )
}
