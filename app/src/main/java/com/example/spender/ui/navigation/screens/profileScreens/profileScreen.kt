package com.example.spender.ui.navigation.screens.profileScreens

import android.app.Activity
import android.content.Intent
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.spender.R
import com.example.spender.data.DataResult
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.TestFriend
import com.example.spender.ui.navigation.ProfileNavGraph
import com.example.spender.ui.navigation.screens.createRideScreens.FriendCard
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.theme.WhiteBackground
import com.example.spender.ui.viewmodel.AuthViewModel
import com.example.spender.ui.viewmodel.UserViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@ProfileNavGraph(start = true)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    var signOut by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "User Name",
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
    })
}

@Composable
fun FriendsList(
    userViewModel: UserViewModel,
    navigator: DestinationsNavigator
) {
    val friends = userViewModel.getUserFriendsDataResult.observeAsState()
    val friendsLst = getFriends(friends)
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(
                "Your Friends",
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
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                tint = GreenMain,
                                contentDescription = ""
                            )
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
        AddFriendGroup(navigator = navigator)
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
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditTextField(
            text = "",
            onTextChanged = {},
            label = { Text("Friend's nickname") },
            keyboardType = KeyboardType.Text
        )
        Button(
            onClick = {
                navigator.navigate(BalanceScreenDestination)
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
}
