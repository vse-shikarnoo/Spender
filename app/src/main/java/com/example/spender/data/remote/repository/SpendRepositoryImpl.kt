package com.example.spender.data.remote.repository

import android.app.Application
import com.example.spender.R
import com.example.spender.data.DataErrorHandler
import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.data.remote.RemoteDataSourceImpl
import com.example.spender.domain.repository.SpendRepository
import com.example.spender.domain.domainmodel.Trip
import com.example.spender.domain.domainmodel.spend.SpendMember
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpendRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val appContext: Application
) : SpendRepository {
    override suspend fun createSpend(
        trip: Trip,
        name: String,
        category: String,
        splitMode: Int,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()
            val newSpendDocRef =
                trip.docRef.collection(appContext.getString(R.string.collection_name_spends))
                    .document()
            batch.set(
                newSpendDocRef,
                mapOf(
                    appContext.getString(R.string.collection_spends_document_field_name) to name,
                    appContext.getString(R.string.collection_spends_document_field_category) to category,
                    appContext.getString(R.string.collection_spends_document_field_split_mode) to splitMode,
                    appContext.getString(R.string.collection_spends_document_field_amount) to amount,
                    appContext.getString(R.string.collection_spends_document_field_geo) to geoPoint,
                )
            )
            when (val addSpendMembersResult = addSpendMembers(newSpendDocRef, members)) {
                is DataResult.Success -> {
                    batch.commit().await()
                    DataResult.Success(FirebaseSuccessMessages.SPEND_CREATED)
                }

                is DataResult.Error -> {
                    addSpendMembersResult
                }
            }
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
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
        newSplitMode: Int
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
        newMembers: List<SpendMember>
    ): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()
            newMembers.forEach { newMember ->
                val newSpendMemberDocRef =
                    spendDocRef.collection(appContext.getString(R.string.collection_name_spend_member))
                        .document(newMember.friend.docRef.toString())

                val arrayList = arrayListOf<HashMap<String, Double>>()
                newMember.debt.forEach { debt ->
                    val userMap = hashMapOf<String, Double>()
                    userMap[debt.user.docRef.toString()] = debt.debt
                    arrayList.add(userMap)
                }

                batch.set(
                    newSpendMemberDocRef,
                    mapOf(
                        appContext.getString(R.string.collection_spend_member_document_field_payment) to
                                newMember.payment,
                        appContext.getString(R.string.collection_spend_member_document_field_member) to
                                newMember.friend.docRef,
                        appContext.getString(R.string.collection_spend_member_document_field_debt) to
                                arrayList
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
        spendDocRef: DocumentReference,
        members: List<SpendMember>
    ): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()
            val spendMembersCollectionRef =
                spendDocRef.collection(appContext.getString(R.string.collection_name_spend_member))
            members.forEach { member ->
                batch.delete(
                    spendMembersCollectionRef.document(member.friend.docRef.toString())
                )
            }
            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_MEMBERS_REMOVED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }

    override suspend fun deleteSpend(trip: Trip): DataResult<String> {
        return try {
            val batch = remoteDataSource.db.batch()
            val spendCollectionRef =
                trip.docRef.collection(appContext.getString(R.string.collection_name_spends)).get()
                    .await()

            spendCollectionRef.documents.forEach { document ->
                batch.delete(document.reference)
            }

            batch.commit().await()
            DataResult.Success(FirebaseSuccessMessages.SPEND_DELETED)
        } catch (e: Exception) {
            DataErrorHandler.handle(e)
        }
    }
}
