package com.example.spender.data.firebase.repositoryInterfaces

import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.Trip
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.user.User

interface TripRepositoryInterface {
    suspend fun createTrip(
        name: String,
        creator: User,
        members: List<Friend>,
    ): FirebaseCallResult<String>

    suspend fun updateTripName(trip: Trip, newName: String): FirebaseCallResult<String>
    suspend fun addTripMember(trip: Trip, newMember: Friend): FirebaseCallResult<String>
    suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>): FirebaseCallResult<String>
    suspend fun addTripSpend(trip: Trip, spend: Spend): FirebaseCallResult<String>
    suspend fun removeTripMember(trip: Trip, member: Friend): FirebaseCallResult<String>
    suspend fun removeTripMembers(trip: Trip, members: List<Friend>): FirebaseCallResult<String>
    suspend fun removeTripSpend(spend: Spend): FirebaseCallResult<String>
    suspend fun deleteTrip(trip: Trip): FirebaseCallResult<String>
}
