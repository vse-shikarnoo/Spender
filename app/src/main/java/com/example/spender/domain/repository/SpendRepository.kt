package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.domainmodel.Trip
import com.example.spender.domain.domainmodel.spend.SpendMember
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

interface SpendRepository {
    suspend fun createSpend(
        trip: Trip,
        name: String,
        category: String = "No category",
        splitMode: Int = 0,
        amount: Double = 0.0,
        geoPoint: GeoPoint = GeoPoint(0.0, 0.0),
        members: List<SpendMember>,
    ): DataResult<String>

    suspend fun updateSpendName(spendDocRef: DocumentReference, newName: String): DataResult<String>
    suspend fun updateSpendCategory(spendDocRef: DocumentReference, newCategory: String): DataResult<String>
    suspend fun updateSpendSplitMode(spendDocRef: DocumentReference, newSplitMode: Int): DataResult<String>
    suspend fun updateSpendAmount(spendDocRef: DocumentReference, newAmount: Double): DataResult<String>
    suspend fun updateSpendGeoPoint(spendDocRef: DocumentReference, newGeoPoint: GeoPoint): DataResult<String>
    suspend fun addSpendMembers(spendDocRef: DocumentReference, newMembers: List<SpendMember>): DataResult<String>
    suspend fun deleteSpendMembers(spendDocRef: DocumentReference, members: List<SpendMember>): DataResult<String>
    suspend fun deleteSpend(trip: Trip): DataResult<String>
}
