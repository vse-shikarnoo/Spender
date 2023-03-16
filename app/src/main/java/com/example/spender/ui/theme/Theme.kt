package com.example.spender.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = GreenMain,
    onPrimary = GreenLight,
    secondary = WhiteBackground,
    background = WhiteBackground,
    surface = WhiteBackground,
)

private val LightColorPalette = lightColorScheme(
    primary = GreenMain,
    onPrimary = GreenLight,
    secondary = WhiteBackground,


    /* Other default colors to override
    background = Color.WhiteBackground,
    surface = Color.WhiteBackground,
    onPrimary = Color.WhiteBackground,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SpenderTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = GreenLightBackground,
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = GreenLightBackground,
            darkIcons = true
        )
    }
    MaterialTheme(
        colorScheme = colors,
        typography = spenderTypography,
        shapes = spenderShapes,
        content = content
    )
}