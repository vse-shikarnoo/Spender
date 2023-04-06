package com.example.spender.data.models.spend

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Spend(
    val name: String,
    val category: String,
    val splitMode: SplitMode,
    val amount: Double,
    val geoPoint: GeoPoint,
    val members: List<SpendMember>,
    val docRef: DocumentReference
)
