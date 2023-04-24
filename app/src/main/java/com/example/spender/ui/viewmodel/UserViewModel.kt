package com.example.spender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.DataResult
import com.example.spender.domain.repository.UserRepository
import com.example.spender.domain.model.user.Friend
import com.example.spender.domain.model.user.User
import com.example.spender.domain.model.Trip
import com.example.spender.domain.model.user.UserName
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: dagger.Lazy<UserRepository>
) : ViewModel() {
    private val _getUserDataResult = MutableLiveData<DataResult<User>>()
    val getUserDataResult: LiveData<DataResult<User>> = _getUserDataResult

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserDataResult.postValue(
                repository.get().getUser()
            )
        }
    }

    private val _getUserNameDataResult = MutableLiveData<DataResult<UserName>>()
    val getUserNameDataResult: LiveData<DataResult<UserName>> =
        _getUserNameDataResult

    fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNameDataResult.postValue(
                repository.get().getUserName()
            )
        }
    }

    private val _getUserAgeDataResult = MutableLiveData<DataResult<Long>>()
    val getUserAgeDataResult: LiveData<DataResult<Long>> =
        _getUserAgeDataResult

    fun getUserAge() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAgeDataResult.postValue(
                repository.get().getUserAge()
            )
        }
    }

    private val _getUserNicknameDataResult = MutableLiveData<DataResult<String>>()
    val getUserNicknameDataResult: LiveData<DataResult<String>> =
        _getUserNicknameDataResult

    fun getUserNickname() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNicknameDataResult.postValue(
                repository.get().getUserNickname()
            )
        }
    }

    private val _getUserIncomingFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserIncomingFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserIncomingFriendsDataResult

    fun getUserIncomingFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserIncomingFriendsDataResult.postValue(
                repository.get().getUserFriends()
            )
        }
    }

    private val _getUserOutgoingFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserOutgoingFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserOutgoingFriendsDataResult

    fun getUserOutgoingFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserOutgoingFriendsDataResult.postValue(
                repository.get().getUserOutgoingFriends()
            )
        }
    }

    private val _getUserFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserFriendsDataResult

    fun getUserFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserFriendsDataResult.postValue(
                repository.get().getUserFriends()
            )
        }
    }

    private val _getUserTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getUserTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getUserTripsDataResult

    fun getUserTrips() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserTripsDataResult.postValue(
                repository.get().getUserTrips()
            )
        }
    }

    private val _getUserAdminTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getUserAdminTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getUserAdminTripsDataResult

    fun getUserAdminTrips() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAdminTripsDataResult.postValue(
                repository.get().getUserAdminTrips()
            )
        }
    }

    private val _getUserPassengerTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getUserPassengerTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getUserPassengerTripsDataResult

    fun getUserPassengerTrips() {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserPassengerTripsDataResult.postValue(
                repository.get().getUserPassengerTrips()
            )
        }
    }

    private val _updateUserDataResult = MutableLiveData<DataResult<String>>()
    val updateUserDataResult: LiveData<DataResult<String>> =
        _updateUserDataResult

    fun updateUser(newUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserDataResult.postValue(
                repository.get().updateUser(newUser)
            )
        }
    }

    private val _updateUserNameDataResult = MutableLiveData<DataResult<String>>()
    val updateUserNameDataResult: LiveData<DataResult<String>> =
        _updateUserNameDataResult

    fun updateUserName(newName: UserName) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNameDataResult.postValue(
                repository.get().updateUserName(newName)
            )
        }
    }

    private val _updateUserAgeDataResult = MutableLiveData<DataResult<String>>()
    val updateUserAgeDataResult: LiveData<DataResult<String>> =
        _updateUserAgeDataResult

    fun updateUserAge(newAge: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserAgeDataResult.postValue(
                repository.get().updateUserAge(newAge)
            )
        }
    }

    private val _updateUserNicknameDataResult =
        MutableLiveData<DataResult<String>>()
    val updateUserNicknameDataResult: LiveData<DataResult<String>> =
        _updateUserNicknameDataResult

    fun updateUserNickname(newNickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNicknameDataResult.postValue(
                repository.get().updateUserNickname(newNickname)
            )
        }
    }

    private val _addUserOutgoingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val addUserOutgoingFriendDataResult: LiveData<DataResult<String>> =
        _addUserOutgoingFriendDataResult

    fun addUserOutgoingFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserOutgoingFriendDataResult.postValue(
                repository.get().addUserOutgoingFriend(friend)
            )
        }
    }

    private val _addUserIncomingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val addUserIncomingFriendDataResult: LiveData<DataResult<String>> =
        _addUserIncomingFriendDataResult

    fun addUserIncomingFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserIncomingFriendDataResult.postValue(
                repository.get().addUserIncomingFriend(friend)
            )
        }
    }

    private val _removeUserFriendDataResult = MutableLiveData<DataResult<String>>()
    val removeUserFriendDataResult: LiveData<DataResult<String>> =
        _removeUserFriendDataResult

    fun removeUserFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserFriendDataResult.postValue(
                repository.get().removeUserFriend(friend)
            )
        }
    }

    private val _removeUserOutgoingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val removeUserOutgoingFriendDataResult: LiveData<DataResult<String>> =
        _removeUserOutgoingFriendDataResult

    fun removeUserOutgoingFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserOutgoingFriendDataResult.postValue(
                repository.get().removeUserOutgoingFriend(friend)
            )
        }
    }

    private val _removeUserIncomingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val removeUserIncomingFriendDataResult: LiveData<DataResult<String>> =
        _removeUserIncomingFriendDataResult

    fun removeUserIncomingFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserIncomingFriendDataResult.postValue(
                repository.get().removeUserIncomingFriend(friend)
            )
        }
    }
}
