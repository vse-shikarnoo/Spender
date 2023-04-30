package com.example.spender.domain.remotedao

import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.spend.RemoteSpend
import com.example.spender.domain.remotemodel.spend.Spend
import com.example.spender.domain.remotemodel.spendmember.DebtToUser
import com.example.spender.domain.remotemodel.spendmember.RemoteSpendMember
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Source

interface RemoteSpendDao {
    var source: Source

    suspend fun createSpend(trip: Trip, spend: Spend): DataResult<String>
    suspend fun getSpends(trip: Trip): DataResult<List<RemoteSpend>>
    suspend fun assembleSpend(spendDocRef: DocumentReference): DataResult<RemoteSpend>
    suspend fun getSpendName(spendDocRef: DocumentReference): DataResult<String>
    suspend fun getSpendCategory(spendDocRef: DocumentReference): DataResult<String>
    suspend fun getSpendSplitMode(spendDocRef: DocumentReference): DataResult<String>
    suspend fun getSpendAmount(spendDocRef: DocumentReference): DataResult<Double>
    suspend fun getSpendGeoPoint(spendDocRef: DocumentReference): DataResult<GeoPoint>
    suspend fun getSpendMembers(
        spendDocRef: DocumentReference
    ): DataResult<List<RemoteSpendMember>>
    suspend fun assembleSpendMember(
        spendMemberDocRef: DocumentReference
    ): DataResult<RemoteSpendMember>
    suspend fun getSpendMemberPayment(
        spendMemberDocRef: DocumentReference
    ): DataResult<Double>
    suspend fun getDebtsToUsers(
        spendMemberDocRef: DocumentReference
    ): DataResult<List<DebtToUser>>
    suspend fun assembleDebtToUser(
        debtToUserMap: HashMap<String, Double>,
        key: String
    ): DataResult<DebtToUser>
    suspend fun updateSpendName(
        spendDocRef: DocumentReference, newName: String
    ): DataResult<String>
    suspend fun updateSpendCategory(
        spendDocRef: DocumentReference, newCategory: String
    ): DataResult<String>
    suspend fun updateSpendSplitMode(
        spendDocRef: DocumentReference,
        newSplitMode: String
    ): DataResult<String>
    suspend fun updateSpendAmount(
        spendDocRef: DocumentReference,
        newAmount: Double
    ): DataResult<String>
    suspend fun updateSpendGeoPoint(
        spendDocRef: DocumentReference,
        newGeoPoint: GeoPoint
    ): DataResult<String>
    suspend fun addSpendMembers(
        spendDocRef: DocumentReference,
        newMembers: List<RemoteSpendMember>
    ): DataResult<String>
    suspend fun deleteSpendMembers(
        members: List<RemoteSpendMember>
    ): DataResult<String>
    suspend fun deleteSpend(spend: RemoteSpend): DataResult<String>
}
