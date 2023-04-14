package com.example.spender.data.firebase.repositoryInterfaces

import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.spend.SpendMember
import com.example.spender.data.models.spend.SplitMode
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

interface SpendRepositoryInterface {
    suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String = "No category",
        splitMode: SplitMode,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>,
    ): FirebaseCallResult<String>

    suspend fun updateSpendName(spend: Spend, newName: String): FirebaseCallResult<String>
    suspend fun updateSpendCategory(spend: Spend, newCategory: String): FirebaseCallResult<String>
    suspend fun updateSpendSplitMode(
        spend: Spend,
        newSplitMode: SplitMode
    ): FirebaseCallResult<String>

    suspend fun updateSpendAmount(spend: Spend, newAmount: Double): FirebaseCallResult<String>
    suspend fun updateSpendGeoPoint(spend: Spend, newGeoPoint: GeoPoint): FirebaseCallResult<String>
    suspend fun addSpendMember(spend: Spend, newMember: Friend): FirebaseCallResult<String>
    suspend fun addSpendMembers(spend: Spend, newMembers: List<Friend>): FirebaseCallResult<String>
    suspend fun deleteSpendMembers(spend: Spend, member: List<Friend>): FirebaseCallResult<String>
    suspend fun deleteSpendSpend(spend: Spend): FirebaseCallResult<String>
}
