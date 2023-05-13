package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.remote.dao.RemoteSpendDaoImpl
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.spend.GoogleMapsSpend
import com.example.spender.domain.remotemodel.spend.LocalSpend
import com.example.spender.domain.remotemodel.spend.RemoteSpend
import com.example.spender.domain.remotemodel.spend.Spend
import com.example.spender.domain.remotemodel.spendmember.RemoteSpendMember
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
        spend: LocalSpend
    ): DataResult<String> {
        return remoteSpendDaoImplServer.createSpend(trip, spend)
    }

    override suspend fun getSpends(trip: Trip, source: Source): DataResult<List<RemoteSpend>> {
        return if (source == Source.CACHE) {
            remoteSpendDaoImplCache.getSpends(trip)
        } else {
            remoteSpendDaoImplServer.getSpends(trip)
        }
    }


    override suspend fun getAllSpends(source: Source): DataResult<List<GoogleMapsSpend>> {
        return if (source == Source.CACHE) {
            remoteSpendDaoImplCache.getAllSpends()
        } else {
            remoteSpendDaoImplServer.getAllSpends()
        }
    }

    override suspend fun updateSpend(
        oldRemoteSpend: RemoteSpend,
        newRemoteSpend: RemoteSpend
    ): DataResult<String> {
        if (oldRemoteSpend.name != newRemoteSpend.name) {
            val result = remoteSpendDaoImplServer.updateSpendName(
                newRemoteSpend.docRef,
                newRemoteSpend.name
            )
            if (result is DataResult.Error) return result
        }
        if (oldRemoteSpend.category != newRemoteSpend.category) {
            val result =
                remoteSpendDaoImplServer.updateSpendCategory(
                    newRemoteSpend.docRef,
                    newRemoteSpend.category
                )
            if (result is DataResult.Error) return result
        }
        if (oldRemoteSpend.splitMode != newRemoteSpend.splitMode) {
            val result =
                remoteSpendDaoImplServer.updateSpendSplitMode(
                    newRemoteSpend.docRef,
                    newRemoteSpend.splitMode
                )
            if (result is DataResult.Error) return result
        }
        if (oldRemoteSpend.amount != newRemoteSpend.amount) {
            val result = remoteSpendDaoImplServer.updateSpendAmount(
                newRemoteSpend.docRef,
                newRemoteSpend.amount
            )
            if (result is DataResult.Error) return result
        }
        if (oldRemoteSpend.geoPoint != newRemoteSpend.geoPoint) {
            val result =
                remoteSpendDaoImplServer.updateSpendGeoPoint(
                    newRemoteSpend.docRef,
                    newRemoteSpend.geoPoint
                )
            if (result is DataResult.Error) return result
        }

        val delLst = buildList {
            oldRemoteSpend.members.forEach { member ->
                if (!newRemoteSpend.members.contains(member)) this.add(member)
            }
        }
        if (delLst.isNotEmpty()) {
            val result = remoteSpendDaoImplServer.deleteSpendMembers(delLst)
            if (result is DataResult.Error) return result
        }

        val addLst = buildList {
            newRemoteSpend.members.forEach { member ->
                if (!oldRemoteSpend.members.contains(member)) this.add(member)
            }
        }
        if (addLst.isNotEmpty()) {
            val result = remoteSpendDaoImplServer.addSpendMembers(newRemoteSpend.docRef, addLst)
            if (result is DataResult.Error) return result
        }

        return DataResult.Success(FirebaseSuccessMessages.SPEND_UPDATED)
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
        newMembers: List<RemoteSpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImplServer.addSpendMembers(spendDocRef, newMembers)
    }

    override suspend fun deleteSpendMembers(
        members: List<RemoteSpendMember>
    ): DataResult<String> {
        return remoteSpendDaoImplServer.deleteSpendMembers(members)
    }

    override suspend fun deleteSpend(spend: RemoteSpend): DataResult<String> {
        return remoteSpendDaoImplServer.deleteSpend(spend)
    }
}
