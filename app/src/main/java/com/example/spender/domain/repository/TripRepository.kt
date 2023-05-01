package com.example.spender.domain.repository

import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.user.Friend
import com.google.firebase.firestore.Source

interface TripRepository {
    suspend fun createTrip(
        name: String,
        members: List<Friend>,
    ): DataResult<String>

    suspend fun getAdminTrips(source: Source): DataResult<List<Trip>>
    suspend fun getPassengerTrips(): DataResult<List<Trip>>
    suspend fun updateTripName(trip: Trip, newName: String): DataResult<String>
    suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>): DataResult<String>
    suspend fun removeTripMembers(trip: Trip, members: List<Friend>): DataResult<String>
    suspend fun deleteTrip(trip: Trip): DataResult<String>
}
