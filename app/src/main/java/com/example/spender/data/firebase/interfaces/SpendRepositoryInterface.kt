package com.example.spender.data.firebase.interfaces

import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.models.Friend
import com.example.spender.data.firebase.models.MemberActivity
import com.example.spender.data.firebase.models.Spend
import com.google.firebase.firestore.DocumentReference

interface SpendRepositoryInterface {
    suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String = "No category",
        amount: Double,
        members: List<MemberActivity>,
    ): Result<Boolean>

    suspend fun getSpend(spendDocRef: DocumentReference): Result<Spend>
    suspend fun getSpendName(spendDocRef: DocumentReference): Result<String>
    suspend fun getSpendCategory(spendDocRef: DocumentReference): Result<String>
    suspend fun getSpendAmount(spendDocRef: DocumentReference): Result<Double>
    suspend fun getSpendMembers(spendDocRef: DocumentReference): Result<List<MemberActivity>>
    suspend fun updateSpendName(spendDocRef: DocumentReference, newName: String): Result<Boolean>
    suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String,
    ): Result<Boolean>

    suspend fun updateSpendAmount(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        newCategory: String,
    ): Result<Boolean>

    suspend fun addSpendMember(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        newMember: Friend,
    ): Result<Boolean>

    suspend fun addSpendMembers(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        newMembers: List<Friend>,
    ): Result<Boolean>

    suspend fun deleteSpendMembers(
        // TODO("update all payments and debts - field members")
        spendDocRef: DocumentReference,
        member: List<Friend>,
    ): Result<Boolean>

    suspend fun deleteSpendSpend(
        spendDocRef: DocumentReference,
    ): Result<Boolean>
}
