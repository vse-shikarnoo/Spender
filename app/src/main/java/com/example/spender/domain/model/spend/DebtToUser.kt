package com.example.spender.domain.model.spend

import com.example.spender.domain.model.user.Friend

data class DebtToUser(
    val debt: Double,
    val user: Friend
)
