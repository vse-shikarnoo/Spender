package com.example.spender.data.firebase.repositories

import android.app.Application
import com.example.spender.R
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.messages.FirebaseErrorHandler
import com.example.spender.data.firebase.messages.FirebaseSuccessMessages
import com.example.spender.data.firebase.repositoryInterfaces.SpendRepositoryInterface
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.spend.SpendMember
import com.example.spender.data.models.spend.SplitMode
import com.example.spender.data.models.user.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpendRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val appContext: Application
) : SpendRepositoryInterface {
    override suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String,
        splitMode: SplitMode,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendName(
        spend: Spend,
        newName: String
    ): FirebaseCallResult<String> {
        return try {
            spend.docRef.update(
                appContext.getString(R.string.subcollection_spends_document_field_name),
                newName
            ).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.SPEND_NAME_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateSpendCategory(
        spend: Spend,
        newCategory: String
    ): FirebaseCallResult<String> {
        return try {
            spend.docRef.update(
                appContext.getString(R.string.subcollection_spends_document_field_category),
                newCategory
            ).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.SPEND_CATEGORY_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateSpendSplitMode(
        spend: Spend,
        newSplitMode: SplitMode
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendAmount(
        spend: Spend,
        newAmount: Double
    ): FirebaseCallResult<String> {
        return try {
            spend.docRef.update(
                appContext.getString(R.string.subcollection_spends_document_field_amount),
                newAmount
            ).await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.SPEND_AMOUNT_UPDATED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun updateSpendGeoPoint(
        spend: Spend,
        newGeoPoint: GeoPoint
    ): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMember(
        spend: Spend,
        newMember: Friend
    ): FirebaseCallResult<String> {
        return try {
            spend.docRef.update(
                appContext.getString(R.string.subcollection_spends_document_field_members),
                FieldValue.arrayUnion(newMember.docRef)
            ).await()

            FirebaseCallResult.Success(FirebaseSuccessMessages.SPEND_MEMBER_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun addSpendMembers(
        spend: Spend,
        newMembers: List<Friend>
    ): FirebaseCallResult<String> {
        return try {
            newMembers.forEach { member ->
                spend.docRef.update(
                    appContext.getString(R.string.subcollection_spends_document_field_members),
                    FieldValue.arrayUnion(member.docRef)
                ).await()
            }
            FirebaseCallResult.Success(FirebaseSuccessMessages.SPEND_MEMBERS_ADDED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun deleteSpendMembers(
        spend: Spend,
        member: List<Friend>
    ): FirebaseCallResult<String> {
        return try {
            member.forEach { member ->
                spend.docRef.update(
                    appContext.getString(R.string.subcollection_spends_document_field_members),
                    FieldValue.arrayRemove(member.docRef)
                ).await()

                TODO("reorganize spends")
            }
            FirebaseCallResult.Success(FirebaseSuccessMessages.TRIP_MEMBERS_REMOVED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }

    override suspend fun deleteSpendSpend(spend: Spend): FirebaseCallResult<String> {
        return try {
            val batch = db.batch()

            spend.members.forEach { member ->
//                batch.update(
//                    // Is docRef needed in SpendMember for deleting?
//                    member.docRef,
//                    appContext.getString(R.string.collection_users_document_field_trips),
//                    FieldValue.arrayRemove(spend.docRef)
//                )
            }

            batch.delete(spend.docRef)

            batch.commit().await()
            FirebaseCallResult.Success(FirebaseSuccessMessages.SPEND_DELETED)
        } catch (e: Exception) {
            FirebaseErrorHandler.handle(e)
        }
    }
}
