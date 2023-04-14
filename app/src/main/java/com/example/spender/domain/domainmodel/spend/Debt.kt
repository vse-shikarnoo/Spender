package com.example.spender.domain.domainmodel.spend

import com.example.spender.domain.domainmodel.user.Friend

data class Debt(
    val friend: Friend,
    val amount: Double
)
