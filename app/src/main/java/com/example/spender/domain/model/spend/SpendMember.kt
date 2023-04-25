package com.example.spender.domain.model.spend

import com.example.spender.domain.model.user.Friend

data class SpendMember(
    val friend: Friend,
    val payment: Double,
    val debt: List<DebtToUser>
)
