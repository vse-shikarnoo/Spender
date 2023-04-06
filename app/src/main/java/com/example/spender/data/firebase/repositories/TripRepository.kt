package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.FirebaseInstanceHolder
import com.example.spender.data.firebase.databaseFieldNames.CollectionNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionTripDocumentFieldNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.interfaces.TripRepositoryInterface
import com.example.spender.data.firebase.interfaces.UserRepositoryInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.Trip
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.user.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TripRepository : TripRepositoryInterface {
    private val tripCollection by lazy { FirebaseInstanceHolder.db.collection(CollectionNames.TRIP) }

    override suspend fun createTrip(
        name: String,
        creator: User,
        members: List<Friend>,
    ): FirebaseCallResult<String> {
        return try {
            val batch = FirebaseInstanceHolder.db.batch()
            val newTripDocRef = tripCollection.document()

            batch.update(
                newTripDocRef,
                CollectionTripDocumentFieldNames.NAME,
                name
            )

            batch.update(
                newTripDocRef,
                CollectionTripDocumentFieldNames.CREATOR,
                creator.docRef
            )

            batch.update(
                newTripDocRef,
                CollectionTripDocumentFieldNames.MEMBERS,
                FieldValue.arrayUnion(members)
            )

            members.forEach { member ->
                batch.update(
                    member.docRef,
                    CollectionUserDocumentFieldNames.TRIPS,
                    FieldValue.arrayUnion(newTripDocRef)
                )
            }

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_CREATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateTripName(
        trip: Trip,
        newName: String
    ): FirebaseCallResult<String> {
        return try {
            trip.docRef.update(CollectionTripDocumentFieldNames.NAME, newName).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_NAME_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun addTripMember(
        trip: Trip,
        newMember: Friend
    ): FirebaseCallResult<String> {
        return try {
            trip.docRef.update(
                CollectionTripDocumentFieldNames.MEMBERS,
                FieldValue.arrayUnion(newMember.docRef)
            ).await()

            TODO("reorganize spends")

            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_MEMBER_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun addTripMembers(
        trip: Trip,
        newMembers: List<Friend>
    ): FirebaseCallResult<String> {
        return try {
            newMembers.forEach { member ->
                trip.docRef.update(
                    CollectionTripDocumentFieldNames.MEMBERS,
                    FieldValue.arrayUnion(member.docRef)
                ).await()

                TODO("reorganize spends")
            }
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_MEMBERS_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun addTripSpend(trip: Trip, spend: Spend): FirebaseCallResult<String> {
        return try {
            trip.docRef.collection(CollectionTripDocumentFieldNames.SubCollectionSpends).document()
                .set(spend).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_SPEND_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeTripMember(
        trip: Trip,
        member: Friend
    ): FirebaseCallResult<String> {
        return try {
            trip.docRef.update(
                CollectionTripDocumentFieldNames.MEMBERS,
                FieldValue.arrayRemove(member.docRef)
            ).await()

            TODO("reorganize spends")

            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_MEMBER_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeTripMembers(
        trip: Trip,
        members: List<Friend>
    ): FirebaseCallResult<String> {
        return try {
            members.forEach { member ->
                trip.docRef.update(
                    CollectionTripDocumentFieldNames.MEMBERS,
                    FieldValue.arrayRemove(member.docRef)
                ).await()

                TODO("reorganize spends")
            }
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_MEMBERS_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun removeTripSpend(spend: Spend): FirebaseCallResult<String> {
        return try {
            spend.docRef.delete().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_SPEND_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun deleteTrip(trip: Trip): FirebaseCallResult<String> {
        return try {
            val batch = FirebaseInstanceHolder.db.batch()

            trip.members.forEach { member ->
                batch.update(
                    member.docRef,
                    CollectionUserDocumentFieldNames.TRIPS,
                    FieldValue.arrayRemove(trip.docRef)
                )
            }

            batch.delete(trip.docRef)

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_DELETED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }
}
