package com.example.spender.data.remote.dao

import android.app.Application
import com.example.spender.R
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.messages.exceptions.FirebaseUndefinedException
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.remotedao.RemoteTripDao
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.user.Friend
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class RemoteTripDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : RemoteTripDao {
    private val sharedFunctions = SharedFunctions(remoteDataSource, appContext)
    override var source: Source = Source.SERVER

    /*
     * Create a new trip
     */

    override suspend fun createTrip(
        name: String,
        members: List<Friend>,
    ): DataResult<String> {
        if (name.isBlank()) {
            return DataErrorHandler.handle(IllegalArgumentException("Trip name cannot be blank"))
        }
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)
            val newTripDocRef = remoteDataSource.db.collection(
                appContext.getString(R.string.collection_name_trips)
            ).document().get().await().reference
            val membersFirebase = buildList {
                members.forEach { member ->
                    this.add(member.docRef)
                }
                this.add(userDocRef)
            }

            newTripDocRef.set(
                mapOf(
                    appContext.getString(R.string.collection_trip_document_field_name) to name,
                    appContext.getString(R.string.collection_trip_document_field_creator) to
                        userDocRef,
                    appContext.getString(R.string.collection_trip_document_field_members) to
                        FieldValue.arrayUnion(
                            *membersFirebase.toTypedArray()
                        )
                )
            ).await()

            membersFirebase.forEach { docRef ->
                docRef.update(
                    appContext.getString(R.string.collection_users_document_field_trips),
                    FieldValue.arrayUnion(newTripDocRef)
                ).await()
            }

            DataResult.Success(FirebaseSuccessMessages.TRIP_CREATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * Get a trip
     */

    override suspend fun getTrip(tripDocRef: DocumentReference): DataResult<Trip> {
        return try {
            val tripDocSnapshot = tripDocRef.get(source).await()
            val tripCreatorDocRef = tripDocSnapshot.get(
                appContext.getString(R.string.collection_trip_document_field_creator)
            ) as DocumentReference

            val creator = sharedFunctions.assembleFriend(tripCreatorDocRef, source)
            if (creator is DataResult.Error) {
                return creator
            }

            val name = getTripName(tripDocRef)
            if (name is DataResult.Error) {
                return name
            }

            val members = getTripMembers(tripDocRef)
            if (members is DataResult.Error) {
                return members
            }

            DataResult.Success(
                Trip(
                    (name as DataResult.Success).data,
                    (creator as DataResult.Success).data,
                    (members as DataResult.Success).data,
                    tripDocRef
                )
            )
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getTripName(tripDocRef: DocumentReference): DataResult<String> {
        val name = tripDocRef.get(source).await().get(
            appContext.getString(R.string.collection_trip_document_field_name)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(name)
    }

    override suspend fun getTripMembers(tripDocRef: DocumentReference): DataResult<List<Friend>> {
        val tripMembersSnapshot = tripDocRef.get().await().get(
            appContext.getString(R.string.collection_trip_document_field_members)
        ) as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

        return DataResult.Success(
            buildList {
                tripMembersSnapshot.forEach { tripMemberDocRef ->
                    val tripMember = sharedFunctions.assembleFriend(tripMemberDocRef, source)
                    if (tripMember is DataResult.Error) {
                        return tripMember
                    }
                    this.add((tripMember as DataResult.Success).data)
                }
            }
        )
    }

    /*
     * Get trips
     */

    override suspend fun getTrips(): DataResult<List<Trip>> {
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)
            val tripsDocRefs = userDocRef.get().await().get(
                appContext.getString(R.string.collection_users_document_field_trips)
            ) as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())
            val trips = assembleTripList(
                buildList {
                    tripsDocRefs.forEach { tripDocRef ->
                        this.add(tripDocRef)
                    }
                }
            )
            if (trips is DataResult.Error) {
                return trips
            }

            DataResult.Success((trips as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getAdminTrips
     */

    override suspend fun getAdminTrips(): DataResult<List<Trip>> {
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)
            val trips = getTrips()
            if (trips is DataResult.Error) {
                return trips
            }

            DataResult.Success(
                (trips as DataResult.Success).data.filter { trip ->
                    trip.creator.docRef == userDocRef
                }
            )
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getPassengerTrips
     */

    override suspend fun getPassengerTrips(): DataResult<List<Trip>> {
        return try {
            val userDocRef = sharedFunctions.getUserDocRef(null)
            val trips = getTrips()
            if (trips is DataResult.Error) {
                return trips
            }

            DataResult.Success(
                (trips as DataResult.Success).data.filter { trip ->
                    trip.creator.docRef != userDocRef
                }
            )
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * assembleTripList
     */

    override suspend fun assembleTripList(
        userTripsDocRefs: List<DocumentReference>
    ): DataResult<List<Trip>> {
        return DataResult.Success(
            buildList {
                userTripsDocRefs.forEach { tripDocRef ->
                    val trip = getTrip(tripDocRef)
                    if (trip is DataResult.Error) {
                        return trip
                    }
                    this.add((trip as DataResult.Success).data)
                }
            }
        )
    }

    /*
     * updateTripName
     */

    override suspend fun updateTripName(
        trip: Trip,
        newName: String
    ): DataResult<String> {
        return try {
            trip.docRef.update(
                appContext.getString(R.string.collection_trip_document_field_name),
                newName
            ).await()
            DataResult.Success(FirebaseSuccessMessages.TRIP_NAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * addTripMembers
     */

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
            }
            DataResult.Success(FirebaseSuccessMessages.TRIP_MEMBERS_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * removeTripMembers
     */

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
            }
            DataResult.Success(FirebaseSuccessMessages.TRIP_MEMBERS_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * deleteTrip
     */

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
