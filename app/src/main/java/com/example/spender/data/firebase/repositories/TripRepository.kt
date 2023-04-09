package com.example.spender.data.firebase.repositories

import android.app.Application
import com.example.spender.R
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.repositoryInterfaces.TripRepositoryInterface
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.Trip
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.user.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val appContext: Application
) : TripRepositoryInterface {

    override suspend fun createTrip(
        name: String,
        creator: User,
        members: List<Friend>,
    ): FirebaseCallResult<String> {
        return try {
            val batch = db.batch()
            val newTripDocRef = db.collection(appContext.getString(R.string.collection_name_trips)).document()

            batch.update(
                newTripDocRef,
                appContext.getString(R.string.collection_trip_document_field_name),
                name
            )

            batch.update(
                newTripDocRef,
                appContext.getString(R.string.collection_trip_document_field_creator),
                creator.docRef
            )

            batch.update(
                newTripDocRef,
                appContext.getString(R.string.collection_trip_document_field_members),
                FieldValue.arrayUnion(members)
            )

            members.forEach { member ->
                batch.update(
                    member.docRef,
                    appContext.getString(R.string.collection_users_document_field_trips),
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
            trip.docRef.update(appContext.getString(R.string.collection_trip_document_field_name), newName).await()
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
                appContext.getString(R.string.collection_trip_document_field_members),
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
                    appContext.getString(R.string.collection_trip_document_field_members),
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
            trip.docRef.collection(appContext.getString(R.string.collection_trip_document_field_spends)).document()
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
                appContext.getString(R.string.collection_trip_document_field_members),
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
                    appContext.getString(R.string.collection_trip_document_field_members),
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
            val batch = db.batch()

            trip.members.forEach { member ->
                batch.update(
                    member.docRef,
                    appContext.getString(R.string.collection_users_document_field_trips),
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
