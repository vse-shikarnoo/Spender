package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteTripDaoImpl
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.repository.TripRepository
import com.google.firebase.firestore.Source
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val remoteTripDaoImplServer: RemoteTripDaoImpl,
    private val remoteTripDaoImplCache: RemoteTripDaoImpl
) : TripRepository {
    init {
        remoteTripDaoImplServer.source = Source.SERVER
        remoteTripDaoImplCache.source = Source.CACHE
    }

    override suspend fun createTrip(
        name: String,
        members: List<Friend>
    ): DataResult<String> {
        return remoteTripDaoImplServer.createTrip(
            name,
            members
        )
    }

    override suspend fun updateTripName(trip: Trip, newName: String): DataResult<String> {
        return remoteTripDaoImplServer.updateTripName(trip, newName)
    }

    override suspend fun addTripMember(trip: Trip, newMember: Friend): DataResult<String> {
        return remoteTripDaoImplServer.addTripMember(trip, newMember)
    }

    override suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>): DataResult<String> {
        return remoteTripDaoImplServer.addTripMembers(trip, newMembers)
    }

    override suspend fun addTripSpend(trip: Trip, spend: Spend): DataResult<String> {
        return remoteTripDaoImplServer.addTripSpend(trip, spend)
    }

    override suspend fun removeTripMember(trip: Trip, member: Friend): DataResult<String> {
        return remoteTripDaoImplServer.removeTripMember(trip, member)
    }

    override suspend fun removeTripMembers(trip: Trip, members: List<Friend>): DataResult<String> {
        return remoteTripDaoImplServer.removeTripMembers(trip, members)
    }

    override suspend fun removeTripSpend(spend: Spend): DataResult<String> {
        return remoteTripDaoImplServer.removeTripSpend(spend)
    }

    override suspend fun deleteTrip(trip: Trip): DataResult<String> {
        return remoteTripDaoImplServer.deleteTrip(trip)
    }
}
