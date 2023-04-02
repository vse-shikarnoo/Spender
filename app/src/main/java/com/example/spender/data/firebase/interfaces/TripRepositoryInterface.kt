package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.dataClasses.Friend
import com.example.spender.data.firebase.dataClasses.Spend
import com.example.spender.data.firebase.dataClasses.Trip
import com.example.spender.data.firebase.fieldNames.CollectionUserDocumentFieldNames
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

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
        newMember: List<DocumentReference>,
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