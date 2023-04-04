package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.databaseFieldNames.CollectionNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionTripDocumentFieldNames
import com.example.spender.data.firebase.databaseFieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.interfaces.TripRepositoryInterface
import com.example.spender.data.firebase.interfaces.UserRepositoryInterface
import com.example.spender.data.firebase.models.Friend
import com.example.spender.data.firebase.models.Trip
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TripRepository : TripRepositoryInterface {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val tripCollection by lazy { db.collection(CollectionNames.TRIP) }

    override suspend fun createTrip(
        name: String,
        creatorDocRef: DocumentReference,
        members: List<Friend>,
    ): Result<Boolean> {
        return try {
            val batch = db.batch()
            val newTripDocRef = tripCollection.document()

            batch.update(
                newTripDocRef,
                CollectionTripDocumentFieldNames.NAME,
                name
            )

            batch.update(
                newTripDocRef,
                CollectionTripDocumentFieldNames.CREATOR,
                creatorDocRef
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

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun getTrip(tripDocRef: DocumentReference): Result<Trip> {
        return try {
            val trip = tripDocRef.get().await().toObject(Trip::class.java)
            Result.Success(trip!!)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun getTripName(tripDocRef: DocumentReference): Result<String> {
        return try {
            val name =
                tripDocRef.get().await().data?.get(CollectionTripDocumentFieldNames.NAME) as String
            Result.Success(name)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun getTripCreator(tripDocRef: DocumentReference): Result<Friend> {
        return try {
            val creatorDocRef = tripDocRef.get()
                .await().data?.get(CollectionTripDocumentFieldNames.CREATOR) as DocumentReference

            when (val nameResult =
                UserRepositoryInterface.getUserName(
                    creatorDocRef.id,
                    db.collection(CollectionNames.USER)
                )
            ) {
                is Result.Success -> {
                    when (val nicknameResult = UserRepositoryInterface.getUserNickname(
                        creatorDocRef.id,
                        db.collection(CollectionNames.USER)
                    )
                    ) {
                        is Result.Success -> {
                            val creator =
                                Friend(nameResult.data, nicknameResult.data, creatorDocRef)
                            Result.Success(creator)
                        }
                        is Result.Error -> {
                            Result.Error("Unknown error")
                        }
                    }
                }
                is Result.Error -> {
                    Result.Error("Unknown error")
                }
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun getTripMembers(tripDocRef: DocumentReference): Result<List<Friend>> {
        return try {
            val members = tripDocRef.get()
                .await().data?.get(CollectionTripDocumentFieldNames.MEMBERS) as List<DocumentReference>

            val lst: MutableList<Friend> = mutableListOf()
            members.forEach { memberDocRef ->
                when (val nameResult =
                    UserRepositoryInterface.getUserName(
                        memberDocRef.id,
                        db.collection(CollectionNames.USER)
                    )
                ) {
                    is Result.Success -> {
                        when (val nicknameResult = UserRepositoryInterface.getUserNickname(
                            memberDocRef.id,
                            db.collection(CollectionNames.USER)
                        )
                        ) {
                            is Result.Success -> {
                                val member =
                                    Friend(nameResult.data, nicknameResult.data, memberDocRef)
                                lst.add(member)
                            }
                            is Result.Error -> {
                                Result.Error("Unknown error")
                            }
                        }
                    }
                    is Result.Error -> {
                        Result.Error("Unknown error")
                    }
                }
            }

            Result.Success(lst)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun getTripsByUserId(userID: String): Result<List<Trip>> {
        return try {
            val tripsQuery = tripCollection.whereEqualTo(
                CollectionTripDocumentFieldNames.CREATOR,
                userID
            )
            val tripsDocSnapshots = tripsQuery.get().await().documents

            val lst: MutableList<Trip> = mutableListOf()
            tripsDocSnapshots.forEach { tripDocSnapshot ->
                val trip = tripDocSnapshot.toObject(Trip::class.java)!!
                lst.add(trip)
            }

            Result.Success(lst)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun updateTripName(
        tripDocRef: DocumentReference,
        newName: String
    ): Result<Boolean> {
        return try {
            tripDocRef.update(CollectionTripDocumentFieldNames.NAME, newName)
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun addTripMember(
        tripDocRef: DocumentReference,
        newMember: DocumentReference
    ): Result<Boolean> {
        return try {
            tripDocRef.update(
                CollectionTripDocumentFieldNames.MEMBERS,
                FieldValue.arrayUnion(newMember)
            )
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun addTripMembers(
        tripDocRef: DocumentReference,
        newMembers: List<DocumentReference>
    ): Result<Boolean> {
        return try {
            newMembers.forEach { member ->
                tripDocRef.update(
                    CollectionTripDocumentFieldNames.MEMBERS,
                    FieldValue.arrayUnion(member)
                )
            }
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun deleteTripMember(
        tripDocRef: DocumentReference,
        member: DocumentReference
    ): Result<Boolean> {
        return try {
            tripDocRef.update(
                CollectionTripDocumentFieldNames.MEMBERS,
                FieldValue.arrayRemove(member)
            )

            TODO("Delete and reorganize spends")

            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun deleteTripMembers(
        tripDocRef: DocumentReference,
        members: List<DocumentReference>
    ): Result<Boolean> {
        return try {
            members.forEach { member ->
                tripDocRef.update(
                    CollectionTripDocumentFieldNames.MEMBERS,
                    FieldValue.arrayRemove(member)
                )

                TODO("Delete and reorganize spends")

            }
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }

    override suspend fun deleteTrip(tripDocRef: DocumentReference): Result<Boolean> {
        return try {
            val batch = db.batch()

            when(val resultTripMembers = getTripMembers(tripDocRef)) {
                is Result.Success -> {
                    resultTripMembers.data.forEach() { member ->
                        batch.update(
                            member.docRef,
                            CollectionUserDocumentFieldNames.TRIPS,
                            FieldValue.arrayRemove(tripDocRef)
                        )
                    }
                }
                is Result.Error -> {
                    Result.Error(resultTripMembers.exception)
                }
            }

            TODO("Delete and reorganize spends")

            batch.delete(tripDocRef)

            batch.commit()
            Result.Success(true)
        } catch (e: Exception) {
            when (e) {
                is FirebaseNetworkException -> Result.Error("Network error")
                else -> Result.Error("Unknown error")
            }
        }
    }
}