package com.example.spender.domain.domainmodel.spend

import com.example.spender.domain.domainmodel.user.Friend

data class SpendMember(
    val friend: Friend,
    val payment: Double,
    val debt: List<DebtToUser>
)