package com.example.spender.data.firebase.viewModels

import com.example.spender.data.firebase.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.firebase.models.Friend
import com.example.spender.data.firebase.models.User
import com.example.spender.data.firebase.repositories.UserRepository
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(): ViewModel() {
    private val repository = UserRepository()

    private val _createUserResult = MutableLiveData<Result<Boolean>>()
    val createUserResult: LiveData<Result<Boolean>> = _createUserResult

    private val _getUserResult = MutableLiveData<Result<User>>()
    val getUserResult: LiveData<Result<User>> = _getUserResult

    private val _getUserName = MutableLiveData<Result<Triple<String, String, String>>>()
    val getUserName: LiveData<Result<Triple<String, String, String>>> = _getUserName

    private val _getUserAge = MutableLiveData<Result<Int>>()
    val getUserAge: LiveData<Result<Int>> = _getUserAge

    private val _getUserNickname = MutableLiveData<Result<String>>()
    val getUserNickname: LiveData<Result<String>> = _getUserNickname

    private val _getUserFriends = MutableLiveData<Result<List<Friend>>>()
    val getUserFriends: LiveData<Result<List<Friend>>> = _getUserFriends

    private val _updateUserNameResult = MutableLiveData<Result<Boolean>>()
    val updateUserNameResult: LiveData<Result<Boolean>> = _updateUserNameResult

    private val _updateUserAgeResult = MutableLiveData<Result<Boolean>>()
    val updateUserAgeResult: LiveData<Result<Boolean>> = _updateUserAgeResult

    private val _updateUserNicknameResult = MutableLiveData<Result<Boolean>>()
    val updateUserNicknameResult: LiveData<Result<Boolean>> = _updateUserNicknameResult

    private val _updateUserFriendsResult = MutableLiveData<Result<Boolean>>()
    val updateUserFriendsResult: LiveData<Result<Boolean>> = _updateUserFriendsResult

    private val _addUserOutgoingFriendResult = MutableLiveData<Result<Boolean>>()
    val addUserOutgoingFriendResult: LiveData<Result<Boolean>> = _addUserOutgoingFriendResult

    private val _addUserIncomingFriendResult = MutableLiveData<Result<Boolean>>()
    val addUserIncomingFriendResult: LiveData<Result<Boolean>> = _addUserIncomingFriendResult

    private val _removeUserFriendResult = MutableLiveData<Result<Boolean>>()
    val removeUserFriendResult: LiveData<Result<Boolean>> = _removeUserFriendResult

    private val _removeUserFriendsResult = MutableLiveData<Result<Boolean>>()
    val removeUserFriendsResult: LiveData<Result<Boolean>> = _removeUserFriendsResult

    private val _removeUserOutgoingFriendResult = MutableLiveData<Result<Boolean>>()
    val removeUserOutgoingFriendResult: LiveData<Result<Boolean>> = _removeUserOutgoingFriendResult

    private val _removeUserIncomingFriendResult = MutableLiveData<Result<Boolean>>()
    val removeUserIncomingFriendResult: LiveData<Result<Boolean>> = _removeUserIncomingFriendResult

    fun createUser(userID: String, nickname: String) {
        viewModelScope.launch {
            _createUserResult.value = repository.createUser(userID, nickname)
        }
    }

    fun getUser(userID: String) {
        viewModelScope.launch {
            _getUserResult.value = repository.getUser(userID)
        }
    }

    fun getUserName(userID: String) {
        viewModelScope.launch {
            _getUserName.value = repository.getUserName(userID)
        }
    }

    fun getUserAge(userID: String) {
        viewModelScope.launch {
            _getUserAge.value = repository.getUserAge(userID)
        }
    }

    fun getUserNickname(userID: String) {
        viewModelScope.launch {
            _getUserNickname.value = repository.getUserNickname(userID)
        }
    }

    fun getUserFriends(userID: String) {
        viewModelScope.launch {
            _getUserFriends.value = repository.getUserFriends(userID)
        }
    }

    fun updateUserName(userID: String, newName: String) {
        viewModelScope.launch {
            _updateUserNameResult.value = repository.updateUserName(userID, newName)
        }
    }

    fun updateUserAge(userID: String, newAge: Int) {
        viewModelScope.launch {
            _updateUserAgeResult.value = repository.updateUserAge(userID, newAge)
        }
    }

    fun updateUserNickname(userID: String, newNickname: String) {
        viewModelScope.launch {
            _updateUserNicknameResult.value = repository.updateUserNickname(userID, newNickname)
        }
    }

    fun addUserOutgoingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch {
            _addUserOutgoingFriendResult.value = repository.addUserOutgoingFriend(userID, friendDocRef)
        }
    }

    fun addUserIncomingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch {
            _addUserIncomingFriendResult.value = repository.addUserIncomingFriend(userID, friendDocRef)
        }
    }

    fun removeUserFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch {
            _removeUserFriendResult.value = repository.removeUserFriend(userID, friendDocRef)
        }
    }


    fun removeUserFriends(userID: String, friendDocRefs: List<DocumentReference>) {
        viewModelScope.launch {
            _removeUserFriendsResult.value = repository.removeUserFriends(userID, friendDocRefs)
        }
    }

    fun removeUserOutgoingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch {
            _removeUserOutgoingFriendResult.value = repository.removeUserOutgoingFriend(userID, friendDocRef)
        }
    }

    fun removeUserIncomingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch {
            _removeUserIncomingFriendResult.value = repository.removeUserIncomingFriend(userID, friendDocRef)
        }
    }
}