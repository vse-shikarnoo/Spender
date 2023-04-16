package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteSpendDaoImpl
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.SpendMember
import com.example.spender.domain.repository.SpendRepository
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import javax.inject.Inject

class SpendRepositoryImpl @Inject constructor(
    private val remoteSpendDaoImpl: RemoteSpendDaoImpl
) : SpendRepository {
    override suspend fun createSpend(
        trip: Trip,
        name: String,
        category: String,
        splitMode: Int,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImpl.createSpend(
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
        return remoteSpendDaoImpl.updateSpendName(spendDocRef, newName)
    }

    override suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String
    ): DataResult<String> {
        return remoteSpendDaoImpl.updateSpendCategory(spendDocRef, newCategory)
    }

    override suspend fun updateSpendSplitMode(
        spendDocRef: DocumentReference,
        newSplitMode: Int
    ): DataResult<String> {
        return remoteSpendDaoImpl.updateSpendSplitMode(spendDocRef, newSplitMode)
    }

    override suspend fun updateSpendAmount(
        spendDocRef: DocumentReference,
        newAmount: Double
    ): DataResult<String> {
        return remoteSpendDaoImpl.updateSpendAmount(spendDocRef, newAmount)
    }

    override suspend fun updateSpendGeoPoint(
        spendDocRef: DocumentReference,
        newGeoPoint: GeoPoint
    ): DataResult<String> {
        return remoteSpendDaoImpl.updateSpendGeoPoint(spendDocRef, newGeoPoint)
    }

    override suspend fun addSpendMembers(
        spendDocRef: DocumentReference,
        newMembers: List<SpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImpl.addSpendMembers(spendDocRef, newMembers)
    }

    override suspend fun deleteSpendMembers(
        spendDocRef: DocumentReference,
        members: List<SpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImpl.deleteSpendMembers(spendDocRef, members)
    }

    override suspend fun deleteSpend(trip: Trip): DataResult<String> {
        return remoteSpendDaoImpl.deleteSpend(trip)
    }
}