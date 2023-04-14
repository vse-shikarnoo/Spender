package com.example.spender.data.firebase.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.data.firebase.Result
import com.example.spender.data.firebase.models.Friend
import com.example.spender.data.firebase.models.User
import com.example.spender.data.firebase.repositories.UserRepository
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor() : ViewModel() {


    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    fun updateEmail(newEmail: String){
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String){
        _password.value = newPassword
    }

    fun updateNickname(newNickname: String){
        _nickname.value = newNickname
    }


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
        viewModelScope.launch(Dispatchers.IO) {
            _createUserResult.postValue(repository.createUser(userID, nickname))
        }
    }

    fun getUser(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserResult.postValue(repository.getUser(userID))
        }
    }

    fun getUserName(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserName.postValue(repository.getUserName(userID))
        }
    }

    fun getUserAge(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserAge.postValue(repository.getUserAge(userID))
        }
    }

    fun getUserNickname(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserNickname.postValue(repository.getUserNickname(userID))
        }
    }

    fun getUserFriends(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUserFriends.postValue(repository.getUserFriends(userID))
        }
    }

    fun updateUserName(userID: String, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNameResult.postValue(repository.updateUserName(userID, newName))
        }
    }

    fun updateUserAge(userID: String, newAge: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserAgeResult.postValue(repository.updateUserAge(userID, newAge))
        }
    }

    fun updateUserNickname(userID: String, newNickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserNicknameResult.postValue(repository.updateUserNickname(userID, newNickname))
        }
    }

    fun addUserOutgoingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserOutgoingFriendResult.postValue(
                repository.addUserOutgoingFriend(userID, friendDocRef)
            )
        }
    }

    fun addUserIncomingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch(Dispatchers.IO) {
            _addUserIncomingFriendResult.postValue(
                repository.addUserIncomingFriend(userID, friendDocRef)
            )
        }
    }

    fun removeUserFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserFriendResult.postValue(repository.removeUserFriend(userID, friendDocRef))
        }
    }

    fun removeUserFriends(userID: String, friendDocRefs: List<DocumentReference>) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserFriendsResult.postValue(repository.removeUserFriends(userID, friendDocRefs))
        }
    }

    fun removeUserOutgoingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserOutgoingFriendResult.postValue(
                repository.removeUserOutgoingFriend(userID, friendDocRef)
            )
        }
    }

    fun removeUserIncomingFriend(userID: String, friendDocRef: DocumentReference) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeUserIncomingFriendResult.postValue(
                repository.removeUserIncomingFriend(userID, friendDocRef)
            )
        }
    }
}
