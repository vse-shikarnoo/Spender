package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.interfaces.SpendRepositoryInterface
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.MemberActivity
import com.example.spender.data.models.spend.Spend
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SpendRepository : SpendRepositoryInterface {
    private val db by lazy { FirebaseFirestore.getInstance() }

    override suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String,
        amount: Double,
        members: List<MemberActivity>
    ): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpend(spendDocRef: DocumentReference): FirebaseCallResult<Spend> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendName(spendDocRef: DocumentReference): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendCategory(spendDocRef: DocumentReference): FirebaseCallResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendAmount(spendDocRef: DocumentReference): FirebaseCallResult<Double> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendMembers(
        spendDocRef: DocumentReference
    ): FirebaseCallResult<List<MemberActivity>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendName(
        spendDocRef: DocumentReference,
        newName: String
    ): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String
    ): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendAmount(
        spendDocRef: DocumentReference,
        newCategory: String
    ): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMember(
        spendDocRef: DocumentReference,
        newMember: Friend
    ): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMembers(
        spendDocRef: DocumentReference,
        newMembers: List<Friend>
    ): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendMembers(
        spendDocRef: DocumentReference,
        member: List<Friend>
    ): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendSpend(spendDocRef: DocumentReference): FirebaseCallResult<Boolean> {
        TODO("Not yet implemented")
    }
}
