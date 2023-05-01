package com.example.spender.ui.navigation.screens.helperfunctions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.ui.navigation.screens.balanceScreens.AutoResizedText
import com.example.spender.ui.theme.GreenLight
import com.example.spender.ui.theme.GreenMain

@Composable
fun MyPager(
    page: String,
) {
    val myTripsColor: Color
    val associatedTripsColor: Color
    if (page == "My trips") {
        myTripsColor = GreenMain
        associatedTripsColor = GreenLight
    } else {
        myTripsColor = GreenLight
        associatedTripsColor = GreenMain
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AutoResizedText(text = "My trips", color = myTripsColor)
            Divider(thickness = 1.dp, color = myTripsColor)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AutoResizedText(text = "Associated trips", color = associatedTripsColor)
            Divider(thickness = 1.dp, color = associatedTripsColor)
        }
    }
}

@Composable
@Preview
fun test() {
    var page by remember {
        mutableStateOf("My trips")
    }
    val myTripsColor: Color
    val associatedTripsColor: Color
    if (page == "My trips") {
        myTripsColor = GreenMain
        associatedTripsColor = GreenLight
    } else {
        myTripsColor = GreenLight
        associatedTripsColor = GreenMain
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clickable {
                    page = if (page == "My trips") "Associated trips" else "My trips"
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AutoResizedText(text = "My trips", color = myTripsColor)
            Divider(thickness = 1.dp, color = myTripsColor)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clickable {
                    page = if (page == "My trips") "Associated trips" else "My trips"
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AutoResizedText(text = "Associated trips", color = associatedTripsColor)
            Divider(thickness = 1.dp, color = associatedTripsColor)
        }
    }
}