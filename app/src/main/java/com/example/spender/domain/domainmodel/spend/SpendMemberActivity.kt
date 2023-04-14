package com.example.spender.domain.domainmodel.spend

data class SpendMemberActivity(
    val payment: Double,
    val debt: List<Debt>
)
