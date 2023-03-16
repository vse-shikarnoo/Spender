package com.example.spender.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.spender.R

// Set of Material typography styles to start with
val Roboto = FontFamily(
    Font(R.font.roboto_light),
    Font(R.font.roboto_medium),
    Font(R.font.roboto_black),
    Font(R.font.roboto_bold),
    Font(R.font.roboto_regular),
    Font(R.font.roboto_thin),

)
val spenderTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Light,
        fontSize = 36.sp,
        color = GreenMain,
        letterSpacing = 12.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = GreenMain,
        letterSpacing = 4.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = WhiteBackground,
        letterSpacing = 1.sp
    ),/*
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)