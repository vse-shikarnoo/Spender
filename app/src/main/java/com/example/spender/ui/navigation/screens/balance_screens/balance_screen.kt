package com.example.spender.ui.navigation.screens.balance_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.navigation.nav_graphs.BalanceNavGraph
import com.example.spender.ui.theme.GreenBalance
import com.example.spender.ui.theme.RedBalance
import com.example.spender.ui.theme.WhiteBackground
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
                WhiteBackground
            )
            .padding(20.dp),
    ){
        Image(
            modifier = Modifier
                .weight(1f)
                .size(80.dp),
            painter = painterResource(id = R.drawable.profile_icon),
            contentDescription = "profile_icon",
            contentScale = ContentScale.Fit
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
@Composable
fun TripsList(){
}