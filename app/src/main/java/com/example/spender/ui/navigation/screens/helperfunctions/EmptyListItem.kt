package com.example.spender.ui.navigation.screens.helperfunctions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.GreenLightBackground
import com.example.spender.ui.theme.GreenMain

@Composable
fun EmptyListItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
        Text(text = text, color = GreenMain)
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
    }
}

@Preview
@Composable
fun EmptyListItemPreview(text: String = "No") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
        Text(
            text = text,
            color = GreenMain
        )
        Divider(modifier = Modifier.weight(1f), color = GreenLightBackground)
    }
}
