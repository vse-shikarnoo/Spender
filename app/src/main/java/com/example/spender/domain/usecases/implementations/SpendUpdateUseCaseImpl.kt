package com.example.spender.domain.usecases.implementations

import com.example.spender.data.DataResult
import com.example.spender.data.messages.FirebaseSuccessMessages
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.repository.SpendRepository
import com.example.spender.domain.usecases.interfaces.SpendUpdateUseCase
import javax.inject.Inject

class SpendUpdateUseCaseImpl @Inject constructor(
    private val spendRepository: dagger.Lazy<SpendRepository>
) : SpendUpdateUseCase {
    override suspend operator fun invoke(oldSpend: Spend, newSpend: Spend): DataResult<String> {
        if (oldSpend.name != newSpend.name) {
            val result = spendRepository.get().updateSpendName(newSpend.docRef, newSpend.name)
            if (result is DataResult.Error) return result
        }
        if (oldSpend.category != newSpend.category) {
            val result =
                spendRepository.get().updateSpendCategory(newSpend.docRef, newSpend.category)
            if (result is DataResult.Error) return result
        }
        if (oldSpend.splitMode != newSpend.splitMode) {
            val result =
                spendRepository.get().updateSpendSplitMode(newSpend.docRef, newSpend.splitMode)
            if (result is DataResult.Error) return result
        }
        if (oldSpend.amount != newSpend.amount) {
            val result = spendRepository.get().updateSpendAmount(newSpend.docRef, newSpend.amount)
            if (result is DataResult.Error) return result
        }
        if (oldSpend.geoPoint != newSpend.geoPoint) {
            val result =
                spendRepository.get().updateSpendGeoPoint(newSpend.docRef, newSpend.geoPoint)
            if (result is DataResult.Error) return result
        }

        val delLst = buildList {
            oldSpend.members.forEach { member ->
                if (!newSpend.members.contains(member)) this.add(member)
            }
        }
        if (delLst.isNotEmpty()) {
            val result = spendRepository.get().deleteSpendMembers(newSpend.docRef, delLst)
            if (result is DataResult.Error) return result
        }

        val addLst = buildList {
            newSpend.members.forEach { member ->
                if (!oldSpend.members.contains(member)) this.add(member)
            }
        }
        if (addLst.isNotEmpty()) {
            val result = spendRepository.get().addSpendMembers(newSpend.docRef, addLst)
            if (result is DataResult.Error) return result
        }

        return DataResult.Success(FirebaseSuccessMessages.SPEND_UPDATED)
    }
}
