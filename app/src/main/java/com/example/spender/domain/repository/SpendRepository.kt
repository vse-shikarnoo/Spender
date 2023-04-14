package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.domainmodel.spend.Spend
import com.example.spender.domain.domainmodel.user.Friend
import com.example.spender.domain.domainmodel.spend.SpendMember
import com.example.spender.domain.domainmodel.spend.SplitMode
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

interface SpendRepository {
    suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String = "No category",
        splitMode: SplitMode,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>,
    ): DataResult<String>

    suspend fun updateSpendName(spend: Spend, newName: String): DataResult<String>
    suspend fun updateSpendCategory(spend: Spend, newCategory: String): DataResult<String>
    suspend fun updateSpendSplitMode(
        spend: Spend,
        newSplitMode: SplitMode
    ): DataResult<String>

    suspend fun updateSpendAmount(spend: Spend, newAmount: Double): DataResult<String>
    suspend fun updateSpendGeoPoint(spend: Spend, newGeoPoint: GeoPoint): DataResult<String>
    suspend fun addSpendMember(spend: Spend, newMember: Friend): DataResult<String>
    suspend fun addSpendMembers(spend: Spend, newMembers: List<Friend>): DataResult<String>
    suspend fun deleteSpendMember(spend: Spend, member: List<Friend>): DataResult<String>
    suspend fun deleteSpendSpend(spend: Spend): DataResult<String>
}
