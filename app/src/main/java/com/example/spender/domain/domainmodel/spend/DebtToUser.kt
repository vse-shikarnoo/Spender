package com.example.spender.domain.domainmodel.spend

import com.example.spender.domain.domainmodel.user.Friend

data class DebtToUser (
    val debt: Double,
    val user: Friend
)