package com.example.spender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.DataResult
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: dagger.Lazy<TripRepository>
): ViewModel() {
    private val _createTripDataResult = MutableLiveData<DataResult<String>>()
    val createTripDataResult: LiveData<DataResult<String>> =
        _createTripDataResult

    suspend fun createTrip(name: String, creator: User, members: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _createTripDataResult.postValue(
                repository.get().createTrip(name, creator, members)
            )
        }
    }

    private val _updateTripNameDataResult = MutableLiveData<DataResult<String>>()
    val updateTripNameDataResult: LiveData<DataResult<String>> =
        _updateTripNameDataResult

    suspend fun updateTripName(trip: Trip, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateTripNameDataResult.postValue(
                repository.get().updateTripName(trip, newName)
            )
        }
    }

    private val _addTripMemberDataResult = MutableLiveData<DataResult<String>>()
    val addTripMemberDataResult: LiveData<DataResult<String>> =
        _addTripMemberDataResult

    suspend fun addTripMember(trip: Trip, newMember: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripMemberDataResult.postValue(
                repository.get().addTripMember(trip, newMember)
            )
        }
    }

    private val _addTripMembersDataResult = MutableLiveData<DataResult<String>>()
    val addTripMembersDataResult: LiveData<DataResult<String>> =
        _addTripMembersDataResult

    suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripMembersDataResult.postValue(
                repository.get().addTripMembers(trip, newMembers)
            )
        }
    }

    private val _addTripSpendDataResult = MutableLiveData<DataResult<String>>()
    val addTripSpendDataResult: LiveData<DataResult<String>> =
        _addTripSpendDataResult

    suspend fun addTripSpend(trip: Trip, spend: Spend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripSpendDataResult.postValue(
                repository.get().addTripSpend(trip, spend)
            )
        }
    }

    private val _removeTripMemberDataResult = MutableLiveData<DataResult<String>>()
    val removeTripMemberDataResult: LiveData<DataResult<String>> =
        _removeTripMemberDataResult

    suspend fun removeTripMember(trip: Trip, member: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripMemberDataResult.postValue(
                repository.get().removeTripMember(trip, member)
            )
        }
    }

    private val _removeTripMembersDataResult = MutableLiveData<DataResult<String>>()
    val removeTripMembersDataResult: LiveData<DataResult<String>> =
        _removeTripMembersDataResult

    suspend fun removeTripMembers(trip: Trip, members: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripMembersDataResult.postValue(
                repository.get().removeTripMembers(trip, members)
            )
        }
    }

    private val _removeTripSpendDataResult = MutableLiveData<DataResult<String>>()
    val removeTripSpendDataResult: LiveData<DataResult<String>> =
        _removeTripSpendDataResult

    suspend fun removeTripSpend(spend: Spend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripSpendDataResult.postValue(
                repository.get().removeTripSpend(spend)
            )
        }
    }

    private val _deleteTripDataResult = MutableLiveData<DataResult<String>>()
    val deleteTripDataResult: LiveData<DataResult<String>> =
        _deleteTripDataResult

    suspend fun deleteTrip(trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteTripDataResult.postValue(
                repository.get().deleteTrip(trip)
            )
        }
    }
}