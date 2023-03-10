package com.example.spender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import com.example.spender.ui.navigation.screens.NavGraphs
import com.example.spender.ui.theme.SpenderTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpenderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // в терминале вызываем ./gradlew kspDebugKotlin если не появляется
                    // импорт какого-либо ScreenDestination
                    // navigation module
                    DestinationsNavHost(navGraph = NavGraphs.first)
                }
            }
        }
    }
}