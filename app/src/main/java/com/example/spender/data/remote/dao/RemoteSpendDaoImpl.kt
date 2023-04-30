package com.example.spender.data.remote.dao

import android.app.Application
import com.example.spender.R
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.messages.exceptions.FirebaseUndefinedException
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.remotedao.RemoteSpendDao
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.spend.RemoteSpend
import com.example.spender.domain.remotemodel.spend.Spend
import com.example.spender.domain.remotemodel.spendmember.DebtToUser
import com.example.spender.domain.remotemodel.spendmember.RemoteSpendMember
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Source
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class RemoteSpendDaoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : RemoteSpendDao {
    private val sharedFunctions = SharedFunctions(remoteDataSource, appContext)
    override var source: Source = Source.SERVER

    override suspend fun createSpend(
        trip: Trip,
        spend: Spend
    ): DataResult<String> {
        return try {
            val spendDocRef = trip.docRef.collection(
                appContext.getString(R.string.collection_name_spends)
            ).document()
            spendDocRef.set(spendMap(spend)).await()

            val spendMemberCollectionRef = spendDocRef.collection(
                appContext.getString(R.string.collection_name_spend_member)
            )
            spend.members.forEach { spendMember ->
                spendMemberCollectionRef.document().set(
                    spendMemberMap(spendMember)
                )
            }

            DataResult.Success(FirebaseSuccessMessages.SPEND_CREATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    private fun spendMap(spend: Spend): Map<String, Any> {
        return mapOf(
            appContext.getString(R.string.collection_spends_document_field_name) to
                spend.name,
            appContext.getString(R.string.collection_spends_document_field_category) to
                spend.category,
            appContext.getString(R.string.collection_spends_document_field_split_mode) to
                spend.splitMode,
            appContext.getString(R.string.collection_spends_document_field_amount) to
                spend.amount,
            appContext.getString(R.string.collection_spends_document_field_geo) to
                spend.geoPoint,
        )
    }

    private fun spendMemberMap(remoteSpendMember: RemoteSpendMember): Map<String, Any> {
        return mapOf(
            appContext.getString(R.string.collection_spend_member_document_field_member) to
                remoteSpendMember.docRef,
            appContext.getString(R.string.collection_spend_member_document_field_payment) to
                remoteSpendMember.payment,
            appContext.getString(R.string.collection_spend_member_document_field_debt) to
                buildMap {
                    remoteSpendMember.debt.forEach {
                        this[it.user.docRef] = it.debt
                    }
                }
        )
    }

    override suspend fun getSpends(trip: Trip): DataResult<List<RemoteSpend>> {
        val spendsDocumentsSnapshot = trip.docRef.collection(
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

    override suspend fun assembleSpend(spendDocRef: DocumentReference): DataResult<RemoteSpend> {
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
            RemoteSpend(
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

    override suspend fun getSpendName(spendDocRef: DocumentReference): DataResult<String> {
        val spendName = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_name)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendName)
    }

    override suspend fun getSpendCategory(spendDocRef: DocumentReference): DataResult<String> {
        val spendCategory = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_category)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendCategory)
    }

    override suspend fun getSpendSplitMode(spendDocRef: DocumentReference): DataResult<String> {
        val spendSplitMode = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_split_mode)
        ) as String? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendSplitMode)
    }

    override suspend fun getSpendAmount(spendDocRef: DocumentReference): DataResult<Double> {
        val spendAmount = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_amount)
        ) as Double? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendAmount)
    }

    override suspend fun getSpendGeoPoint(spendDocRef: DocumentReference): DataResult<GeoPoint> {
        val spendGeoPoint = spendDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spends_document_field_geo)
        ) as GeoPoint? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(spendGeoPoint)
    }

    override suspend fun getSpendMembers(
        spendDocRef: DocumentReference
    ): DataResult<List<RemoteSpendMember>> {
        val spendMembersSnapshot = spendDocRef.collection(
            appContext.getString(R.string.collection_name_spend_member)
        ).get().await().documents
        if (spendMembersSnapshot.isEmpty()) {
            return DataResult.Success(emptyList())
        }
        return DataResult.Success(
            buildList {
                spendMembersSnapshot.forEach { spendMemberDocSnapshot ->
                    val spendMember = assembleSpendMember(spendMemberDocSnapshot.reference)
                    if (spendMember is DataResult.Error) {
                        return spendMember
                    }
                    this.add((spendMember as DataResult.Success).data)
                }
            }
        )
    }

    override suspend fun assembleSpendMember(
        spendMemberDocRef: DocumentReference
    ): DataResult<RemoteSpendMember> {
        val friendDocRed = spendMemberDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spend_member_document_field_member)
        ) as DocumentReference? ?: return DataErrorHandler.handle(FirebaseUndefinedException())

        val friend = sharedFunctions.assembleFriend(friendDocRed, source)
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
            RemoteSpendMember(
                (friend as DataResult.Success).data,
                (payment as DataResult.Success).data,
                (debtsToUsers as DataResult.Success).data,
                spendMemberDocRef
            )
        )
    }

    override suspend fun getSpendMemberPayment(
        spendMemberDocRef: DocumentReference
    ): DataResult<Double> {
        val payment = spendMemberDocRef.get(source).await().get(
            appContext.getString(R.string.collection_spend_member_document_field_payment)
        ) as Double? ?: return DataErrorHandler.handle(FirebaseUndefinedException())
        return DataResult.Success(payment)
    }

    override suspend fun getDebtsToUsers(
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

    override suspend fun assembleDebtToUser(
        debtToUserMap: HashMap<String, Double>,
        key: String
    ): DataResult<DebtToUser> {
        val friendDocRef = remoteDataSource.db.collection(
            appContext.getString(R.string.collection_name_users)
        ).document(key)

        val friend = sharedFunctions.assembleFriend(friendDocRef, source)
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

    override suspend fun updateSpendName(
        spendDocRef: DocumentReference,
        newName: String
    ): DataResult<String> {
        return try {
            spendDocRef.update(
                appContext.getString(R.string.collection_spends_document_field_name),
                newName
            ).await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_NAME_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String
    ): DataResult<String> {
        return try {
            spendDocRef.update(
                appContext.getString(R.string.collection_spends_document_field_category),
                newCategory
            ).await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_CATEGORY_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateSpendSplitMode(
        spendDocRef: DocumentReference,
        newSplitMode: String
    ): DataResult<String> {
        return try {
            spendDocRef.update(
                appContext.getString(R.string.collection_spends_document_field_split_mode),
                newSplitMode
            ).await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_SPLIT_MODE_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateSpendAmount(
        spendDocRef: DocumentReference,
        newAmount: Double
    ): DataResult<String> {
        return try {
            spendDocRef.update(
                appContext.getString(R.string.collection_spends_document_field_amount),
                newAmount
            ).await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_AMOUNT_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun updateSpendGeoPoint(
        spendDocRef: DocumentReference,
        newGeoPoint: GeoPoint
    ): DataResult<String> {
        return try {
            spendDocRef.update(
                appContext.getString(R.string.collection_spends_document_field_geo),
                newGeoPoint
            ).await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_GEO_UPDATED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun addSpendMembers(
        spendDocRef: DocumentReference,
        newMembers: List<RemoteSpendMember>
    ): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()
            newMembers.forEach { newMember ->
                val newSpendMemberDocRef = spendDocRef.collection(
                    appContext.getString(R.string.collection_name_spend_member)
                ).document()

                val debtToUser = hashMapOf<DocumentReference, Double>()
                newMember.debt.forEach { debt ->
                    debtToUser[debt.user.docRef] = debt.debt
                }

                batch.set(
                    newSpendMemberDocRef,
                    mapOf(
                        appContext.getString(
                            R.string.collection_spend_member_document_field_payment
                        ) to
                            newMember.payment,
                        appContext.getString(R.string.collection_spend_member_document_field_member)
                            to newMember.friend.docRef,
                        appContext.getString(R.string.collection_spend_member_document_field_debt)
                            to debtToUser
                    )
                )
            }
            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_MEMBERS_ADDED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun deleteSpendMembers(
        members: List<RemoteSpendMember>
    ): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()
            members.forEach { member ->
                batch.delete(member.docRef)
            }
            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_MEMBERS_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun deleteSpend(spend: RemoteSpend): DataResult<String> {
        return try {
            spend.docRef.delete().await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_DELETED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }
}
