package com.example.spender.ui.navigation.screens.createRideScreens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.data.firebase.models.TestFriend
import com.example.spender.ui.navigation.CreateRideNavGraph
import com.example.spender.ui.navigation.screens.destinations.BalanceScreenDestination
import com.example.spender.ui.navigation.screens.destinations.FirstScreenDestination
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@CreateRideNavGraph(start = true)
@Destination
@Composable
fun CreateRideScreen(
    navigator: DestinationsNavigator
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Create trip",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.popBackStack()
                            navigator.navigate(FirstScreenDestination)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Arrow back"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(it)
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
                        text = "",
                        onTextChanged = {},
                        label = { Text(text = "Trip title") },
                        keyboardType = KeyboardType.Text
                    )
                }
                AddFriendsList(emptyList(), navigator)
            }
        }
    )
}
@Composable
fun AddFriendsList(
    friends: List<TestFriend>,
    navigator: DestinationsNavigator
) {
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
                items = friends,
            ) {
                FriendCard(it)
            }
            for (i in 1..10) {
                item {
                    FriendCard(friend = TestFriend(Triple("A", "B", "C")))
                }
            }
        }
        if (friends.isEmpty()) {
            Divider(
                color = GreenLight,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
        CreateTripButton(navigator)
    }
}

@Composable
fun FriendCard(
    friend: TestFriend,
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
                    friend.name.first + " " + friend.name.second,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Checkbox(
                checked = false,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    uncheckedColor = GreenMain,
                )
            )
        }
    }
}
@Composable
fun CreateTripButton(
    navigator: DestinationsNavigator
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                navigator.navigate(BalanceScreenDestination)
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
}
