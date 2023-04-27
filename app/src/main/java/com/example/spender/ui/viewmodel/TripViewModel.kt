package com.example.spender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.DataResult
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.spend.Spend
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: dagger.Lazy<TripRepository>
) : ViewModel() {

    /*
     * Create trip
     */

    private val _createTripDataResult = MutableLiveData<DataResult<String>>()
    val createTripDataResult: LiveData<DataResult<String>> =
        _createTripDataResult
    private val _createTripMsgShow = MutableLiveData<Boolean>()
    val createTripMsgShow: LiveData<Boolean> = _createTripMsgShow

    fun createTrip(name: String, members: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _createTripDataResult.postValue(
                repository.get().createTrip(name, members)
            )
        }.invokeOnCompletion {
            _createTripMsgShow.postValue(true)
        }
    }

    fun doNotShowCreateTripMsg() {
        _createTripMsgShow.postValue(false)
    }

    /*
     * Update trip
     */

    private val _updateTripNameDataResult = MutableLiveData<DataResult<String>>()
    val updateTripNameDataResult: LiveData<DataResult<String>> =
        _updateTripNameDataResult
    private val _updateTripMsgShow = MutableLiveData<Boolean>()
    val updateTripMsgShow: LiveData<Boolean> = _updateTripMsgShow

    fun updateTripName(trip: Trip, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateTripNameDataResult.postValue(
                repository.get().updateTripName(trip, newName)
            )
        }.invokeOnCompletion {
            _updateTripMsgShow.postValue(true)
        }
    }

    fun doNotShowUpdateTripMsg() {
        _updateTripMsgShow.postValue(false)
    }

    /*
     * Add trip member
     */

    private val _addTripMemberDataResult = MutableLiveData<DataResult<String>>()
    val addTripMemberDataResult: LiveData<DataResult<String>> =
        _addTripMemberDataResult
    private val _addTripMemberMsgShow = MutableLiveData<Boolean>()
    val addTripMemberMsgShow: LiveData<Boolean> = _addTripMemberMsgShow

    fun addTripMember(trip: Trip, newMember: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripMemberDataResult.postValue(
                repository.get().addTripMember(trip, newMember)
            )
        }.invokeOnCompletion {
            _addTripMemberMsgShow.postValue(true)
        }
    }

    fun doNotShowAddTripMemberMsg() {
        _addTripMemberMsgShow.postValue(false)
    }

    /*
     * Add trip members
     */

    private val _addTripMembersDataResult = MutableLiveData<DataResult<String>>()
    val addTripMembersDataResult: LiveData<DataResult<String>> =
        _addTripMembersDataResult
    private val _addTripMembersMsgShow = MutableLiveData<Boolean>()
    val addTripMembersMsgShow: LiveData<Boolean> = _addTripMembersMsgShow

    fun addTripMembers(trip: Trip, newMembers: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripMembersDataResult.postValue(
                repository.get().addTripMembers(trip, newMembers)
            )
        }.invokeOnCompletion {
            _addTripMembersMsgShow.postValue(true)
        }
    }

    fun doNotShowAddTripMembersMsg() {
        _addTripMembersMsgShow.postValue(false)
    }

    /*
     * Add trip spend
     */

    private val _addTripSpendDataResult = MutableLiveData<DataResult<String>>()
    val addTripSpendDataResult: LiveData<DataResult<String>> =
        _addTripSpendDataResult
    private val _addTripSpendMsgShow = MutableLiveData<Boolean>()
    val addTripSpendMsgShow: LiveData<Boolean> = _addTripSpendMsgShow

    fun addTripSpend(trip: Trip, spend: Spend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addTripSpendDataResult.postValue(
                repository.get().addTripSpend(trip, spend)
            )
        }.invokeOnCompletion {
            _addTripSpendMsgShow.postValue(true)
        }
    }

    fun doNotShowAddTripSpendMsg() {
        _addTripSpendMsgShow.postValue(false)
    }

    /*
     * Remove trip member
     */

    private val _removeTripMemberDataResult = MutableLiveData<DataResult<String>>()
    val removeTripMemberDataResult: LiveData<DataResult<String>> =
        _removeTripMemberDataResult
    private val _removeTripMemberMsgShow = MutableLiveData<Boolean>()
    val removeTripMemberMsgShow: LiveData<Boolean> = _removeTripMemberMsgShow

    fun removeTripMember(trip: Trip, member: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripMemberDataResult.postValue(
                repository.get().removeTripMember(trip, member)
            )
        }.invokeOnCompletion {
            _removeTripMemberMsgShow.postValue(true)
        }
    }

    fun doNotShowRemoveTripMemberMsg() {
        _removeTripMemberMsgShow.postValue(false)
    }

    /*
     * Remove trip members
     */

    private val _removeTripMembersDataResult = MutableLiveData<DataResult<String>>()
    val removeTripMembersDataResult: LiveData<DataResult<String>> =
        _removeTripMembersDataResult
    private val _removeTripMembersMsgShow = MutableLiveData<Boolean>()
    val removeTripMembersMsgShow: LiveData<Boolean> = _removeTripMembersMsgShow

    fun removeTripMembers(trip: Trip, members: List<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripMembersDataResult.postValue(
                repository.get().removeTripMembers(trip, members)
            )
        }.invokeOnCompletion {
            _removeTripMembersMsgShow.postValue(true)
        }
    }

    fun doNotShowRemoveTripMembersMsg() {
        _removeTripMembersMsgShow.postValue(false)
    }

    /*
     * Remove trip spend
     */

    private val _removeTripSpendDataResult = MutableLiveData<DataResult<String>>()
    val removeTripSpendDataResult: LiveData<DataResult<String>> =
        _removeTripSpendDataResult
    private val _removeTripSpendMsgShow = MutableLiveData<Boolean>()
    val removeTripSpendMsgShow: LiveData<Boolean> = _removeTripSpendMsgShow

    fun removeTripSpend(spend: Spend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeTripSpendDataResult.postValue(
                repository.get().removeTripSpend(spend)
            )
        }.invokeOnCompletion {
            _removeTripSpendMsgShow.postValue(true)
        }
    }

    fun doNotShowRemoveTripSpendMsg() {
        _removeTripSpendMsgShow.postValue(false)
    }

    /*
     * Delete trip
     */

    private val _deleteTripDataResult = MutableLiveData<DataResult<String>>()
    val deleteTripDataResult: LiveData<DataResult<String>> =
        _deleteTripDataResult
    private val _deleteTripMsgShow = MutableLiveData<Boolean>()
    val deleteTripMsgShow: LiveData<Boolean> = _deleteTripMsgShow

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteTripDataResult.postValue(
                repository.get().deleteTrip(trip)
            )
        }.invokeOnCompletion {
            _deleteTripMsgShow.postValue(true)
        }
    }
}
