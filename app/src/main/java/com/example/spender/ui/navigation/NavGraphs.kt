package com.example.spender.ui.navigation

import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class FirstNavGraph(
    val start: Boolean = false
)

@RootNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class BottomNavGraph(
    val start: Boolean = false
)

@BottomNavGraph(start = true)
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class BalanceNavGraph(
    val start: Boolean = false
)

@BottomNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class RideMapNavGraph(
    val start: Boolean = false
)

@BottomNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class CreateRideNavGraph(
    val start: Boolean = false
)

@BottomNavGraph
@com.ramcosta.composedestinations.annotation.NavGraph
annotation class ProfileNavGraph(
    val start: Boolean = false
)
