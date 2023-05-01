package com.example.spender.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.DataResult
import com.example.spender.domain.remotemodel.Trip
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.repository.TripRepository
import com.google.firebase.firestore.Source
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
     * Get admin trips
     */

    private val _getAdminTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getAdminTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getAdminTripsDataResult

    fun getAdminTrips() {
        viewModelScope.launch(Dispatchers.IO) {
            _getAdminTripsDataResult.postValue(
                repository.get().getAdminTrips(Source.CACHE)
            )
        }.invokeOnCompletion {
            viewModelScope.launch(Dispatchers.IO) {
                _getAdminTripsDataResult.postValue(
                    repository.get().getAdminTrips(Source.SERVER)
                )
            }
        }
    }

    /*
     * Get passenger trips
     */

    private val _getPassengerTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getPassengerTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getPassengerTripsDataResult

    fun getPassengerTrips() {
        viewModelScope.launch(Dispatchers.IO) {
            _getPassengerTripsDataResult.postValue(
                repository.get().getPassengerTrips(Source.CACHE)
            )
        }.invokeOnCompletion {
            viewModelScope.launch(Dispatchers.IO) {
                _getPassengerTripsDataResult.postValue(
                    repository.get().getPassengerTrips(Source.SERVER)
                )
            }
        }
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
