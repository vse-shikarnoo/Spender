package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.domainmodel.user.Friend
import com.example.spender.domain.domainmodel.Trip
import com.example.spender.domain.domainmodel.spend.Spend
import com.example.spender.domain.domainmodel.user.User

interface TripRepository {
    suspend fun createTrip(
        name: String,
        creator: User,
        members: List<Friend>,
    ): DataResult<String>

    suspend fun updateTripName(trip: Trip, newName: String): DataResult<String>
    suspend fun addTripMember(trip: Trip, newMember: Friend): DataResult<String>
    suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>): DataResult<String>
    suspend fun addTripSpend(trip: Trip, spend: Spend): DataResult<String>
    suspend fun removeTripMember(trip: Trip, member: Friend): DataResult<String>
    suspend fun removeTripMembers(trip: Trip, members: List<Friend>): DataResult<String>
    suspend fun removeTripSpend(spend: Spend): DataResult<String>
    suspend fun deleteTrip(trip: Trip): DataResult<String>
}