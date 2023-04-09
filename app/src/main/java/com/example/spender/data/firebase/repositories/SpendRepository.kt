package com.example.spender.data.firebase.repositories

import android.app.Application
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.repositoryInterfaces.SpendRepositoryInterface
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.spend.SpendMember
import com.example.spender.data.models.spend.SplitMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import javax.inject.Inject

class SpendRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val appContext: Application
) : SpendRepositoryInterface {
    override suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String,
        splitMode: SplitMode,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendName(
        spend: Spend,
        newName: String
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendCategory(
        spend: Spend,
        newCategory: String
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendSplitMode(
        spend: Spend,
        newSplitMode: SplitMode
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendAmount(
        spend: Spend,
        newAmount: Double
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendGeoPoint(
        spend: Spend,
        newGeoPoint: GeoPoint
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMember(
        spend: Spend,
        newMember: Friend
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMembers(
        spend: Spend,
        newMembers: List<Friend>
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendMember(
        spend: Spend,
        member: List<Friend>
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendSpend(spend: Spend): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }
}
