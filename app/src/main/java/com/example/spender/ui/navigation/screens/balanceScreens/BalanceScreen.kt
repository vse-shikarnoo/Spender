package com.example.spender.ui.navigation.screens.balanceScreens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spender.R
import com.example.spender.data.DataResult
import com.example.spender.ui.viewmodel.UserViewModel
import com.example.spender.domain.model.user.User
import com.example.spender.ui.navigation.BalanceNavGraph
import com.example.spender.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@BalanceNavGraph(start = true)
@Destination
@Composable
fun BalanceScreen(
    //navigator: DestinationsNavigator,
    userViewModel: UserViewModel = hiltViewModel()
) {
    var key by remember { mutableStateOf(0) }
    val user = userViewModel.getUserDataResult.observeAsState()

    Log.d("ABOBA", userViewModel.hashCode().toString())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Balance",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            key += 1
                            Log.d("ABOBA", "$key")
                        },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.reload),
                                contentDescription = "refresh"
                            )
                        }
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (user.value) {
                    is DataResult.Success -> {
                        BalanceCard((user.value as DataResult.Success).data)
                    }
                    is DataResult.Error -> {
                        Toast.makeText(
                            LocalContext.current,
                            (user.value as DataResult.Error).exception,
                            Toast.LENGTH_SHORT
                        ).show()
                        BalanceCard(null)
                    }
                    else -> {
                        BalanceCard(null)
                    }
                }
                SearchTrip()
                TripsList()
            }
        }
    )

    LaunchedEffect(key1 = key) {
        Log.d("ABOBA", "launched effect")
        userViewModel.getUser(null)
    }
}

@Composable
fun BalanceCard(user: User?) {
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
                .weight(1f)
                .size(80.dp)
                .clip(CircleShape),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .weight(3f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(colors = listOf(RedBalance, RedBalance)),
                        shape = MaterialTheme.shapes.medium,
                        alpha = 0.2f,
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = user?.nickname ?: "Loading...",
                    color = RedBalance,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(colors = listOf(RedBalance, RedBalance)),
                        shape = MaterialTheme.shapes.medium,
                        alpha = 0.2f,
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "You owe " + "1234",
                    color = RedBalance,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                GreenBalance,
                                GreenBalance
                            )
                        ),
                        shape = MaterialTheme.shapes.medium,
                        alpha = 0.2f,
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "You are owed " + "1234",
                    color = GreenBalance,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun SearchTrip() {
    val lol = "df"
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsList() {
    Column {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Your Trips", style = MaterialTheme.typography.titleMedium)
            Image(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(id = R.drawable.bag_icon),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Card(onClick = {}) {
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
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
                Column(
                    modifier = Modifier.weight(4f)
                ) {
                    Text("Moscow Trip")
                    Text("You are owed 1200")
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        colorFilter = ColorFilter.tint(GreenBalance)
                    )
                }
            }
        }
    }
}
