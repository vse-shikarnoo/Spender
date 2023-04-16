package com.example.spender.data.remote.dao

import android.app.Application
import com.example.spender.R
import com.example.spender.data.DataResult
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.dao.TripDao
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.user.User
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteTripDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
): TripDao {
    override suspend fun createTrip(
        name: String,
        creator: User,
        members: List<Friend>,
    ): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()
            val newTripDocRef = remoteDataSource.db.collection(appContext.getString(R.string.collection_name_trips)).document()

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
            DataResult.Success(FirebaseSuccessMessages.TRIP_CREATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateTripName(
        trip: Trip,
        newName: String
    ): DataResult<String> {
        return try {
            trip.docRef.update(appContext.getString(R.string.collection_trip_document_field_name), newName).await()
            DataResult.Success(FirebaseSuccessMessages.TRIP_NAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun addTripMember(
        trip: Trip,
        newMember: Friend
    ): DataResult<String> {
        return try {
            trip.docRef.update(
                appContext.getString(R.string.collection_trip_document_field_members),
                FieldValue.arrayUnion(newMember.docRef)
            ).await()

            TODO("reorganize spends")

            DataResult.Success(FirebaseSuccessMessages.TRIP_MEMBER_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun addTripMembers(
        trip: Trip,
        newMembers: List<Friend>
    ): DataResult<String> {
        return try {
            newMembers.forEach { member ->
                trip.docRef.update(
                    appContext.getString(R.string.collection_trip_document_field_members),
                    FieldValue.arrayUnion(member.docRef)
                ).await()

                TODO("reorganize spends")
            }
            DataResult.Success(FirebaseSuccessMessages.TRIP_MEMBERS_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun addTripSpend(trip: Trip, spend: Spend): DataResult<String> {
        return try {
            trip.docRef.collection(appContext.getString(R.string.collection_trip_document_field_spends)).document()
                .set(spend).await()
            DataResult.Success(FirebaseSuccessMessages.TRIP_SPEND_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeTripMember(
        trip: Trip,
        member: Friend
    ): DataResult<String> {
        return try {
            trip.docRef.update(
                appContext.getString(R.string.collection_trip_document_field_members),
                FieldValue.arrayRemove(member.docRef)
            ).await()

            TODO("reorganize spends")

            DataResult.Success(FirebaseSuccessMessages.TRIP_MEMBER_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeTripMembers(
        trip: Trip,
        members: List<Friend>
    ): DataResult<String> {
        return try {
            members.forEach { member ->
                trip.docRef.update(
                    appContext.getString(R.string.collection_trip_document_field_members),
                    FieldValue.arrayRemove(member.docRef)
                ).await()

                TODO("reorganize spends")
            }
            DataResult.Success(FirebaseSuccessMessages.TRIP_MEMBERS_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeTripSpend(spend: Spend): DataResult<String> {
        return try {
            spend.docRef.delete().await()
            DataResult.Success(FirebaseSuccessMessages.TRIP_SPEND_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun deleteTrip(trip: Trip): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()

            trip.members.forEach { member ->
                batch.update(
                    member.docRef,
                    appContext.getString(R.string.collection_users_document_field_trips),
                    FieldValue.arrayRemove(trip.docRef)
                )
            }

            batch.delete(trip.docRef)

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.TRIP_DELETED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }
}
