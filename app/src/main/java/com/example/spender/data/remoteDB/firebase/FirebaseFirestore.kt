package com.example.spender.data.remoteDB.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

object CollectionNames {
    const val USER = "Users"
    const val TRIP = "Trips"
}

object CollectionUserDocumentFieldNames {
    object FieldNameKeys {
        const val FIRST_NAME: String = "firstName"
        const val MIDDLE_NAME: String = "middleName"
        const val LAST_NAME: String = "lastName"
    }

    const val NAME = "Name"
    const val AGE = "Age"
    const val NICKNAME = "Nickname"
    const val INCOMING_FRIENDS = "IncomingFriends" // Array<DocumentReference>
    const val OUTGOING_FRIENDS = "OutgoingFriends" // Array<DocumentReference>
    const val FRIENDS = "Friends" // Array<DocumentReference>
    const val TRIPS = "Trips" // Array<DocumentReference>
}

object CollectionTripDocumentFieldNames {
    const val NAME = "Name"
    const val CREATOR = "Creator" // DocumentReference
    const val MEMBERS = "Members" // Array<DocumentReference>
    const val SubCollectionSpends = "Spends"
}

object SubCollectionSpendsDocumentFieldNames {
    const val NAME = "Name"
    const val CATEGORY = "Category"
    const val MEMBERS_SPEND_CATEGORY = "MembersSpend" // map {"<UserDocRef>" to "Amount"}
}

@Singleton
class FirebaseFirestoreManager @Inject constructor() {
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val userManager by lazy { UserManager() }
    private val tripManager by lazy { TripManager() }

    inner class UserManager {

        // Creators

        fun createUser(
            @NotEmpty userID: String, // from Auth
            onSuccess: (DocumentReference?) -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            db.collection(CollectionNames.USER).document(userID).get()
                .addOnSuccessListener { userDocSnapshot -> onSuccess(userDocSnapshot.reference) }
                .addOnFailureListener { e -> onFailure(e) }
        }

        // Getters

        fun getUserName(
            userDocRef: DocumentReference,
            onSuccess: (HashMap<String, String>?) -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            userDocRef.get()
                .addOnSuccessListener { userDoc ->
                    val name =
                        userDoc.data?.get(CollectionUserDocumentFieldNames.NAME) as HashMap<String, String>?
                    onSuccess(name)
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }

        fun getUserAge(
            userDocRef: DocumentReference,
            onSuccess: (Int?) -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            userDocRef.get()
                .addOnSuccessListener { userDoc ->
                    val age = userDoc.data?.get(CollectionUserDocumentFieldNames.AGE) as Int?
                    onSuccess(age)
                }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun getUserNickname(
            userDocRef: DocumentReference,
            onSuccess: (String?) -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            userDocRef.get()
                .addOnSuccessListener { userDoc ->
                    val nickname =
                        userDoc.data?.get(CollectionUserDocumentFieldNames.NICKNAME) as String?
                    onSuccess(nickname)
                }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun getUserFriends(
            userDocRef: DocumentReference,
            onSuccess: (Array<DocumentReference>?) -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            userDocRef.get()
                .addOnSuccessListener { userDoc ->
                    val friends =
                        userDoc.data?.get(CollectionUserDocumentFieldNames.FRIENDS) as Array<DocumentReference>?
                    onSuccess(friends)
                }
                .addOnFailureListener { e -> onFailure(e) }
        }

        // Updates

        fun updateUserName(
            userDocRef: DocumentReference,
            @NotEmpty newName: String,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            userDocRef.update(CollectionUserDocumentFieldNames.NAME, newName)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun updateUserAge(
            userDocRef: DocumentReference,
            @Positive age: Int,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            userDocRef.update(CollectionUserDocumentFieldNames.AGE, age)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun updateUserNickname(
            userDocRef: DocumentReference,
            @NotEmpty nickname: String,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            userDocRef.update(CollectionUserDocumentFieldNames.NICKNAME, nickname)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun addUserOutgoingFriend(
            userDocRef: DocumentReference,
            friendDocRef: DocumentReference,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            val batch = db.batch()

            // Remove the friendID from the user's friend list
            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayUnion(friendDocRef)
            )

            // Remove the userID from the friend's friend list
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayUnion(userDocRef)
            )

            batch.commit()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun addUserIncomingFriend(
            userDocRef: DocumentReference,
            friendDocRef: DocumentReference,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            val batch = db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayUnion(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayUnion(userDocRef)
            )

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.INCOMING_FRIENDS,
                FieldValue.arrayRemove(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.OUTGOING_FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        // Deletes

        fun deleteUserFriend(
            userDocRef: DocumentReference,
            friendDocRef: DocumentReference,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            val batch = db.batch()

            batch.update(
                userDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayRemove(friendDocRef)
            )
            batch.update(
                friendDocRef,
                CollectionUserDocumentFieldNames.FRIENDS,
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun deleteUserFriends(
            userDocRef: DocumentReference,
            @NotEmpty friendDocRefs: List<DocumentReference>,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            val batch = db.batch()

            friendDocRefs.forEach { friendDocRef ->
                batch.update(
                    userDocRef,
                    CollectionUserDocumentFieldNames.FRIENDS,
                    FieldValue.arrayRemove(friendDocRef)
                )
                batch.update(
                    friendDocRef,
                    CollectionUserDocumentFieldNames.FRIENDS,
                    FieldValue.arrayRemove(userDocRef)
                )
            }

            batch.commit()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    inner class TripManager {

        // Creators

        fun createTrip(
            creatorDocRef: DocumentReference,
            @NotEmpty name: String,
            @NotEmpty members: ArrayList<DocumentReference>, // Members include creator
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            val batch = db.batch()
            val tripDocRef = db.collection(CollectionNames.TRIP).document()

            // Create trip
            batch.update(
                tripDocRef,
                CollectionTripDocumentFieldNames.CREATOR,
                creatorDocRef
            )

            batch.update(
                tripDocRef,
                CollectionTripDocumentFieldNames.NAME,
                name
            )

            batch.update(
                tripDocRef,
                CollectionTripDocumentFieldNames.MEMBERS,
                FieldValue.arrayUnion(*members.toTypedArray())
            )

            members.forEach { member ->
                batch.update(
                    member,
                    CollectionUserDocumentFieldNames.TRIPS,
                    FieldValue.arrayUnion(tripDocRef)
                )
            }

            batch.commit()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        // Getters

        fun getTripName(
            tripDocRef: DocumentReference,
            onSuccess: (String?) -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            tripDocRef.get()
                .addOnSuccessListener { tripDoc ->
                    val name =
                        tripDoc.data?.get(CollectionTripDocumentFieldNames.NAME) as String?
                    onSuccess(name)
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }

        fun getTripCreator(
            tripDocRef: DocumentReference,
            onSuccess: (DocumentReference?) -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            tripDocRef.get()
                .addOnSuccessListener { tripDoc ->
                    val creator =
                        tripDoc.data?.get(CollectionTripDocumentFieldNames.NAME) as DocumentReference?
                    onSuccess(creator)
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }

        fun getTripMembers(
            tripDocRef: DocumentReference,
            onSuccess: (List<DocumentReference>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            tripDocRef.get()
                .addOnSuccessListener { tripDoc ->
                    val lst = tripDoc.data?.get(CollectionTripDocumentFieldNames.MEMBERS)
                    onSuccess(lst as List<DocumentReference>)
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }

        // Updates

        fun updateTripName(
            tripDocRef: DocumentReference,
            @NotEmpty newName: String,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            tripDocRef.update(CollectionTripDocumentFieldNames.NAME, newName)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

    }

    inner class SpendManager {

        // Creators

        fun createSpend(
            tripDocRef: DocumentReference,
            @NotEmpty name: String,
            @NotEmpty category: String = "No category",
            @NotEmpty payers: Map<DocumentReference, Double>,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            val batch = db.batch()

            val categoryDocRef =
                tripDocRef.collection(CollectionTripDocumentFieldNames.SubCollectionSpends)
                    .document()

            batch.update(
                categoryDocRef,
                SubCollectionSpendsDocumentFieldNames.NAME,
                name
            )

            batch.update(
                categoryDocRef,
                SubCollectionSpendsDocumentFieldNames.CATEGORY,
                category
            )

            for (payer in payers) {
                batch.update(
                    categoryDocRef,
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
            categoryDocRef: DocumentReference,
            @NotEmpty newName: String,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            categoryDocRef.update(SubCollectionSpendsDocumentFieldNames.NAME, newName)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun updateCategory(
            categoryDocRef: DocumentReference,
            @NotEmpty newCategory: String,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            categoryDocRef.update(SubCollectionSpendsDocumentFieldNames.CATEGORY, newCategory)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

        fun updateMemberSpend(
            categoryDocRef: DocumentReference,
            newSpend: Pair<String, Double>,
            onSuccess: () -> Unit = {},
            onFailure: (Exception) -> Unit = {}
        ) {
            categoryDocRef.update(
                "${SubCollectionSpendsDocumentFieldNames.MEMBERS_SPEND_CATEGORY}.${newSpend.first}",
                newSpend.second
            )
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }

    }

    // Queries

    fun checkIfCurrentUserIsCreatorOfTrip(
        tripDocRef: DocumentReference,
        @NotEmpty userID: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        tripDocRef.get()
            .addOnSuccessListener { tripDocSnapshot ->
                val creatorID = tripDocSnapshot.get(CollectionTripDocumentFieldNames.CREATOR)
                if (userID == creatorID) {
                    onSuccess()
                } else {
                    onFailure(NoSuchFieldException())
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

}