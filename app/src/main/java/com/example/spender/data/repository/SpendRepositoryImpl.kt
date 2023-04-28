package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteSpendDaoImpl
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.SpendMember
import com.example.spender.domain.repository.SpendRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Source
import javax.inject.Inject

class SpendRepositoryImpl @Inject constructor(
    private val remoteSpendDaoImplServer: RemoteSpendDaoImpl,
    private val remoteSpendDaoImplCache: RemoteSpendDaoImpl
) : SpendRepository {
    init {
        remoteSpendDaoImplServer.source = Source.SERVER
        remoteSpendDaoImplCache.source = Source.CACHE
    }

    override suspend fun createSpend(
        trip: Trip,
        name: String,
        category: String,
        splitMode: String,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImplServer.createSpend(
            trip,
            name,
            category,
            splitMode,
            amount,
            geoPoint,
            members
        )
    }

    override suspend fun updateSpendName(
        spendDocRef: DocumentReference,
        newName: String
    ): DataResult<String> {
        return remoteSpendDaoImplServer.updateSpendName(spendDocRef, newName)
    }

    override suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String
    ): DataResult<String> {
        return remoteSpendDaoImplServer.updateSpendCategory(spendDocRef, newCategory)
    }

    override suspend fun updateSpendSplitMode(
        spendDocRef: DocumentReference,
        newSplitMode: String
    ): DataResult<String> {
        return remoteSpendDaoImplServer.updateSpendSplitMode(spendDocRef, newSplitMode)
    }

    override suspend fun updateSpendAmount(
        spendDocRef: DocumentReference,
        newAmount: Double
    ): DataResult<String> {
        return remoteSpendDaoImplServer.updateSpendAmount(spendDocRef, newAmount)
    }

    override suspend fun updateSpendGeoPoint(
        spendDocRef: DocumentReference,
        newGeoPoint: GeoPoint
    ): DataResult<String> {
        return remoteSpendDaoImplServer.updateSpendGeoPoint(spendDocRef, newGeoPoint)
    }

    override suspend fun addSpendMembers(
        spendDocRef: DocumentReference,
        newMembers: List<SpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImplServer.addSpendMembers(spendDocRef, newMembers)
    }

    override suspend fun deleteSpendMembers(
        spendDocRef: DocumentReference,
        members: List<SpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImplServer.deleteSpendMembers(spendDocRef, members)
    }

    override suspend fun deleteSpend(trip: Trip): DataResult<String> {
        return remoteSpendDaoImplServer.deleteSpend(trip)
    }
}
