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

    fun getUser(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserDataResult.postValue(
                repository.get().getUser(userID)
            )
        }
    }

    private val _getUserNameDataResult = MutableLiveData<DataResult<UserName>>()
    val getUserNameDataResult: LiveData<DataResult<UserName>> =
        _getUserNameDataResult

    fun getUserName(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNameDataResult.postValue(
                repository.get().getUserName(userID)
            )
        }
    }

    private val _getUserAgeDataResult = MutableLiveData<DataResult<Long>>()
    val getUserAgeDataResult: LiveData<DataResult<Long>> =
        _getUserAgeDataResult

    fun getUserAge(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAgeDataResult.postValue(
                repository.get().getUserAge(userID)
            )
        }
    }

    private val _getUserNicknameDataResult = MutableLiveData<DataResult<String>>()
    val getUserNicknameDataResult: LiveData<DataResult<String>> =
        _getUserNicknameDataResult

    fun getUserNickname(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNicknameDataResult.postValue(
                repository.get().getUserNickname(userID)
            )
        }
    }

    private val _getUserIncomingFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserIncomingFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserIncomingFriendsDataResult

    fun getUserIncomingFriends(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserIncomingFriendsDataResult.postValue(
                repository.get().getUserFriends(userID)
            )
        }
    }

    private val _getUserOutgoingFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserOutgoingFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserOutgoingFriendsDataResult

    fun getUserOutgoingFriends(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserOutgoingFriendsDataResult.postValue(
                repository.get().getUserOutgoingFriends(userID)
            )
        }
    }

    private val _getUserFriendsDataResult =
        MutableLiveData<DataResult<List<Friend>>>()
    val getUserFriendsDataResult: LiveData<DataResult<List<Friend>>> =
        _getUserFriendsDataResult

    fun getUserFriends(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserFriendsDataResult.postValue(
                repository.get().getUserFriends(userID)
            )
        }
    }

    private val _getUserTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getUserTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getUserTripsDataResult

    fun getUserTrips(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserTripsDataResult.postValue(
                repository.get().getUserTrips(userID)
            )
        }
    }

    private val _getUserAdminTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getUserAdminTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getUserAdminTripsDataResult

    fun getUserAdminTrips(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAdminTripsDataResult.postValue(
                repository.get().getUserAdminTrips(userID)
            )
        }
    }

    private val _getUserPassengerTripsDataResult =
        MutableLiveData<DataResult<List<Trip>>>()
    val getUserPassengerTripsDataResult: LiveData<DataResult<List<Trip>>> =
        _getUserPassengerTripsDataResult

    fun getUserPassengerTrips(userID: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserPassengerTripsDataResult.postValue(
                repository.get().getUserPassengerTrips(userID)
            )
        }
    }

    private val _updateUserDataResult = MutableLiveData<DataResult<String>>()
    val updateUserDataResult: LiveData<DataResult<String>> =
        _updateUserDataResult

    fun updateUser(userID: String? = null, newUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserDataResult.postValue(
                repository.get().updateUser(userID, newUser)
            )
        }
    }

    private val _updateUserNameDataResult = MutableLiveData<DataResult<String>>()
    val updateUserNameDataResult: LiveData<DataResult<String>> =
        _updateUserNameDataResult

    fun updateUserName(userID: String? = null, newName: UserName) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNameDataResult.postValue(
                repository.get().updateUserName(userID, newName)
            )
        }
    }

    private val _updateUserAgeDataResult = MutableLiveData<DataResult<String>>()
    val updateUserAgeDataResult: LiveData<DataResult<String>> =
        _updateUserAgeDataResult

    fun updateUserAge(userID: String? = null, newAge: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserAgeDataResult.postValue(
                repository.get().updateUserAge(userID, newAge)
            )
        }
    }

    private val _updateUserNicknameDataResult =
        MutableLiveData<DataResult<String>>()
    val updateUserNicknameDataResult: LiveData<DataResult<String>> =
        _updateUserNicknameDataResult

    fun updateUserNickname(userID: String? = null, newNickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNicknameDataResult.postValue(
                repository.get().updateUserNickname(userID, newNickname)
            )
        }
    }

    private val _addUserOutgoingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val addUserOutgoingFriendDataResult: LiveData<DataResult<String>> =
        _addUserOutgoingFriendDataResult

    fun addUserOutgoingFriend(userID: String? = null, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserOutgoingFriendDataResult.postValue(
                repository.get().addUserOutgoingFriend(userID, friend)
            )
        }
    }

    private val _addUserIncomingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val addUserIncomingFriendDataResult: LiveData<DataResult<String>> =
        _addUserIncomingFriendDataResult

    fun addUserIncomingFriend(userID: String? = null, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserIncomingFriendDataResult.postValue(
                repository.get().addUserIncomingFriend(userID, friend)
            )
        }
    }

    private val _removeUserFriendDataResult = MutableLiveData<DataResult<String>>()
    val removeUserFriendDataResult: LiveData<DataResult<String>> =
        _removeUserFriendDataResult

    fun removeUserFriend(userID: String? = null, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserFriendDataResult.postValue(
                repository.get().removeUserFriend(userID, friend)
            )
        }
    }

    private val _removeUserOutgoingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val removeUserOutgoingFriendDataResult: LiveData<DataResult<String>> =
        _removeUserOutgoingFriendDataResult

    fun removeUserOutgoingFriend(userID: String? = null, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserOutgoingFriendDataResult.postValue(
                repository.get().removeUserOutgoingFriend(userID, friend)
            )
        }
    }

    private val _removeUserIncomingFriendDataResult =
        MutableLiveData<DataResult<String>>()
    val removeUserIncomingFriendDataResult: LiveData<DataResult<String>> =
        _removeUserIncomingFriendDataResult

    fun removeUserIncomingFriend(userID: String? = null, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserIncomingFriendDataResult.postValue(
                repository.get().removeUserIncomingFriend(userID, friend)
            )
        }
    }
}
