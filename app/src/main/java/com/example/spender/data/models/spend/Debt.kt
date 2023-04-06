package com.example.spender.data.models.spend

import com.example.spender.data.models.user.Friend

data class Debt(
    val friend: Friend,
    val amount: Double
)
