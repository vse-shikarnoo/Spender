package com.example.spender.domain.remotemodel.spend

import com.example.spender.domain.remotemodel.spendmember.RemoteSpendMember
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class RemoteSpend(
    val name: String,
    val category: String,
    val splitMode: String,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<RemoteSpendMember>,
    val docRef: DocumentReference
)
