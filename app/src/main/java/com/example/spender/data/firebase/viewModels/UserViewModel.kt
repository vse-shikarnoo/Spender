package com.example.spender.data.firebase.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.firebase.FirebaseCallResult
import com.example.spender.data.firebase.FirebaseRepositoriesHolder
import com.example.spender.data.models.user.Friend
import com.example.spender.data.models.user.User
import com.example.spender.data.models.Trip
import com.example.spender.data.models.user.UserName
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor() : ViewModel() {
    private val _getUserFirebaseCallResult = MutableLiveData<FirebaseCallResult<User>>()
    val getUserFirebaseCallResult: LiveData<FirebaseCallResult<User>> = _getUserFirebaseCallResult

    fun getUser(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUser(userID)
            )
        }
    }

    private val _getUserNameFirebaseCallResult = MutableLiveData<FirebaseCallResult<UserName>>()
    val getUserNameFirebaseCallResult: LiveData<FirebaseCallResult<UserName>> =
        _getUserNameFirebaseCallResult

    fun getUserName(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNameFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserName(userID)
            )
        }
    }

    private val _getUserAgeFirebaseCallResult = MutableLiveData<FirebaseCallResult<Long>>()
    val getUserAgeFirebaseCallResult: LiveData<FirebaseCallResult<Long>> =
        _getUserAgeFirebaseCallResult

    fun getUserAge(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAgeFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserAge(userID)
            )
        }
    }

    private val _getUserNicknameFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val getUserNicknameFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _getUserNicknameFirebaseCallResult

    fun getUserNickname(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNicknameFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserNickname(userID)
            )
        }
    }

    private val _getUserIncomingFriendsFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<List<Friend>>>()
    val getUserIncomingFriendsFirebaseCallResult: LiveData<FirebaseCallResult<List<Friend>>> =
        _getUserIncomingFriendsFirebaseCallResult

    fun getUserIncomingFriends(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserIncomingFriendsFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserFriends(userID)
            )
        }
    }

    private val _getUserOutgoingFriendsFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<List<Friend>>>()
    val getUserOutgoingFriendsFirebaseCallResult: LiveData<FirebaseCallResult<List<Friend>>> =
        _getUserOutgoingFriendsFirebaseCallResult

    fun getUserOutgoingFriends(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserOutgoingFriendsFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserOutgoingFriends(userID)
            )
        }
    }

    private val _getUserFriendsFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<List<Friend>>>()
    val getUserFriendsFirebaseCallResult: LiveData<FirebaseCallResult<List<Friend>>> =
        _getUserFriendsFirebaseCallResult

    fun getUserFriends(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserFriendsFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserFriends(userID)
            )
        }
    }

    private val _getUserAdminTripsFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<List<Trip>>>()
    val getUserAdminTripsFirebaseCallResult: LiveData<FirebaseCallResult<List<Trip>>> =
        _getUserAdminTripsFirebaseCallResult

    fun getUserAdminTrips(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAdminTripsFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserAdminTrips(userID)
            )
        }
    }

    private val _getUserPassengerTripsFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<List<Trip>>>()
    val getUserPassengerTripsFirebaseCallResult: LiveData<FirebaseCallResult<List<Trip>>> =
        _getUserPassengerTripsFirebaseCallResult

    fun getUserPassengerTrips(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserPassengerTripsFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.getUserPassengerTrips(userID)
            )
        }
    }

    private val _updateUserFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateUserFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateUserFirebaseCallResult

    fun updateUser(userID: String, newUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.updateUser(userID, newUser)
            )
        }
    }

    private val _updateUserNameFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateUserNameFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateUserNameFirebaseCallResult

    fun updateUserName(userID: String, newName: UserName) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNameFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.updateUserName(userID, newName)
            )
        }
    }

    private val _updateUserAgeFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val updateUserAgeFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateUserAgeFirebaseCallResult

    fun updateUserAge(userID: String, newAge: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserAgeFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.updateUserAge(userID, newAge)
            )
        }
    }

    private val _updateUserNicknameFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<String>>()
    val updateUserNicknameFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _updateUserNicknameFirebaseCallResult

    fun updateUserNickname(userID: String, newNickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNicknameFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.updateUserNickname(userID, newNickname)
            )
        }
    }

    private val _addUserOutgoingFriendFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<String>>()
    val addUserOutgoingFriendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _addUserOutgoingFriendFirebaseCallResult

    fun addUserOutgoingFriend(userID: String, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserOutgoingFriendFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.addUserOutgoingFriend(userID, friend)
            )
        }
    }

    private val _addUserIncomingFriendFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<String>>()
    val addUserIncomingFriendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _addUserIncomingFriendFirebaseCallResult

    fun addUserIncomingFriend(userID: String, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserIncomingFriendFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.addUserIncomingFriend(userID, friend)
            )
        }
    }

    private val _removeUserFriendFirebaseCallResult = MutableLiveData<FirebaseCallResult<String>>()
    val removeUserFriendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _removeUserFriendFirebaseCallResult

    fun removeUserFriend(userID: String, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserFriendFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.removeUserFriend(userID, friend)
            )
        }
    }

    private val _removeUserOutgoingFriendFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<String>>()
    val removeUserOutgoingFriendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _removeUserOutgoingFriendFirebaseCallResult

    fun removeUserOutgoingFriend(userID: String, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserOutgoingFriendFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.removeUserOutgoingFriend(userID, friend)
            )
        }
    }

    private val _removeUserIncomingFriendFirebaseCallResult =
        MutableLiveData<FirebaseCallResult<String>>()
    val removeUserIncomingFriendFirebaseCallResult: LiveData<FirebaseCallResult<String>> =
        _removeUserIncomingFriendFirebaseCallResult

    fun removeUserIncomingFriend(userID: String, friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserIncomingFriendFirebaseCallResult.postValue(
                FirebaseRepositoriesHolder.userRepository.removeUserIncomingFriend(userID, friend)
            )
        }
    }
}
