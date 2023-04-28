package com.example.spender.domain.model.spend

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Spend(
    val name: String,
    val category: String,
    val splitMode: String,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<SpendMember>,
    val docRef: DocumentReference
)
