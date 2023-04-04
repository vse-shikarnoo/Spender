package com.example.spender.ui.theme

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val GreenMain = Color(0xFF686C33)
val GreenLight = Color(0xFFC5BAA9)
val WhiteBackground = Color(0xFFFFFFFF)
val GreenLightBackground = Color(0xFFF6F4F5)
val RedBalance = Color(0xFFA01414)
val GreenBalance = Color(0xFF12921E)
@Composable
fun spenderTextFieldColors(
    primary: Color = GreenMain,
    secondary: Color = GreenLight,
    backgroundColor: Color = WhiteBackground

) = TextFieldDefaults.textFieldColors(
    textColor = primary,
    disabledTextColor = secondary,
    cursorColor = primary,
    focusedIndicatorColor = primary,
    unfocusedIndicatorColor = secondary,
    disabledIndicatorColor = secondary,
    backgroundColor = backgroundColor,
    focusedLabelColor = primary,
    unfocusedLabelColor = secondary,
    disabledLabelColor = secondary,
    placeholderColor = primary,
)
