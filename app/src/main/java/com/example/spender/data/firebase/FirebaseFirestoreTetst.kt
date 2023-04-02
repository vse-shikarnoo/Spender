package com.example.spender.data.firebase

import com.example.spender.data.firebase.dataClasses.Friend
import com.example.spender.data.firebase.dataClasses.Spend
import com.example.spender.data.firebase.dataClasses.Trip
import com.example.spender.data.firebase.dataClasses.User
import com.example.spender.data.firebase.fieldNames.CollectionNames
import com.example.spender.data.firebase.fieldNames.CollectionTripDocumentFieldNames
import com.example.spender.data.firebase.fieldNames.CollectionUserDocumentFieldNames
import com.example.spender.data.firebase.fieldNames.SubCollectionSpendsDocumentFieldNames
import com.example.spender.data.firebase.interfaces.TripRepositoryInterface
import com.example.spender.data.firebase.interfaces.UserRepositoryInterface
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
        TODO("Not yet implemented")
    }

    override suspend fun addTripMember(
        tripDocRef: DocumentReference,
        newMember: DocumentReference
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addTripMembers(
        tripDocRef: DocumentReference,
        newMember: List<DocumentReference>
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTripMember(
        tripDocRef: DocumentReference,
        member: DocumentReference
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTripMembers(
        tripDocRef: DocumentReference,
        members: List<DocumentReference>
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTrip(tripDocRef: DocumentReference): Result<Boolean> {
        TODO("Not yet implemented")
    }

}

class SpendRepository {
    private val db by lazy { FirebaseFirestore.getInstance() }

    // Creators

    fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String = "No category",
        payers: Map<DocumentReference, Double>,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val batch = db.batch()

        val spendDocRef =
            tripDocRef.collection(CollectionTripDocumentFieldNames.SubCollectionSpends)
                .document()

        batch.update(
            spendDocRef,
            SubCollectionSpendsDocumentFieldNames.NAME,
            name
        )

        batch.update(
            spendDocRef,
            SubCollectionSpendsDocumentFieldNames.CATEGORY,
            category
        )

        for (payer in payers) {
            batch.update(
                spendDocRef,
                "${SubCollectionSpendsDocumentFieldNames.MEMBERS_SPEND_CATEGORY}.${payer.key}",
                (payer.value).toString()
            )
        }

        batch.commit()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Updates

    fun updateName(
        spendDocRef: DocumentReference,
        newName: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        spendDocRef.update(SubCollectionSpendsDocumentFieldNames.NAME, newName)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateCategory(
        spendDocRef: DocumentReference,
        newCategory: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        spendDocRef.update(SubCollectionSpendsDocumentFieldNames.CATEGORY, newCategory)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateMemberSpend(
        spendDocRef: DocumentReference,
        newSpend: Pair<String, Double>,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        spendDocRef.update(
            "${SubCollectionSpendsDocumentFieldNames.MEMBERS_SPEND_CATEGORY}.${newSpend.first}",
            newSpend.second
        )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Deletes

    fun deleteSpend(
        spendDocRef: DocumentReference,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        spendDocRef.delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
