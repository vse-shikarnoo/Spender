package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.spend.RemoteSpend
import com.example.spender.domain.remotemodel.spend.Spend
import com.example.spender.domain.remotemodel.spendmember.RemoteSpendMember
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Source

interface SpendRepository {
    suspend fun createSpend(trip: Trip, spend: Spend): DataResult<String>
    suspend fun getSpends(trip: Trip, source: Source): DataResult<List<RemoteSpend>>
    suspend fun updateSpend(
        oldRemoteSpend: RemoteSpend,
        newRemoteSpend: RemoteSpend
    ): DataResult<String>
    suspend fun updateSpendName(
        spendDocRef: DocumentReference,
        newName: String
    ): DataResult<String>
    suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String
    ): DataResult<String>
    suspend fun updateSpendSplitMode(
        spendDocRef: DocumentReference,
        newSplitMode: String
    ): DataResult<String>
    suspend fun updateSpendAmount(
        spendDocRef: DocumentReference,
        newAmount: Double
    ): DataResult<String>
    suspend fun updateSpendGeoPoint(
        spendDocRef: DocumentReference,
        newGeoPoint: GeoPoint
    ): DataResult<String>
    suspend fun addSpendMembers(
        spendDocRef: DocumentReference,
        newMembers: List<RemoteSpendMember>
    ): DataResult<String>
    suspend fun deleteSpendMembers(
        members: List<RemoteSpendMember>
    ): DataResult<String>
    suspend fun deleteSpend(spend: RemoteSpend): DataResult<String>
}
