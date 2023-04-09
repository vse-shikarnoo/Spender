package com.example.spender.data.firebase.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.repositoryInterfaces.TripRepositoryInterface
import com.example.spender.data.models.Trip
import com.example.spender.data.models.spend.Spend
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: dagger.Lazy<TripRepositoryInterface>
): ViewModel() {
    private val _createTripFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val createTripFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _createTripFirebaseCallResult

    suspend fun createTrip(name: String, creator: User, members: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _createTripFirebaseCallResult.postValue(
                repository.get().createTrip(name, creator, members)
            )
        }
    }

    private val _updateTripNameFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateTripNameFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateTripNameFirebaseCallResult

    suspend fun updateTripName(trip: Trip, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateTripNameFirebaseCallResult.postValue(
                repository.get().updateTripName(trip, newName)
            )
        }
    }

    private val _addTripMemberFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val addTripMemberFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _addTripMemberFirebaseCallResult

    suspend fun addTripMember(trip: Trip, newMember: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripMemberFirebaseCallResult.postValue(
                repository.get().addTripMember(trip, newMember)
            )
        }
    }

    private val _addTripMembersFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val addTripMembersFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _addTripMembersFirebaseCallResult

    suspend fun addTripMembers(trip: Trip, newMembers: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripMembersFirebaseCallResult.postValue(
                repository.get().addTripMembers(trip, newMembers)
            )
        }
    }

    private val _addTripSpendFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val addTripSpendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _addTripSpendFirebaseCallResult

    suspend fun addTripSpend(trip: Trip, spend: Spend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripSpendFirebaseCallResult.postValue(
                repository.get().addTripSpend(trip, spend)
            )
        }
    }

    private val _removeTripMemberFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val removeTripMemberFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _removeTripMemberFirebaseCallResult

    suspend fun removeTripMember(trip: Trip, member: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripMemberFirebaseCallResult.postValue(
                repository.get().removeTripMember(trip, member)
            )
        }
    }

    private val _removeTripMembersFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val removeTripMembersFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _removeTripMembersFirebaseCallResult

    suspend fun removeTripMembers(trip: Trip, members: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripMembersFirebaseCallResult.postValue(
                repository.get().removeTripMembers(trip, members)
            )
        }
    }

    private val _removeTripSpendFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val removeTripSpendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _removeTripSpendFirebaseCallResult

    suspend fun removeTripSpend(spend: Spend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripSpendFirebaseCallResult.postValue(
                repository.get().removeTripSpend(spend)
            )
        }
    }

    private val _deleteTripFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val deleteTripFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _deleteTripFirebaseCallResult

    suspend fun deleteTrip(trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteTripFirebaseCallResult.postValue(
                repository.get().deleteTrip(trip)
            )
        }
    }
}