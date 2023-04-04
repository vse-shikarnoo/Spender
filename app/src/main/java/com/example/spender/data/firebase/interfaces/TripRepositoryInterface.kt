package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.models.Friend
import com.example.spender.data.firebase.models.Trip
import com.google.firebase.firestore.DocumentReference

interface TripRepositoryInterface {
    suspend fun createTrip(
        name: String,
        creatorDocRef: DocumentReference,
        members: List<Friend>,
    ): Result<Boolean>

    suspend fun getTrip(
        tripDocRef: DocumentReference,
    ): Result<Trip>

    suspend fun getTripName(
        tripDocRef: DocumentReference,
    ): Result<String>

    suspend fun getTripCreator(
        tripDocRef: DocumentReference,
    ): Result<Friend>

    suspend fun getTripMembers(
        tripDocRef: DocumentReference,
    ): Result<List<Friend>>

    suspend fun getTripsByUserId(
        userID: String,
    ): Result<List<Trip>>

    suspend fun updateTripName(
        tripDocRef: DocumentReference,
        newName: String,
    ): Result<Boolean>

    suspend fun addTripMember(
        tripDocRef: DocumentReference,
        newMember: DocumentReference,
    ): Result<Boolean>

    suspend fun addTripMembers(
        tripDocRef: DocumentReference,
        newMembers: List<DocumentReference>,
    ): Result<Boolean>

    suspend fun deleteTripMember(
        tripDocRef: DocumentReference,
        member: DocumentReference,
    ): Result<Boolean>

    suspend fun deleteTripMembers(
        tripDocRef: DocumentReference,
        members: List<DocumentReference>,
    ): Result<Boolean>

    suspend fun deleteTrip(
        tripDocRef: DocumentReference,
    ): Result<Boolean>
}