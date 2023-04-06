package com.example.spender.data.models.spend

data class SpendMemberActivity(
    val payment: Double,
    val debt: List<Debt>
)
