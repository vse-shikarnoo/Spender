package com.example.spender.ui.navigation.screens.helperfunctions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.theme.GreenLight

@Preview
@Composable
fun GreetingGroup() {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(180.dp),
                painter = painterResource(id = R.drawable.spender_icon),
                contentDescription = "splash",
                contentScale = ContentScale.Fit
            )
        }
        Text(
            "Travel more, spend less",
            style = MaterialTheme.typography.headlineMedium
        )
        Divider(color = GreenLight, modifier = Modifier.padding(24.dp))
    }
}