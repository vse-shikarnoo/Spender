package com.example.spender.data.models.spend

import com.example.spender.data.models.user.Friend

data class SpendMember(
    val friend: Friend,
    val memberActivity: SpendMemberActivity
)