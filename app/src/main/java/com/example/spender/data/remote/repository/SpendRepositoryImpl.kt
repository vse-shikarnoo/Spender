package com.example.spender.data.remote.repository

import android.app.Application
import com.example.spender.data.DataResult
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.repository.SpendRepository
import com.example.spender.domain.domainmodel.user.Friend
import com.example.spender.domain.domainmodel.spend.Spend
import com.example.spender.domain.domainmodel.spend.SpendMember
import com.example.spender.domain.domainmodel.spend.SplitMode
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import javax.inject.Inject

class SpendRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : SpendRepository {
    override suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String,
        splitMode: SplitMode,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendName(
        spend: Spend,
        newName: String
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendCategory(
        spend: Spend,
        newCategory: String
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendSplitMode(
        spend: Spend,
        newSplitMode: SplitMode
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendAmount(
        spend: Spend,
        newAmount: Double
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendGeoPoint(
        spend: Spend,
        newGeoPoint: GeoPoint
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMember(
        spend: Spend,
        newMember: Friend
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMembers(
        spend: Spend,
        newMembers: List<Friend>
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendMember(
        spend: Spend,
        member: List<Friend>
    ): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendSpend(spend: Spend): DataResult<String> {
        TODO("Not yet implemented")
    }
}
