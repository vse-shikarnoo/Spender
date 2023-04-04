package com.example.spender.ui.navigation.screens.balance_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.navigation.BalanceNavGraph
import com.example.spender.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@BalanceNavGraph(start = true)
@Destination
@Composable
fun BalanceScreen(
    //navigator: DestinationsNavigator
) {
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
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BalanceCard()
                SearchTrip()
                TripsList()
            }
        }
    )
}
@Preview()
@Composable
fun BalanceCard(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(
                GreenLightBackground
            )
            .padding(16.dp),
    ){
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
        ){
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(colors = listOf(RedBalance, RedBalance)),
                        shape = MaterialTheme.shapes.medium,
                        alpha = 0.2f,
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ){
                Text(text = "You owe "+"1234",color = RedBalance,style = MaterialTheme.typography.labelMedium)
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
            ){
                Text(text = "You are owed "+"1234",color = GreenBalance, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
@Composable
fun SearchTrip(){
}
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsList(){
    Column(){
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Your Trips",style = MaterialTheme.typography.titleMedium)
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
            ){
                Box(
                    modifier = Modifier.weight(1f)
                ){
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
                ){
                    Text("Moscow Trip")
                    Text("You are owed 1200")
                }
                Box(
                    modifier = Modifier.weight(1f)
                ){
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