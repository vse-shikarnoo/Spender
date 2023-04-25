package com.example.spender.domain.usecases.interfaces

import com.example.spender.data.DataResult
import com.example.spender.domain.model.spend.Spend

interface SpendUpdateUseCase {
    suspend operator fun invoke(oldSpend: Spend, newSpend: Spend): DataResult<String>
}