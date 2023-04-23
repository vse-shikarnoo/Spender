package com.example.spender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.DataResult
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.spend.SpendMember
import com.example.spender.domain.repository.SpendRepository
import com.example.spender.domain.usecases.SpendUpdateUseCase
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpendViewModel @Inject constructor(
    private val repository: dagger.Lazy<SpendRepository>,
    private val spendUpdateUseCase: dagger.Lazy<SpendUpdateUseCase>
) : ViewModel() {
    private val _createSpendDataResult = MutableLiveData<DataResult<String>>()
    val createSpendDataResult: LiveData<DataResult<String>> =
        _createSpendDataResult

    fun createSpend(
        trip: Trip,
        name: String,
        category: String,
        splitMode: Int,
        amount: Double,
        geoPoint: GeoPoint,
        members: List<SpendMember>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _createSpendDataResult.postValue(
                repository.get().createSpend(
                    trip,
                    name,
                    category,
                    splitMode,
                    amount,
                    geoPoint,
                    members
                )
            )
        }
    }

    private val _updateSpendGeoPointDataResult = MutableLiveData<DataResult<String>>()
    val updateSpendGeoPointDataResult: LiveData<DataResult<String>> =
        _updateSpendGeoPointDataResult

    fun updateSpend(
        oldSpend: Spend,
        newSpend: Spend
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateSpendGeoPointDataResult.postValue(
                spendUpdateUseCase.get().invoke(oldSpend, newSpend)
            )
        }
    }
}