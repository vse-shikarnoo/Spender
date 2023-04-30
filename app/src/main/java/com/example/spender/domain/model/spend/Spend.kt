package com.example.spender.domain.remotemodel.spend

import com.example.spender.domain.remotemodel.spendmember.RemoteSpendMember
import com.google.firebase.firestore.GeoPoint

data class Spend(
    val name: String,
    val category: String,
    val splitMode: String,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<RemoteSpendMember>
)
