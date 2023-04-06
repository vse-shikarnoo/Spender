package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.MemberActivity
import com.example.spender.data.models.spend.Spend
import com.google.firebase.firestore.DocumentReference

interface SpendRepositoryInterface {
    suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String = "No category",
        amount: Double,
        members: List<MemberActivity>,
    ): FirebaseCallResult<Boolean>

    suspend fun getSpend(spendDocRef: DocumentReference): FirebaseCallResult<Spend>
    suspend fun getSpendName(spendDocRef: DocumentReference): FirebaseCallResult<String>
    suspend fun getSpendCategory(spendDocRef: DocumentReference): FirebaseCallResult<String>
    suspend fun getSpendAmount(spendDocRef: DocumentReference): FirebaseCallResult<Double>
    suspend fun getSpendMembers(spendDocRef: DocumentReference): FirebaseCallResult<List<MemberActivity>>
    suspend fun updateSpendName(spendDocRef: DocumentReference, newName: String): FirebaseCallResult<Boolean>
    suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String,
    ): FirebaseCallResult<Boolean>

    suspend fun updateSpendAmount(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        newCategory: String,
    ): FirebaseCallResult<Boolean>

    suspend fun addSpendMember(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        newMember: Friend,
    ): FirebaseCallResult<Boolean>

    suspend fun addSpendMembers(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        newMembers: List<Friend>,
    ): FirebaseCallResult<Boolean>

    suspend fun deleteSpendMembers(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        member: List<Friend>,
    ): FirebaseCallResult<Boolean>

    suspend fun deleteSpendSpend(
        spendDocRef: DocumentReference,
    ): FirebaseCallResult<Boolean>
}
