package com.example.spender.domain.domainmodel.spend

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Spend(
    val name: String,
    val category: String,
    val splitMode: Int,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<SpendMember>,
    val docRef: DocumentReference
)
