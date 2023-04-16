package com.example.spender.data.repository

import com.example.spender.data.DataResult
import com.example.spender.data.remote.dao.RemoteTripDaoImpl
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.repository.TripRepository
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val remoteTripDaoImpl: RemoteTripDaoImpl
): TripRepository {
    override suspend fun createTrip(
        name: String,
        creator: User,
        members: List<Friend>
    ): DataResult<String> {
        return remoteTripDaoImpl.createTrip(
            name,
            creator,
            members
        )
    }

    override suspend fun updateTripName(trip: Trip, newName: String): DataResult<String> {
        return remoteTripDaoImpl.updateTripName(trip, newName)
    }

    override suspend fun addTripMember(trip: Trip, newMember: Friend): DataResult<String> {
        return remoteTripDaoImpl.addTripMember(trip, newMember)
    }

    override suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>): DataResult<String> {
        return remoteTripDaoImpl.addTripMembers(trip, newMembers)
    }

    override suspend fun addTripSpend(trip: Trip, spend: Spend): DataResult<String> {
        return remoteTripDaoImpl.addTripSpend(trip, spend)
    }

    override suspend fun removeTripMember(trip: Trip, member: Friend): DataResult<String> {
        return remoteTripDaoImpl.removeTripMember(trip, member)
    }

    override suspend fun removeTripMembers(trip: Trip, members: List<Friend>): DataResult<String> {
        return remoteTripDaoImpl.removeTripMembers(trip, members)
    }

    override suspend fun removeTripSpend(spend: Spend): DataResult<String> {
        return remoteTripDaoImpl.removeTripSpend(spend)
    }

    override suspend fun deleteTrip(trip: Trip): DataResult<String> {
        return remoteTripDaoImpl.deleteTrip(trip)
    }
}