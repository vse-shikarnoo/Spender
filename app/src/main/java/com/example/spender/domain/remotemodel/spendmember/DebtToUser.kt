package com.example.spender.domain.remotemodel.spendmember

import com.example.spender.domain.remotemodel.user.Friend

data class DebtToUser(
    val user: Friend,
    val debt: Double
)
