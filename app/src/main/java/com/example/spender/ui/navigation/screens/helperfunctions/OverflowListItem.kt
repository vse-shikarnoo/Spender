package com.example.spender.ui.navigation.screens.helperfunctions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.ui.navigation.screens.balanceScreens.AutoResizedText
import com.example.spender.ui.theme.GreenLightBackground
import com.example.spender.ui.theme.GreenMain

@Composable
fun OverflowListItem(
    text: () -> String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                ),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLightBackground,
                    contentColor = GreenMain
                ),
            ) {
                AutoResizedText(text = text.invoke(), color = GreenMain)
            }
        }
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
    }
}