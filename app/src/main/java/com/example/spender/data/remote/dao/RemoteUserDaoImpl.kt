package com.example.spender.data.remote.dao

import android.app.Application
import com.example.spender.R
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.messages.exceptions.FirebaseAddMyselfFriendException
import com.example.spender.data.messages.exceptions.FirebaseAlreadyFriendException
import com.example.spender.data.messages.exceptions.FirebaseAlreadySentFriendException
import com.example.spender.data.messages.exceptions.FirebaseNicknameException
import com.example.spender.data.messages.exceptions.FirebaseNicknameLengthException
import com.example.spender.data.messages.exceptions.FirebaseUndefinedException
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.DebtToUser
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.spend.SpendMember
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.user.UserName
import com.example.spender.domain.remotedao.RemoteUserDao
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class RemoteUserDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : RemoteUserDao {
    override var source: Source = Source.SERVER

    private fun getUserDocRef(userId: String?): DocumentReference {
        val userID = userId ?: remoteDataSource.auth.currentUser?.uid.toString()
        return remoteDataSource.db.collection(
            appContext.getString(
                R.string.collection_name_users
            )
        ).document(userID)
    }

    /*
     * getUser
     */

    override suspend fun getUser(): DataResult<User> {
        return try {
            val user = assembleUser()
            if (user is DataResult.Error) {
                return user
            }
            DataResult.Success((user as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleUser(): DataResult<User> {
        val name = getUserName(null)
        if (name is DataResult.Error) {
            return name
        }
        val age = getUserAge()
        if (age is DataResult.Error) {
            return age
        }
        val nickname = getUserNickname(null)
        if (nickname is DataResult.Error) {
            return nickname
        }
        val incomingFriends = getUserIncomingFriends()
        if (incomingFriends is DataResult.Error) {
            return incomingFriends
        }
        val outgoingFriends = getUserOutgoingFriends()
        if (outgoingFriends is DataResult.Error) {
            return outgoingFriends
        }
        val friends = getUserFriends()
        if (friends is DataResult.Error) {
            return friends
        }
        val adminTrips = getUserAdminTrips()
        if (adminTrips is DataResult.Error) {
            return adminTrips
        }
        val passengerTrips = getUserPassengerTrips()
        if (passengerTrips is DataResult.Error) {
            return passengerTrips
        }

        return DataResult.Success(
            User(
                name = (name as DataResult.Success).data,
                age = (age as DataResult.Success).data,
                nickname = (nickname as DataResult.Success).data,
                incomingFriends = (incomingFriends as DataResult.Success).data,
                outgoingFriends = (outgoingFriends as DataResult.Success).data,
                friends = (friends as DataResult.Success).data,
                adminTrips = (adminTrips as DataResult.Success).data,
                passengerTrips = (passengerTrips as DataResult.Success).data,
                docRef = getUserDocRef(null)
            )
        )
    }

    /*
     * getUserName
     */

    override suspend fun getUserName(userId: String?): DataResult<UserName> {
        return try {
            val userName = assembleUserName(userId)
            if (userName is DataResult.Error) {
                return userName
            }
            DataResult.Success((userName as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleUserName(userId: String?): DataResult<UserName> {
        val userDocRef = getUserDocRef(userId)
        val firstName = userDocRef.get(source).await().data!![appContext.getString(
            R.string.collection_users_document_field_first_name
        )] as String
        val middleName = userDocRef.get(source).await().data!![appContext.getString(
            R.string.collection_users_document_field_middle_name
        )] as String
        val lastName = userDocRef.get(source).await().data!![appContext.getString(
            R.string.collection_users_document_field_last_name
        )] as String
        return DataResult.Success(UserName(firstName, middleName, lastName))
    }

    /*
     * getUserAge
     */

    override suspend fun getUserAge(): DataResult<Long> {
        return try {
            val age = getUserDocRef(null).get(source).await().data!![appContext.getString(
                R.string.collection_users_document_field_age
            )] as Long
            DataResult.Success(age)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserNickname
     */

    override suspend fun getUserNickname(userId: String?): DataResult<String> {
        return try {
            val nickname = getUserDocRef(userId).get(source).await().data!![appContext.getString(
                R.string.collection_users_document_field_nickname
            )] as String
            DataResult.Success(nickname)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserIncomingFriends
     */

    override suspend fun getUserIncomingFriends(): DataResult<List<Friend>> {
        return try {
            val friendsDocRefs = getUserDocRef(null).get(source).await()
                .data!![appContext.getString(
                R.string.collection_users_document_field_incoming_friends
            )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = assembleFriendsList(friendsDocRefs)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleFriendsList(
        friendsDocRefs: ArrayList<DocumentReference>
    ): DataResult<List<Friend>> {
        return DataResult.Success(
            buildList {
                friendsDocRefs.forEach { friendDocRef ->
                    val friend = assembleFriend(friendDocRef)
                    if (friend is DataResult.Error) {
                        return friend
                    }
                    this.add((friend as DataResult.Success).data)
                }
            }
        )
    }

    private suspend fun assembleFriend(friendDocRef: DocumentReference): DataResult<Friend> {
        val resultName = getUserName(friendDocRef.id)
        if (resultName is DataResult.Error) {
            return resultName
        }
        val resultNickname = getUserNickname(friendDocRef.id)
        if (resultNickname is DataResult.Error) {
            return resultNickname
        }
        return DataResult.Success(
            Friend(
                name = (resultName as DataResult.Success).data,
                nickname = (resultNickname as DataResult.Success).data,
                docRef = friendDocRef
            )
        )
    }

    /*
     * getUserOutgoingFriends
     */

    override suspend fun getUserOutgoingFriends(): DataResult<List<Friend>> {
        return try {
            val friendsDocRefs = getUserDocRef(null).get(source).await()
                .data!![appContext.getString(
                R.string.collection_users_document_field_outgoing_friends
            )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = assembleFriendsList(friendsDocRefs)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserFriends
     */

    override suspend fun getUserFriends(): DataResult<List<Friend>> {
        return try {
            val friendsDocRefs =
                getUserDocRef(null).get(source).await().data!![appContext.getString(
                    R.string.collection_users_document_field_friends
                )] as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val friends = assembleFriendsList(friendsDocRefs)
            if (friends is DataResult.Error) {
                return friends
            }

            DataResult.Success((friends as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * getUserTrips
     */

    override suspend fun getUserTrips(): DataResult<List<Trip>> {
        return try {
            val userTripsDocRefs = getUserDocRef(null).get(source).await().get(
                appContext.getString(R.string.collection_users_document_field_trips)
            ) as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

            val trips = assembleTripList(userTripsDocRefs)
            if (trips is DataResult.Error) {
                return trips
            }

            DataResult.Success((trips as DataResult.Success).data)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun assembleTripList(
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
     * getUserAdminTrips
     */

    override suspend fun getUserAdminTrips(): DataResult<List<Trip>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()

            val tripCollection: CollectionReference = remoteDataSource.db.collection(
                appContext.getString(R.string.collection_name_trips)
            )

            val tripsQuery = tripCollection.whereEqualTo(
                appContext.getString(R.string.collection_trip_document_field_creator),
                userID
            ).get(source).await()

            if (tripsQuery.isEmpty) {
                return DataResult.Success(emptyList())
            }

            val trips = assembleTripList(
                buildList {
                    tripsQuery.documents.forEach { tripDocRef ->
                        this.add(tripDocRef.reference)
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
     * getUserPassengerTrips
     */

    override suspend fun getUserPassengerTrips(): DataResult<List<Trip>> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()

            val tripCollection: CollectionReference = remoteDataSource.db.collection(
                appContext.getString(R.string.collection_name_trips)
            )

            val tripsQuery = tripCollection.whereNotEqualTo(
                appContext.getString(R.string.collection_trip_document_field_creator),
                userID
            )
            val tripsQuerySnapshot = tripsQuery.whereArrayContains(
                appContext.getString(R.string.collection_trip_document_field_members),
                userID
            ).get(source).await()

            if (tripsQuerySnapshot.isEmpty) {
                return DataResult.Success(emptyList())
            }

            val trips = assembleTripList(
                buildList {
                    tripsQuerySnapshot.documents.forEach { tripDocRef ->
                        this.add(tripDocRef.reference)
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
     * updateUser
     */

    override suspend fun updateUser(
        newUser: User
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            remoteDataSource.db.collection(appContext.getString(R.string.collection_name_users))
                .document(userID).set(newUser).await()
            DataResult.Success(FirebaseSuccessMessages.USER_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * updateUserName
     */

    override suspend fun updateUserName(
        newName: UserName
    ): DataResult<String> {
        return try {
            val userDocRef = getUserDocRef(null)
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_first_name),
                newName.firstName
            ).await()
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_middle_name),
                newName.middleName
            ).await()
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_last_name),
                newName.lastName
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_NAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * updateUserAge
     */

    override suspend fun updateUserAge(
        newAge: Int,
    ): DataResult<String> {
        return try {
            val userDocRef = getUserDocRef(null)
            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_age),
                newAge
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_AGE_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * updateUserNickname
     */

    override suspend fun updateUserNickname(
        newNickname: String,
    ): DataResult<String> {
        return try {
            val userDocRef = getUserDocRef(null)

            val checkNicknameResult = checkNickname(newNickname)
            if (checkNicknameResult is DataResult.Error) {
                return checkNicknameResult
            }

            userDocRef.update(
                appContext.getString(R.string.collection_users_document_field_nickname),
                newNickname
            ).await()
            DataResult.Success(FirebaseSuccessMessages.USER_NICKNAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * addUserIncomingFriend
     */

    override suspend fun addUserIncomingFriend(
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userDocRef = getUserDocRef(null)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayUnion(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayUnion(userDocRef)
            )

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_INCOMING_FRIEND_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    /*
     * addUserOutgoingFriend
     */

    override suspend fun addUserOutgoingFriend(
        nickname: String
    ): DataResult<String> {

        // nickname Exists
        val nicknameExists = checkExistNickname(nickname)
        if (nicknameExists is DataResult.Error) {
            return nicknameExists
        }

        val userDocRef = getUserDocRef(null)
        val friendDocRef = (nicknameExists as DataResult.Success).data

        // already friend
        val alreadyFriend = checkAlreadyFriend(friendDocRef)
        if (alreadyFriend is DataResult.Error) {
            return alreadyFriend
        }

        // me
        if (userDocRef == friendDocRef) {
            return DataErrorHandler.handle(FirebaseAddMyselfFriendException())
        }

        // already incoming
        val alreadyIncoming = checkAlreadyIncoming(friendDocRef)
        if (alreadyIncoming is DataResult.Error) {
            return alreadyIncoming
        }
        if ((alreadyIncoming as DataResult.Success).data != null) {
            return addUserIncomingFriend((alreadyIncoming).data!!)
        }

        // already outgoing
        val alreadyOutgoing = checkAlreadyOutgoing(userDocRef)
        if (alreadyOutgoing is DataResult.Error) {
            return alreadyOutgoing
        }

        return try {
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayUnion(friendDocRef)
            )

            batch.update(
                friendDocRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayUnion(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun checkExistNickname(
        nickname: String
    ): DataResult<DocumentReference> {
        return try {
            val querySnapshot = remoteDataSource.db.collection(
                appContext.getString(R.string.collection_name_users)
            ).whereEqualTo(
                appContext.getString(R.string.collection_users_document_field_nickname),
                nickname
            ).get().await()
            if (querySnapshot.documents.isEmpty()) {
                return DataErrorHandler.handle(FirebaseNicknameException())
            }
            DataResult.Success(querySnapshot.documents[0].reference)
        } catch (e: Exception) {
            return DataErrorHandler.handle(e)
        }
    }

    private suspend fun checkAlreadyFriend(
        friendDocRef: DocumentReference
    ): DataResult<Boolean> {
        val friendsResult = getUserFriends()
        if (friendsResult is DataResult.Error) {
            return friendsResult
        }

        if ((friendsResult as DataResult.Success).data.find {
                it.docRef == friendDocRef
            } != null
        ) {
            return DataErrorHandler.handle(FirebaseAlreadyFriendException())
        }
        return DataResult.Success(false)
    }

    private suspend fun checkAlreadyIncoming(
        friendDocRef: DocumentReference
    ): DataResult<Friend?> {
        val incomingFriendsResult = getUserIncomingFriends()
        if (incomingFriendsResult is DataResult.Error) {
            return incomingFriendsResult
        }

        (incomingFriendsResult as DataResult.Success).data.forEach {
            if (it.docRef == friendDocRef) {
                return DataResult.Success(it)
            }
        }
        return DataResult.Success(null)
    }

    private suspend fun checkAlreadyOutgoing(
        friendDocRef: DocumentReference
    ): DataResult<Boolean> {
        val outgoingFriendsResult = getUserOutgoingFriends()
        if (outgoingFriendsResult is DataResult.Error) {
            return outgoingFriendsResult
        }

        (outgoingFriendsResult as DataResult.Success).data.forEach {
            if (it.docRef == friendDocRef) {
                return DataErrorHandler.handle(FirebaseAlreadySentFriendException())
            }
        }
        return DataResult.Success(true)
    }

    /*
     * removeUserFriend
     */

    override suspend fun removeUserFriend(
        friend: Friend,
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_FRIEND_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserOutgoingFriend(
        friend: Friend
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_OUTGOING_FRIEND_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun removeUserIncomingFriend(
        friend: Friend
    ): DataResult<String> {
        return try {
            val userID = remoteDataSource.auth.currentUser?.uid.toString()
            val userDocRef = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).document(userID)
            val batch = remoteDataSource.db.batch()

            batch.update(
                userDocRef,
                appContext.getString(R.string.collection_users_document_field_incoming_friends),
                FieldValue.arrayRemove(friend.docRef)
            )
            batch.update(
                friend.docRef,
                appContext.getString(R.string.collection_users_document_field_outgoing_friends),
                FieldValue.arrayRemove(userDocRef)
            )

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.USER_INCOMING_FRIEND_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun checkNickname(
        nickname: String
    ): DataResult<Boolean> {
        if (nickname.length < 6) {
            return DataErrorHandler.handle(FirebaseNicknameLengthException())
        }
        return try {
            val checkNicknameQuerySnapshot = remoteDataSource.db.collection(
                appContext.getString(
                    R.string.collection_name_users
                )
            ).whereEqualTo(
                appContext.getString(R.string.collection_users_document_field_nickname),
                nickname
            ).get(source).await()
            if (!checkNicknameQuerySnapshot.isEmpty) {
                DataErrorHandler.handle(FirebaseNicknameException())
            }
            DataResult.Success(true)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun getTrip(tripDocRef: DocumentReference): DataResult<Trip> {
        return try {
            val tripDocSnapshot = tripDocRef.get(source).await()
            val tripCreatorDocRef = tripDocSnapshot.get(
                appContext.getString(R.string.collection_trip_document_field_creator)
            ) as DocumentReference

            val creator = assembleFriend(tripCreatorDocRef)
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

            val spends = getTripSpends(tripDocRef)
            if (spends is DataResult.Error) {
                return spends
            }

            DataResult.Success(
                Trip(
                    (name as DataResult.Success).data,
                    (creator as DataResult.Success).data,
                    (members as DataResult.Success).data,
                    (spends as DataResult.Success).data,
                    tripDocRef
                )
            )
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private suspend fun getTripName(tripDocRef: DocumentReference): DataResult<String> {
        val name = tripDocRef.get(source).await().get(
            appContext.getString(R.string.collection_trip_document_field_name)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(name)
    }

    private suspend fun getTripMembers(tripDocRef: DocumentReference): DataResult<List<Friend>> {
        val tripMembersSnapshot = tripDocRef.get().await().get(
            appContext.getString(R.string.collection_trip_document_field_members)
        ) as ArrayList<DocumentReference>? ?: return DataResult.Success(emptyList())

        return DataResult.Success(
            buildList {
                tripMembersSnapshot.forEach { tripMemberDocRef ->
                    val tripMember = assembleFriend(tripMemberDocRef)
                    if (tripMember is DataResult.Error) {
                        return tripMember
                    }
                    this.add((tripMember as DataResult.Success).data)
                }
            }
        )
    }

    private suspend fun getTripSpends(tripDocRef: DocumentReference): DataResult<List<Spend>> {
        val spendsDocumentsSnapshot = tripDocRef.collection(
            appContext.getString(R.string.collection_trip_document_field_spends)
        ).get().await().documents
        if (spendsDocumentsSnapshot.isEmpty()) {
            return DataResult.Success(emptyList())
        }
        return DataResult.Success(
            buildList {
                spendsDocumentsSnapshot.forEach { spendDocRef ->
                    val spend = assembleSpend(spendDocRef.reference)
                    if (spend is DataResult.Error) {
                        return spend
                    }
                    this.add((spend as DataResult.Success).data)
                }
            }
        )
    }

    private suspend fun assembleSpend(spendDocRef: DocumentReference): DataResult<Spend> {
        val name = getSpendName(spendDocRef)
        if (name is DataResult.Error) {
            return name
        }

        val category = getSpendCategory(spendDocRef)
        if (category is DataResult.Error) {
            return category
        }

        val splitMode = getSpendSplitMode(spendDocRef)
        if (splitMode is DataResult.Error) {
            return splitMode
        }

        val amount = getSpendAmount(spendDocRef)
        if (amount is DataResult.Error) {
            return amount
        }

        val geoPoint = getSpendGeoPoint(spendDocRef)
        if (geoPoint is DataResult.Error) {
            return geoPoint
        }

        val members = getSpendMembers(spendDocRef)
        if (members is DataResult.Error) {
            return members
        }

        return DataResult.Success(
            Spend(
                (name as DataResult.Success).data,
                (category as DataResult.Success).data,
                (splitMode as DataResult.Success).data,
                (amount as DataResult.Success).data,
                (geoPoint as DataResult.Success).data,
                (members as DataResult.Success).data,
                spendDocRef
            )
        )
    }

    private suspend fun getSpendName(spendDocRef: DocumentReference): DataResult<String> {
        val spendName = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_name)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendName)
    }

    private suspend fun getSpendCategory(spendDocRef: DocumentReference): DataResult<String> {
        val spendCategory = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_category)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendCategory)
    }

    private suspend fun getSpendSplitMode(spendDocRef: DocumentReference): DataResult<String> {
        val spendSplitMode = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_split_mode)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendSplitMode)
    }

    private suspend fun getSpendAmount(spendDocRef: DocumentReference): DataResult<Double> {
        val spendAmount = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_amount)
        ) as Double? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendAmount)
    }

    private suspend fun getSpendGeoPoint(spendDocRef: DocumentReference): DataResult<GeoPoint> {
        val spendGeoPoint = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_geo)
        ) as GeoPoint? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendGeoPoint)
    }

    private suspend fun getSpendMembers(
        spendDocRef: DocumentReference
    ): DataResult<List<SpendMember>> {
        val spendMembersSnapshot = spendDocRef.collection(
            appContext.getString(R.string.collection_name_spend_member)
        ).get().await().documents
        if (spendMembersSnapshot.isEmpty()) {
            return DataResult.Success(emptyList())
        }
        return DataResult.Success(
            buildList {
                spendMembersSnapshot.forEach { spendMemberDocRef ->
                    val spendMember = assembleSpendMember(spendMemberDocRef.reference)
                    if (spendMember is DataResult.Error) {
                        return spendMember
                    }
                    this.add((spendMember as DataResult.Success).data)
                }
            }
        )
    }

    private suspend fun assembleSpendMember(
        spendMemberDocRef: DocumentReference
    ): DataResult<SpendMember> {
        val friendDocRed = spendMemberDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spend_member_document_field_member)
        ) as DocumentReference? ?: return DataErrorHandler.handle(FirebaseUndefinedException())

        val friend = assembleFriend(friendDocRed)
        if (friend is DataResult.Error) {
            return friend
        }

        val payment = getSpendMemberPayment(spendMemberDocRef)
        if (payment is DataResult.Error) {
            return payment
        }

        val debtsToUsers = getDebtsToUsers(spendMemberDocRef)
        if (debtsToUsers is DataResult.Error) {
            return debtsToUsers
        }

        return DataResult.Success(
            SpendMember(
                (friend as DataResult.Success).data,
                (payment as DataResult.Success).data,
                (debtsToUsers as DataResult.Success).data,
            )
        )
    }

    private suspend fun getSpendMemberPayment(
        spendMemberDocRef: DocumentReference
    ): DataResult<Double> {
        val payment = spendMemberDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spend_member_document_field_payment)
        ) as Double? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(payment)
    }

    private suspend fun getDebtsToUsers(
        spendMemberDocRef: DocumentReference
    ): DataResult<List<DebtToUser>> {
        val spendDebtsToUsers = spendMemberDocRef.get().await().get(
            appContext.getString(R.string.collection_spend_member_document_field_debt)
        ) as HashMap<String, Double>? ?: return DataResult.Success(emptyList())

        return DataResult.Success(
            buildList {
                spendDebtsToUsers.keys.forEach { key ->
                    val debtToUser = assembleDebtToUser(spendDebtsToUsers, key)
                    if (debtToUser is DataResult.Error) {
                        return debtToUser
                    }
                    this.add((debtToUser as DataResult.Success).data)
                }
            }
        )
    }

    private suspend fun assembleDebtToUser(
        debtToUserMap: HashMap<String, Double>,
        key: String
    ): DataResult<DebtToUser> {
        val friendDocRef = remoteDataSource.db.collection(
            appContext.getString(R.string.collection_name_users)
        ).document(key)

        val friend = assembleFriend(friendDocRef)
        if (friend is DataResult.Error) {
            return friend
        }

        return DataResult.Success(
            DebtToUser(
                (friend as DataResult.Success).data,
                debtToUserMap[key] as Double
            )
        )
    }
}
