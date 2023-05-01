package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteTripDaoImpl
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.repository.TripRepository
import com.google.firebase.firestore.Source
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override suspend fun getAdminTrips(source: Source): DataResult<List<Trip>> {
        return if (source == Source.CACHE) {
            remoteTripDaoImplCache.getAdminTrips()
        } else {
            remoteTripDaoImplServer.getAdminTrips()
        }
    }

    override suspend fun getPassengerTrips(source: Source): DataResult<List<Trip>> {
        return if (source == Source.CACHE) {
            remoteTripDaoImplCache.getAdminTrips()
        } else {
            remoteTripDaoImplCache.getPassengerTrips()
        }
    }

    override suspend fun updateTripName(trip: Trip, newName: String): DataResult<String> {
        return remoteTripDaoImplServer.updateTripName(trip, newName)
    }

    override suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>): DataResult<String> {
        return remoteTripDaoImplServer.addTripMembers(trip, newMembers)
    }

    override suspend fun removeTripMembers(trip: Trip, members: List<Friend>): DataResult<String> {
        return remoteTripDaoImplServer.removeTripMembers(trip, members)
    }

    override suspend fun deleteTrip(trip: Trip): DataResult<String> {
        return remoteTripDaoImplServer.deleteTrip(trip)
    }
}
