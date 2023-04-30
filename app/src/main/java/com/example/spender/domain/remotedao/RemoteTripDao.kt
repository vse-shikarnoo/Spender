package com.example.spender.domain.remotedao

import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.user.Friend
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source

interface RemoteTripDao {
    var source: Source

    suspend fun createTrip(
        name: String,
        members: List<Friend>,
    ): DataResult<String>

    suspend fun getTrip(tripDocRef: DocumentReference): DataResult<Trip>
    suspend fun getTripName(tripDocRef: DocumentReference): DataResult<String>
    suspend fun getTripMembers(tripDocRef: DocumentReference): DataResult<List<Friend>>
    suspend fun getTrips(): DataResult<List<Trip>>
    suspend fun getAdminTrips(): DataResult<List<Trip>>
    suspend fun getPassengerTrips(): DataResult<List<Trip>>
    suspend fun assembleTripList(userTripsDocRefs: List<DocumentReference>): DataResult<List<Trip>>
    suspend fun updateTripName(trip: Trip, newName: String): DataResult<String>
    suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>): DataResult<String>
    suspend fun removeTripMembers(trip: Trip, members: List<Friend>): DataResult<String>
    suspend fun deleteTrip(trip: Trip): DataResult<String>
}
