package com.example.spender.data.firebase.repositories

import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.interfaces.SpendRepositoryInterface
import com.example.spender.data.firebase.models.Friend
import com.example.spender.data.firebase.models.MemberActivity
import com.example.spender.data.firebase.models.Spend
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SpendRepository: SpendRepositoryInterface {
    private val db by lazy { FirebaseFirestore.getInstance() }

    override suspend fun createSpend(
        tripDocRef: DocumentReference,
        name: String,
        category: String,
        amount: Double,
        members: List<MemberActivity>
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpend(spendDocRef: DocumentReference): Result<Spend> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendName(spendDocRef: DocumentReference): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendCategory(spendDocRef: DocumentReference): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendAmount(spendDocRef: DocumentReference): Result<Double> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpendMembers(spendDocRef: DocumentReference): Result<List<MemberActivity>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendName(
        spendDocRef: DocumentReference,
        newName: String
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendCategory(
        spendDocRef: DocumentReference,
        newCategory: String
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpendAmount(
        spendDocRef: DocumentReference,
        newCategory: String
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMember(
        spendDocRef: DocumentReference,
        newMember: Friend
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addSpendMembers(
        spendDocRef: DocumentReference,
        newMembers: List<Friend>
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendMembers(
        spendDocRef: DocumentReference,
        member: List<Friend>
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpendSpend(spendDocRef: DocumentReference): Result<Boolean> {
        TODO("Not yet implemented")
    }

}
